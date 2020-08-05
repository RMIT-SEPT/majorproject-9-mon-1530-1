package sept.major.common.response;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/*
    Most responseErrors being found will be implemented in a subclass of this class however some errors are thrown with the
    generic supper class (This class).
 */
@Getter
@EqualsAndHashCode
@ToString
public class ResponseError {
    protected String field;
    protected String message;

    public ResponseError(String field, String message) {
        this.field = field;
        this.message = message;
    }

}
