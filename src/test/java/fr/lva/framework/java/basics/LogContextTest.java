package fr.lva.framework.java.basics;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

public class LogContextTest {
	
	private static final Logger LOG = LogManager.getLogger();
	
	@Test
	public void shouldAddContextTest() {
		LogContext.init("1", "foo@bar.com");
		LOG.info("Test");
	}
	
}
