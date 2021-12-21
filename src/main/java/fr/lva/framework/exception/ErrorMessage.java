package fr.lva.framework.exception;

public enum ErrorMessage implements IErrorMessage {

    ERROR_SAMPLE("FWK_001");

    // ------------------------------------------------------------------------
    // Constants
    // ------------------------------------------------------------------------

    /** The resource bundles defining internationalized error messages. */
    public static final String MESSAGE_BUNDLE_NAME = "fwk-error-messages";

    // ------------------------------------------------------------------------
    // Instance members
    // ------------------------------------------------------------------------

    /** The message code associated to this error message. */
    private final String code;

    // ------------------------------------------------------------------------
    // Constructors
    // ------------------------------------------------------------------------

    /**
     * Private constructor.
     *
     * @param code the message code associated to the error message.
     */
    ErrorMessage(String code) {
        this.code = code;
    }

    @Override
    public final String getCode() {
        return this.code;
    }

    @Override
    public String getMessageBundleName() {
        return MESSAGE_BUNDLE_NAME;
    }
}
