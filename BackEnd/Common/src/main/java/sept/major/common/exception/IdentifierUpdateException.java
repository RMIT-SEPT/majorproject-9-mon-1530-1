package sept.major.common.exception;

public class IdentifierUpdateException extends Exception {
    public IdentifierUpdateException() {
        super("Cannot update value of identifier field");
    }
}
