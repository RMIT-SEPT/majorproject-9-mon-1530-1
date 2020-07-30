package sept.major.users.response.error;

import lombok.Getter;

@Getter
public class MissingFieldError extends ResponseError {

    public MissingFieldError(String field) {
        super(field, "must be included in payload body");
    }
}
