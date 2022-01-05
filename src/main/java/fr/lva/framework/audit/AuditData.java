package fr.lva.framework.audit;

import java.util.Map;

public class AuditData {

    private Object externalId;
    private Map<String, Object> metaData;

    public Object getExternalId() {
        return externalId;
    }

    public void setExternalId(Object externalId) {
        this.externalId = externalId;
    }

    public Map<String, Object> getMetaData() {
        return metaData;
    }

    public void setMetaData(Map<String, Object> metaData) {
        this.metaData = metaData;
    }
}
