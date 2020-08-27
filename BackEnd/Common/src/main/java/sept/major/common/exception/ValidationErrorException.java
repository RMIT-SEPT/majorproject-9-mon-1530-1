package sept.major.common.exception;

import lombok.Getter;
import lombok.Setter;
import sept.major.common.response.ValidationError;

import java.util.List;

/**
 * @author Brodey Yendall
 * @version %I%, %G%
 * @since 1.0.9
 * <p>
 * Thrown in order to carry validation errors up the stack trace.
 * All validation errors are stored within the exception, see {@link #getValidationErrors()}
 */
/*
    Used to transfer validationErrors back to the calling method if any exist.
 */
public class ValidationErrorException extends Exception {
    /**
     * The validation errors being thrown up the stack trace
     */
    @Getter
    @Setter
    private List<ValidationError> validationErrors;

    public ValidationErrorException(List<ValidationError> validationErrors) {
        super("Found errors in API input which were unhandled");
        this.validationErrors = validationErrors;
    }
}
