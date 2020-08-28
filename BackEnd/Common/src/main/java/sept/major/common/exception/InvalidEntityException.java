package sept.major.common.exception;

/**
 * @author Brodey Yendall
 * @version %I%, %G%
 * @since 1.0.9
 *
 * Thrown when there was an error in the entity (implementation of AbstractEntity) that ControllerHelper, CrudService etc use.
 * Often occurs whens the entity doesn't contain an accessible entity constructor or the a poor code alteration.
 *
 */
public class InvalidEntityException extends RuntimeException {
    public InvalidEntityException(String message) {
        super(message);
    }
}
