package fr.lva.framework.utils;

import fr.lva.framework.audit.AuditField;
import fr.lva.framework.exception.ErrorMessage;
import fr.lva.framework.exception.TechnicalException;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

public class FieldUtils {

    public static Map<String, Field> getAllAuditField(Class<?> clazz) {
            Map<String, Field> fields = new TreeMap<>();
            Arrays.stream(clazz.getDeclaredFields())
                    .filter(f -> f.isAnnotationPresent(AuditField.class))
                    .forEach(f -> fields.put(f.getName(), f));
            Class<?> superClazz = clazz.getSuperclass();
            if (superClazz != null) {
                fields.putAll(getAllAuditField(superClazz));
            }
            return fields;
    }

    public static Field extractField(Class<?> clazz, String fieldName) {
        Field field = null;
        try {
            field = clazz.getDeclaredField(fieldName);
            return field;
        } catch (NoSuchFieldException nsfe) {
            if (clazz.getSuperclass() != null) {
                field = extractField(clazz.getSuperclass(), fieldName);
            }
        }
        if (field == null) {
            throw new TechnicalException(ErrorMessage.AUDIT);
        }
        return field;
    }

}
