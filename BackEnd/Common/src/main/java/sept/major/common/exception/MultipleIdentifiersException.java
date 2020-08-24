package sept.major.common.exception;

public class MultipleIdentifiersException extends RuntimeException {
    public MultipleIdentifiersException(Class entityClass) {
        super("Multiple identifiers found found in the provided entity: " + entityClass.toString());
    }
}
