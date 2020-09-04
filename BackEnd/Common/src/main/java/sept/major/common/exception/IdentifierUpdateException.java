package sept.major.common.exception;

/**
 * @author Brodey Yendall
 * @version %I%, %G%
 * @since 1.0.9
 *
 * Thrown when the user attempts to update the identifying field
 *
 */
public class IdentifierUpdateException extends Exception {
    public IdentifierUpdateException() {
        super("Cannot update value of identifier field");
    }
}
