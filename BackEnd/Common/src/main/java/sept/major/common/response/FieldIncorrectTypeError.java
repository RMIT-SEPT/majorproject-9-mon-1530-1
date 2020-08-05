package sept.major.common.response;

import lombok.Getter;

/*
    Field provided in API input is a different type to the field in the entity class
 */
@Getter
public class FieldIncorrectTypeError extends ResponseError {

    public FieldIncorrectTypeError(String field, String expectedType, String actualType) {
        super(field, String.format("field expected to be %s but received %s", expectedType, actualType));
    }
}
