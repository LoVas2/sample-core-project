package fr.lva.framework.audit;

import fr.lva.framework.utils.FieldUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.event.spi.PostInsertEvent;
import org.hibernate.event.spi.PostInsertEventListener;
import org.hibernate.event.spi.PostUpdateEvent;
import org.hibernate.event.spi.PostUpdateEventListener;
import org.hibernate.persister.entity.EntityPersister;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class DatabaseListener implements PostInsertEventListener, PostUpdateEventListener {

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = LogManager.getLogger();

    @Override
    public boolean requiresPostCommitHanding(EntityPersister persister) {
        return false;
    }

    @Override
    public void onPostInsert(PostInsertEvent event) {
        try {
			if (event.getEntity() instanceof Auditable && ((Auditable) event.getEntity()).isAuditable()
					&& ((Auditable) event.getEntity()).isAuditableOnCreate()) {
				auditDataOnCreate((Auditable) event.getEntity(), event.getId().toString());
			}
        } catch (Exception e) {
            LOG.error("Error", e);
        }
    }

    @Override
    public void onPostUpdate(PostUpdateEvent event) {
        try {
			if (event.getEntity() instanceof Auditable) {
				auditDataOnSave((Auditable) event.getEntity(), event.getId(), event.getState(), event.getOldState(),
                    event.getPersister().getPropertyNames());
			}
        } catch (Exception e) {
            LOG.error("Error", e);
        }
    }

	private void auditDataOnCreate(Auditable entity, String id) {
		AuditEvent auditEvent = AuditEvent.buildCreateEvent(Long.valueOf(id), entity.getClass().getSimpleName());
		LOG.info("Generated auditData = {}", auditEvent);
    }

	private void auditDataOnSave(Auditable entity, Serializable id, Object[] state, Object[] previousState,
                           String[] propertyNames) {
		Map<String, Object> updatedFields = new HashMap<>();
		Map<String, Field> fields = FieldUtils.getAllAuditField(entity.getClass());
		if (entity.isAuditable()) {
			for (int i = 0; i < propertyNames.length; i++) {
				if (fields.get(propertyNames[i]) != null
						&& fields.get(propertyNames[i]).getAnnotation(AuditField.class).required()
						&& state[i] != null) {
					// Required field just set => new audited object
					if (previousState == null || previousState[i] == null || checkStateChange(previousState[i], state[i])) {
						AuditEvent auditEvent = AuditEvent.buildCreateEvent(Long.valueOf(id.toString()), entity.getClass().getSimpleName());
						LOG.info("Generated auditData = {}", auditEvent);
						return;
					}
				}
				if (fields.get(propertyNames[i]) != null
						&& (previousState == null || checkStateChange(previousState[i], state[i]))) {
					updatedFields.put(propertyNames[i], state[i]);
				}
			}
			if (updatedFields.size() > 0) {
				AuditEvent auditEvent = AuditEvent.buildUpdateEvent(Long.valueOf(id.toString()), entity.getClass().getSimpleName(), updatedFields);
				LOG.info("Generated auditData = {}", auditEvent);
			}
		} else {
			for (int i = 0; i < propertyNames.length; i++) {
				if (fields.get(propertyNames[i]) != null
						&& fields.get(propertyNames[i]).getAnnotation(AuditField.class).required()
						&& state[i] == null && previousState[i] != null) {
					AuditEvent auditEvent = AuditEvent.buildDeleteEvent(Long.valueOf(id.toString()), entity.getClass().getSimpleName());
					LOG.info("Generated auditData = {}", auditEvent);
				}
			}
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

}
