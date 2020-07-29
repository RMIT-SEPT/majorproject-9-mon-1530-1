package sept.major.users.response.error;

import lombok.Getter;

@Getter
public class FieldMissingError implements ResponseError {
    private String field;
    private String message;

    public FieldMissingError(String field) {
        this.field = field;
        this.message = String.format("must be included in payload body");
    }
}
