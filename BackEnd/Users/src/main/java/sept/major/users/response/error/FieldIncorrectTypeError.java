package sept.major.users.response.error;

import lombok.Getter;

@Getter
public class FieldIncorrectTypeError extends ResponseError {

    public FieldIncorrectTypeError(String field, String expectedType, String actualType) {
        super(field, String.format("field expected to be %s but received %s", expectedType, actualType));
    }
}
