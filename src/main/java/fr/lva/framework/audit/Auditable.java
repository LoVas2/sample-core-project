package fr.lva.framework.audit;

public interface Auditable {

	default boolean isAuditable() {
		return false;
	}

	default boolean isAuditableOnCreate() {
		return true;
	}


}
