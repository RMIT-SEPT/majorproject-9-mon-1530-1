package sept.major.common.exception;

import lombok.Getter;

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
