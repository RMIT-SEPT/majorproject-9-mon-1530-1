package sept.major.common.response;

import lombok.Getter;

/*
    A field is missing from the API input
 */
@Getter
public class FieldMissingError extends ResponseError {
    public FieldMissingError(String field) {
        super(field, "must be included in payload body");
    }
}
