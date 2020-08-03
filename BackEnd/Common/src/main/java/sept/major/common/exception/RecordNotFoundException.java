package sept.major.common.exception;

import lombok.Getter;

public class RecordNotFoundException extends Exception {

    @Getter
    private String message;

    public RecordNotFoundException(String message) {
        super(message);
        this.message = message;
    }
}
