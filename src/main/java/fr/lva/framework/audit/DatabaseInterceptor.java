package fr.lva.framework.audit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.lva.framework.utils.FieldUtils;
import fr.lva.framework.utils.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;
import org.springframework.util.ReflectionUtils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class DatabaseInterceptor extends EmptyInterceptor {

    private static final Logger LOG = LogManager.getLogger();

    public DatabaseInterceptor() {
    }

    @Override
    public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
        auditDataOnSave(entity, state, propertyNames);
        return false;
    }

    @Override
    public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState,
                                String[] propertyNames, Type[] types) {
        auditData(entity, id, currentState, previousState, propertyNames);
        return false;
    }

    private void auditDataOnSave(Object entity,Object[] state, String[] propertyNames) {
        try {
            if (entity instanceof Auditable) {
                Map<String, Field> fields = FieldUtils.getAllAuditField(entity.getClass());
                for (int i = 0; i < propertyNames.length; i++) {
                    if (fields.get(propertyNames[i]) != null && fields.get(propertyNames[i]).getAnnotation(AuditField.class).required() &&
                            state[i] != null) {
                        sendCreateData(state[i], entity.getClass());
                    }
                }
            }
        } catch (IllegalArgumentException e) {
            LOG.error("Not sending : " + entity.getClass().getSimpleName(), e);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }

    private void auditData(Object entity, Serializable id, Object[] state, Object[] previousState, String[] propertyNames) {
        try {
            if (entity instanceof Auditable) {
                AuditData auditData = new AuditData();
                Map<String, Field> fields = FieldUtils.getAllAuditField(entity.getClass());
                for (int i = 0; i < propertyNames.length; i++) {
                    if (fields.get(propertyNames[i]) != null &&
                            (previousState == null || checkStateChange(previousState[i], state[i]))) {
                        addMetaData(auditData, propertyNames[i], state[i], fields.get(propertyNames[i]));
                    }
                }
                sendAuditData(auditData);
            }
        } catch (IllegalArgumentException e) {
            LOG.info("Not sending : " + entity.getClass().getSimpleName());
        } catch (JsonProcessingException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    private boolean checkStateChange(Object oldValue, Object newValue) {
        if (oldValue == null && newValue != null) {
            return true;
        }
        if (oldValue != null && newValue == null) {
            return true;
        }
        if (oldValue == newValue) {
            return false;
        }
        if (!oldValue.equals(newValue)) {
            return true;
        }
        return false;
    }

    private void addMetaData(AuditData auditData, String fieldName, Object newValue, Field field)
            throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, NoSuchFieldException {
        LOG.info("New value for field {} = {}", fieldName, newValue);
        // Compare value
        AuditField auditField = field.getAnnotation(AuditField.class);
        // Construct path in data structure. "path.name"
        String path = (StringUtils.isBlank(auditField.path()) ? "" : auditField.path() + ".") +
                (StringUtils.isBlank(auditField.name()) ? fieldName : auditField.name());
        // Get map of the parent
        Map<String, Object> map = auditData.getMetaData();
        String[] keys = path.split("\\.");
        for (int j = 0; j < keys.length - 1; j++) {
            map = getParentLevel(map, keys[j]);
        }
        // Add action verb
        Map<String, Object> levelAction = (Map<String, Object>) map.computeIfAbsent("create_or_update_by_id", (v) -> new HashMap<>());
        Object data = initObject(auditField, levelAction.get(keys[keys.length - 1]));
        // Set object value
        data = getObjectValue(fieldName, newValue, auditField, data);
        if (auditField.required() && data == null) {
            LOG.info("Message not send missing value for {}", fieldName);
            throw new IllegalArgumentException();
        } else if (auditField.externalIdField()) {
            auditData.setExternalId(data);
        } else if (!auditField.ignored()) {
            levelAction.put(keys[keys.length - 1], data);
        }
    }

    private Object initObject(AuditField auditField, Object levelData) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Object data = levelData;
        // Instantiate object if null
        if (data == null && auditField.type().equals(Boolean.class)) {
            data = Boolean.FALSE;
        } else if (data == null) {
            data = auditField.type().getDeclaredConstructor().newInstance();
        }
        return data;
    }

    private Object getObjectValue(String fieldName, Object newValue, AuditField auditField, Object data) throws NoSuchFieldException {
        String[] nestedFieldPath = auditField.embeddedPath().split("\\.");
        if (nestedFieldPath.length > 1 && nestedFieldPath[0].equals(fieldName)) {
            Long test = 0L;
            Field f = newValue.getClass().getDeclaredField(nestedFieldPath[0]);
            ReflectionUtils.makeAccessible(f);
            String value = String.valueOf(ReflectionUtils.getField(f, newValue));
            data = value;
        }
        if (data instanceof Collection) {
            ((Collection) data).add(newValue);
        } else {
            data = newValue;
        }
        return data;
    }

    private static Map<String, Object> getParentLevel(Map<String, Object> metaData, String key) {
        if (metaData.get(key) instanceof Map) {
            return (Map<String, Object>) metaData.get(key);
        } else {
            Map<String, Object> newMap = new HashMap<>();
            metaData.put(key, newMap);
            return newMap;
        }
    }

    private void sendCreateData(Object saveId, Class clazz) throws JsonProcessingException {
        Long id = null;
        if (saveId instanceof Long) {
            id = (Long) saveId;
        } else if (saveId instanceof String) {
            id = Long.parseLong((String) saveId);
        }
        if (id != null) {
            AuditEvent auditEvent = AuditEvent.buildCreateEvent(id, clazz.getSimpleName());
            ObjectMapper om = new ObjectMapper();
            LOG.info("Generated auditData = {}", om.writeValueAsString(auditEvent));
        } else {
            LOG.error("Cannot send createData Event, id class = " + saveId.getClass());
        }
    }

    private void sendAuditData(AuditData auditData) throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        LOG.info("Generated auditData = {}", om.writeValueAsString(auditData));
    }

}
