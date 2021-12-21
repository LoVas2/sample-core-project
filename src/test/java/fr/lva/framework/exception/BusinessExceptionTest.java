package fr.lva.framework.exception;

import org.junit.Assert;
import org.junit.Test;

import java.util.Locale;

public class BusinessExceptionTest {

    @Test
    public void createTest() {
        BusinessException businessException = new BusinessException(ErrorMessage.ERROR_SAMPLE, "SAMPLE_TEST");
        Assert.assertEquals("Message does not match", "Erreur Test avec paramètre var=SAMPLE_TEST", businessException.getMessage());
        Assert.assertEquals("Error does not match", ErrorMessage.ERROR_SAMPLE, businessException.getError());
        Assert.assertEquals("Code does not match", "FWK_001", businessException.getMessageCode());
    }

    @Test
    public void exceptionWithCauseTest() {
        BusinessException businessException = new BusinessException(ErrorMessage.ERROR_SAMPLE, new IllegalArgumentException(), "SAMPLE_TEST");
        Assert.assertTrue("Cause not instance of IllegalArgumentException", businessException.getCause() instanceof IllegalArgumentException);
    }

    @Test
    public void usLocalTest() {
        BusinessException businessException = new BusinessException(ErrorMessage.ERROR_SAMPLE, "SAMPLE_TEST");
        Assert.assertEquals("Message does not match", "Sample Error with parameter var=SAMPLE_TEST", businessException.getMessage(Locale.US));
    }

    @Test(expected = Exception.class)
    public void throwItTest() throws BusinessException {
        throw new BusinessException(ErrorMessage.ERROR_SAMPLE);
    }
}
