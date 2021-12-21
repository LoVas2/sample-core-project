package fr.lva.framework.exception;

public interface IErrorMessage {

    /**
     * Returns the error code
     *
     * @return the error code
     */
    String getCode();

    /**
     * Returns the error bundle properties file name
     *
     * @return the error bundle properties file name
     */
    String getMessageBundleName();
}
