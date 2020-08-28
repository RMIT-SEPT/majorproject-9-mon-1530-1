package sept.major.common.exception;

import lombok.Getter;

/**
 * @author Brodey Yendall
 * @version %I%, %G%
 * @since 1.0.9
 * <p>
 * Thrown when a record couldn't be retrieved, used in most crud implementations
 */
/*
    Occurs in GET and PATCH when there is not a saved record with the identifier provided.
 */
public class RecordNotFoundException extends Exception {

    @Getter
    private String message;

    public RecordNotFoundException(String message) {
        super(message);
        this.message = message;
    }
}
