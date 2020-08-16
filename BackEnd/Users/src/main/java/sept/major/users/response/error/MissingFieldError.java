package sept.major.users.response.error;

import lombok.Getter;

@Getter
public class MissingFieldError implements ResponseError {
    private String field;

    private String message = "must be included in payload body";

    public MissingFieldError(String field) {
        this.field = field;
    }
}
