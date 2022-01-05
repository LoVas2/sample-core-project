package fr.lva.framework.audit;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.AttributeConverter;
import java.util.Map;

public class AuditEventKeysConverter implements AttributeConverter<Map, String> {

    private static final Logger LOG = LogManager.getLogger();
    private final ObjectMapper objectMapper;

    public AuditEventKeysConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public String convertToDatabaseColumn(Map attribute) {
        if(attribute == null) {
            return null;
        }
        String keys = null;
        try {
            keys = objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            LOG.error("Cannot deserialize map");
        }
        return keys;
    }

    @Override
    public Map convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        Map map = null;
        try {
            map = objectMapper.readValue(dbData, Map.class);
        } catch (JsonProcessingException e) {
            LOG.error("Cannot serialize color config");
        }
        return map;
    }
}
