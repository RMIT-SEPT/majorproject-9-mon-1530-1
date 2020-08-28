package sept.major.common.exception;

/**
 * @author Brodey Yendall
 * @version %I%, %G%
 * @since 1.0.9
 * <p>
 * Thrown when an object was unsuccessful in being converted to another class.
 * Conversion exceptions can come in many forms so this exception makes it simple by condensing them into one.
 */
public class FailedConversionException extends Exception {
    public FailedConversionException(String message) {
        super(message);
    }
}
