package sept.major.users.response.error;

import sept.major.users.response.ResponseObject;

public interface ResponseError extends ResponseObject {
    String getField();

    String getMessage();
}
