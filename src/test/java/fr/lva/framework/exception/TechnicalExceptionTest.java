package fr.lva.framework.exception;

import org.junit.Assert;
import org.junit.Test;

import java.util.Locale;

public class TechnicalExceptionTest {

    @Test
    public void createTest() {
        TechnicalException technicalException = new TechnicalException(ErrorMessage.ERROR_SAMPLE, "SAMPLE_TEST");
        Assert.assertEquals("Message does not match", "[FWK_001] Erreur Test avec paramètre var=SAMPLE_TEST", technicalException.getMessage());
        Assert.assertEquals("Error does not match", ErrorMessage.ERROR_SAMPLE, technicalException.getError());
        Assert.assertEquals("Code does not match", "FWK_001", technicalException.getMessageCode());
    }

    @Test
    public void exceptionWithCauseTest() {
        TechnicalException technicalException = new TechnicalException(ErrorMessage.ERROR_SAMPLE, new IllegalArgumentException(), "SAMPLE_TEST");
        Assert.assertTrue("Cause not instance of IllegalArgumentException", technicalException.getCause() instanceof IllegalArgumentException);
    }

    @Test
    public void usLocalTest() {
        TechnicalException technicalException = new TechnicalException(ErrorMessage.ERROR_SAMPLE, "SAMPLE_TEST");
        Assert.assertEquals("Message does not match", "[FWK_001] Sample Error with parameter var=SAMPLE_TEST", technicalException.getMessage(Locale.US));
    }

    @Test(expected = RuntimeException.class)
    public void throwItTest() {
        throw new TechnicalException(ErrorMessage.ERROR_SAMPLE);
    }
}
