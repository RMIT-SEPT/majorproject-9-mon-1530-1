package sept.major.common.exception;

/*
    Occurs when the user attempts to update the identifying field of an entity in PATCH
 */
public class IdentifierUpdateException extends Exception {
    public IdentifierUpdateException() {
        super("Cannot update value of identifier field");
    }
}
