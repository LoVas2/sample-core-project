package fr.lva.framework.audit;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "message_audit_event")
public class AuditEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private Status status;
    @Enumerated(EnumType.STRING)
    private Type type;
    private Long objectId;
    @Enumerated(EnumType.STRING)
    private ObjectHandled objectHandled;
    @Convert(converter = AuditEventKeysConverter.class)
    private Map<String, Object> fields;
    private Instant createdDate;
    private Instant updatedDate;
    @Column(name = "error_message", length = 4096)
    private String errorMessage;

    public AuditEvent() {

    }

    public static AuditEvent buildCreateEvent(Long id, String className) {
        AuditEvent auditEvent = new AuditEvent();
        auditEvent.setObjectId(id);
        auditEvent.setStatus(Status.TODO);
        auditEvent.setType(Type.CREATE);
        auditEvent.setObjectHandled(ObjectHandled.getByClassName(className));
        return auditEvent;
    }

    public static AuditEvent buildUpdateEvent(Long id, String className, Map<String, Object> fields) {
        AuditEvent auditEvent = new AuditEvent();
        auditEvent.setObjectId(id);
        auditEvent.setStatus(Status.TODO);
        auditEvent.setType(Type.UPDATE);
        auditEvent.setObjectHandled(ObjectHandled.getByClassName(className));
        auditEvent.setFields(fields);
        return auditEvent;
    }

    public static AuditEvent buildDeleteEvent(Long id, String className) {
        AuditEvent auditEvent = new AuditEvent();
        auditEvent.setObjectId(id);
        auditEvent.setStatus(Status.TODO);
        auditEvent.setType(Type.DELETE);
        auditEvent.setObjectHandled(ObjectHandled.getByClassName(className));
        return auditEvent;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Long getObjectId() {
        return objectId;
    }

    private void setObjectId(Long objectId) {
        this.objectId = objectId;
    }

    public ObjectHandled getObjectHandled() {
        return objectHandled;
    }

    private void setObjectHandled(ObjectHandled objectHandled) {
        this.objectHandled = objectHandled;
    }

    public Type getType() {
        return type;
    }

    private void setType(Type type) {
        this.type = type;
    }

    public Map<String, Object> getFields() {
        return fields;
    }

    public void setFields(Map<String, Object> fields) {
        this.fields = fields;
    }

    public void addField(String key, Object value) {
        if (this.fields == null) {
            this.fields = new HashMap<>();
        }
        this.fields.put(key, value);
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Instant updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public enum Type {
        CREATE, UPDATE, DELETE
    }

    public enum Status {
        TODO, SEND, ERROR
    }

    @PrePersist
    public void prePersist() {
        createdDate = Instant.now();
        updatedDate = Instant.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedDate = Instant.now();
    }

    @Override
    public String toString() {
        return "AuditEvent [id=" + id + ", status=" + status + ", type=" + type + ", objectId=" + objectId
                + ", objectHandled=" + objectHandled + ", fields=" + fields + "]";
    }

}
