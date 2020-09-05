package sept.major.common.exception;

/**
 * @author Brodey Yendall
 * @version %I%, %G%
 * @since 1.0.9
 * <p>
 * Thrown when the user attempts to create an entity that conflicts with an existing entity.
 * Typically caused due to the entity having the same id as an existing entity.
 */
public class RecordAlreadyExistsException extends Exception {
    public RecordAlreadyExistsException() {
        super("Cannot create entity because an entity with given identifier already exists");
    }

    /**
     * @param message The message to describe the exception
     * @since 1.1.1
     * Allows you to set a custom message to the exception
     */
    public RecordAlreadyExistsException(String message) {
        super(message);
    }
}
