package sept.major.common.exception;

/*
    This exception will occur whenever the implemented entity provided to either ControllerHelp or CrudService does not have
        * An accessible empty constructor
        * At least one field with a getter and setter
    This exception could also occur when code is changed incorrectly and checks are missed
 */
public class InvalidEntityException extends RuntimeException {
    public InvalidEntityException(String message) {
        super(message);
    }
}
