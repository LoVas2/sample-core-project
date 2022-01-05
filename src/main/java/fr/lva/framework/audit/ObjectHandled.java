package fr.lva.framework.audit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public enum ObjectHandled {

	USER("User");

	private static final Logger LOG = LogManager.getLogger();

	private String className;

	private String getClassName() {
		return this.className;
	}

	ObjectHandled(String className) {
		this.className = className;
	}

	public static ObjectHandled getByClassName(String className) {
		for (ObjectHandled o : ObjectHandled.values()) {
			if (o.getClassName().equals(className)) {
				return o;
			}
		}
		LOG.warn("Object not found for className={}", className);
		return null;
	}

}
