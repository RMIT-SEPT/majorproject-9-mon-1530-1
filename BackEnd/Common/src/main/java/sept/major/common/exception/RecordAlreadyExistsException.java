package sept.major.common.exception;

/*
    Occurs when the user attempts to create a create with the same identifying value of an existing record
 */
public class RecordAlreadyExistsException extends Exception {
    public RecordAlreadyExistsException() {
        super("Cannot create entity because an entity with given identifier already exists");
    }
}
