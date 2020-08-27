package sept.major.common.exception;

import lombok.Getter;
import lombok.Setter;
import sept.major.common.response.ResponseError;

import java.util.List;

/*
    Used to transfer responseErrors back to the calling method if any exist.
 */
public class ResponseErrorException extends Exception {
    @Getter
    @Setter
    private List<ResponseError> responseErrors;

    public ResponseErrorException(List<ResponseError> responseErrors) {
        super("Found errors in API input which were unhandled");
        this.responseErrors = responseErrors;
    }
}
