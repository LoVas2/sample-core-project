package fr.lva.framework.exception;

/**
 * Error reported to the users
 */
@SuppressWarnings("serial")
public class BusinessException extends AbstractBusinessException {

    protected final transient IErrorMessage error;

    // ------------------------------------------------------------------------
    // Constructors
    // ------------------------------------------------------------------------

    /**
     * Constructs a new exception with the specified error message and arguments to build a detail
     * message from the format associated to the error message.
     *
     * @param error the error message.
     * @param data the arguments to build the detail message from the error message format.
     */
    public BusinessException(IErrorMessage error, Object... data) {
        super(error.getCode(), data);
        this.error = error;
    }

    /**
     * Constructs a new exception with the specified error message and arguments to build a detail
     * message from the format associated to the error message.
     *
     * @param error the error message.
     * @param cause the cause. A <code>null</code> value is allowed to indicate that the cause is
     *        nonexistent or unknown.
     * @param data the arguments to build the detail message from the error message format.
     */
    public BusinessException(IErrorMessage error, Throwable cause, Object... data) {
        super(error.getCode(), cause, data);
        this.error = error;
    }

    @Override
    public String getMessageBundleName() {
        return error != null ? error.getMessageBundleName() : null;
    }

    public IErrorMessage getError() {
        return error;
    }

}
