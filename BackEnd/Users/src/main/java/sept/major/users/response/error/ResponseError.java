package sept.major.users.response.error;

import lombok.Getter;

@Getter
public class ResponseError {
    protected String field;
    protected String message;

    public ResponseError(String field, String message) {
        this.field = field;
        this.message = message;
    }

}
