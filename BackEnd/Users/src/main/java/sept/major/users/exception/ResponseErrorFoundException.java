package sept.major.users.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sept.major.users.response.error.ResponseError;

import java.util.Set;

public class ResponseErrorFoundException extends Exception {

    private Set<ResponseError> responseErrors;

    public ResponseErrorFoundException(Set<ResponseError> responseErrors) {
        super("Response errors were unhandled");
        this.responseErrors = responseErrors;
    }

    public ResponseEntity<Set<ResponseError>> generateResponseEntity() {
        return new ResponseEntity<>(responseErrors, HttpStatus.BAD_REQUEST);
    }
}
