package sept.major.users.response.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;

public class ResponseErrorManager {

    ArrayList<ResponseError> responseErrors = new ArrayList<>();

    public ResponseEntity getResponseEntity() {
        if (responseErrors.size() == 0) {
            throw new ResponseErrorManagerException("Expected error response when there were no errors present");
        } else if (responseErrors.size() == 1) {
            return new ResponseEntity(responseErrors.get(0), HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity(responseErrors, HttpStatus.BAD_REQUEST);
        }
    }

    public boolean hasErrors() {
        return !responseErrors.isEmpty();
    }

    public void addError(ResponseError responseError) {
        responseErrors.add(responseError);
    }

    public void clear() {
        responseErrors.clear();
    }

}
