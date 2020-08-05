package sept.major.common.exception;

import lombok.Getter;
import lombok.Setter;
import sept.major.common.response.ResponseError;

import java.util.Set;

/*
    Used to transfer responseErrors back to the calling method if any exist.
 */
public class ReponseErrorException extends Exception {
    @Getter
    @Setter
    private Set<ResponseError> responseErrors;

    public ReponseErrorException(Set<ResponseError> responseErrors) {
        super("Found errors in API input which were unhandled");
    }
}
