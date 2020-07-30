package sept.major.users.response.error;

import lombok.Getter;

@Getter
public class FieldMissingError extends ResponseError {
    public FieldMissingError(String field) {
        super(field, "must be included in payload body");
    }
}
