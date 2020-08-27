package sept.major.common.exception;

/**
 * @author Brodey Yendall
 * @version %I%, %G%
 * @since 1.0.9
 * <p>
 * Thrown when the user attempts to create an entity that conflicts with an existing entity.
 * Typically caused due to the entity having the same id as an existing entity.
 */
/*
    Occurs when the user attempts to create a create with the same identifying value of an existing record
 */
public class RecordAlreadyExistsException extends Exception {
    public RecordAlreadyExistsException() {
        super("Cannot create entity because an entity with given identifier already exists");
    }
}
