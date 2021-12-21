package fr.lva.framework.exception;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * <code>TechnicalExceptions</code> are thrown to indicate technical or environmental (i.e.
 * configuration, hardware...) error conditions that need not be handled by the application code.
 * These errors shall be notified to the system administrator.
 *
 */
public abstract class AbstractTechnicalException extends RuntimeException {

    private static final Logger LOG = LogManager.getLogger();

    /**
     * The message code, i.e. the name of the message format in the resource bundle or
     * <code>null</code> if the message is specified as a string directly containing the message
     * format or text.
     */
    private final String             messageCode;
    /** The message arguments, if any. */
    private final transient Object[] messageArgs;

    private static final int STRING_BUFFER_SIZE = 256;

    /**
     * Constructs a new exception with the specified message code and arguments to build a detail
     * message from the format associated to the message code.
     * <p>
     * The message code can be either the actual message format or the identifier of a resource
     * (defined in the {@link #getMessageBundleName exception type resource bundle}) that contains
     * the message format.
     * </p>
     *
     * @param code the message code or format. In the latter case, it shall be compliant with the
     *        grammar defined by {@link MessageFormat}.
     * @param data the arguments to build the detail message from the format.
     */
    protected AbstractTechnicalException(String code, Object... data) {
        this(code, null, data);
    }

    /**
     * Constructs a new exception with the specified message code and the arguments to build a
     * detail message from the format associated to the message code.
     * <p>
     * The message code can be either the actual message format or the identifier of a resource
     * (defined in the {@link #getMessageBundleName exception type resource bundle}) that contains
     * the message format.
     * </p>
     * <p>
     * Note that the detail message associated with <code>cause</code> is <em>not</em> automatically
     * incorporated in this exception's detail message.
     * </p>
     *
     * @param code the message code or format. In the latter case, it shall be compliant with the
     *        grammar defined by {@link MessageFormat}.
     * @param cause the cause. A <code>null</code> value is allowed to indicate that the cause is
     *        nonexistent or unknown.
     * @param data the arguments to build the detail message from the format.
     */
    protected AbstractTechnicalException(String code, Throwable cause, Object... data) {
        super(code, cause);
        this.messageCode = code;
        if (data != null) {
            this.messageArgs = data.clone();
        } else {
            this.messageArgs = null;
        }
    }

    // ------------------------------------------------------------------------
    // Exception contract support
    // ------------------------------------------------------------------------

    /**
     * Returns the detail message for this exception, formatting it from the message code and
     * arguments if need be.
     * <p>
     * This default implementation invokes {@link #getMessage(Locale)} with the default
     * system locale.
     * </p>
     * <p>
     * Subclasses requiring specific user locale support shall overwrite this method and consider
     * overwriting {@link #getLocalizedMessage()} as well.
     * </p>
     *
     * @return the detail message.
     * @see #getLocalizedMessage()
     */
    @Override
    public String getMessage() {
        return this.getMessage(Locale.getDefault());
    }

    /**
     * Returns the detail message for this exception, formatting it from the message code and
     * arguments if need be.
     *
     * @return the detail message.
     */
    @Override
    public String getLocalizedMessage() {
        return this.getMessage(Locale.getDefault());
    }

    // ------------------------------------------------------------------------
    // Specific implementation
    // ------------------------------------------------------------------------

    /**
     * Returns the message code.
     *
     * @return the message code.
     */
    public String getMessageCode() {
        return this.messageCode;
    }

    /**
     * Returns the name of the message bundle to use for formatting error messages for this
     * exception type. The returned name is relative to the classpath.
     * <p>
     * Subclasses should overwrite this method to enforce their own naming convention if need be.
     * </p>
     *
     * @return the path to the message bundle for this exception or <code>null</code> if no message
     *         formatting shall be attempted.
     */
    abstract protected String getMessageBundleName();

    /**
     * Formats the exception detail message.
     *
     * @param locale the locale for which the message shall be formatted. If no locale is specified,
     *        the default system one will be used.
     * @return a formatted detail message.
     */
    protected String getMessage(Locale locale) {
        StringBuilder buf = new StringBuilder(STRING_BUFFER_SIZE);
        if (this.getMessageCode() != null) {
            buf.append('[').append(this.getMessageCode()).append("] ");
        }
        buf.append(
            this.formatMessage(this.getMessageFormat(this.getMessageCode(), locale), locale));
        return buf.toString();
    }

    /**
     * Formats the exception detail message.
     *
     * @param format the format for the detailed message, compliant with the grammar defined by
     *        {@link MessageFormat}.
     * @param locale the target locale.
     * @return a formatted detail message.
     */
    protected String formatMessage(String format, Locale locale) {
        String message = null;
        if (this.messageCode != null) {
            if (format != null) {
                message = formatMessageFromFormat(format, locale);
            }
            if (message == null) {
                message = formatMessageFromMessageCode();
            }
        } else {
            message = super.getMessage();
        }
        return message;
    }

    /**
     * @return a formated message base on the {@link #messageCode}.
     */
    private String formatMessageFromMessageCode() {
        String message;
        Object[] args = this.messageArgs;
        if ((args != null) && (args.length != 0)) {
            StringBuilder buf = new StringBuilder(this.messageCode);
            // No format found for this message identifier
            // or message formatting error encountered.
            // => Just dump the message identifier and each of the
            // arguments as strings.
            for (Object arg : args) {
                buf.append(" \"").append(arg).append('\"');
            }
            message = buf.toString();
        } else {
            message = this.messageCode;
        }
        return message;
    }

    /**
     * @param format The format of the message which will be returned
     * @param locale The locale used to build the formated message
     * @return a formated message based on the given format
     */
    private String formatMessageFromFormat(String format, Locale locale) {
        Object[] args = this.messageArgs;
        String message;
        if (args != null) {
            // Arguments are provided. => Format message.
            try {
                message = new MessageFormat(format, locale).format(args);
            } catch (IllegalArgumentException e) {
                LOG.warn(e);
                throw e;
            }
        } else {
            // No arguments. => use the format string as message.
            message = format;
        }
        return message;
    }

    /**
     * Returns the format associated to the specified key.
     *
     * @param key the name of the message format to retrieve.
     * @param locale the locale for which the message format shall be retrieved.
     * @return the message format associated to the key, the key itself (if no corresponding
     *         resource was found) or <code>null</code> (if the {@link #getMessageBundleName
     *         resource bundle} can not be loaded).
     */
    private final String getMessageFormat(String key, Locale locale) {
        String format = key;
        String bundleName = this.getMessageBundleName();
        Locale localTMP;
        if ((key != null) && (bundleName != null)) {
            if (locale == null) {
                localTMP = Locale.getDefault();
            } else {
                localTMP = locale;
            }
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            if (cl == null) {
                cl = this.getClass().getClassLoader();
            }
            ResourceBundle bundle = null;
            try {
                bundle = ResourceBundle.getBundle(bundleName, localTMP, cl);
                format = bundle.getString(key);
            } catch (MissingResourceException mre) {
                // Ignore: a message format may have been directly
                // provided, if no localization is needed.
                format = (bundle == null) ? null : key;
                LOG.trace("Failed to resolve key \"{}\" in bundle \"{}\"", key, bundleName);
            }
        }
        return format;
    }

}
