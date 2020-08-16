package sept.major.users.response.error;

import lombok.Getter;

@Getter
public class FieldIncorrectTypeError implements ResponseError {
    private String field;
    private String message;

    public FieldIncorrectTypeError(String field, String expectedType, String actualType) {
        this.field = field;
        this.message = String.format("field expected to be %s but received %s", expectedType, actualType);
    }
}
