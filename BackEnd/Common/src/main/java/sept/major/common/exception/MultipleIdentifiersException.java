package sept.major.common.exception;

/**
 * @author Brodey Yendall
 * @version %I%, %G%
 * @since 1.0.9
 * <p>
 * Thrown when entity (implementation of AbstractEntity) that ControllerHelper, CrudService etc use contains two fields with
 * the {@link javax.persistence.Id} annotation
 */
public class MultipleIdentifiersException extends RuntimeException {
    public MultipleIdentifiersException(Class entityClass) {
        super("Multiple identifiers found found in the provided entity: " + entityClass.toString());
    }
}
