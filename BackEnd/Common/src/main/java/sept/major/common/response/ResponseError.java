package sept.major.common.response;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

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
