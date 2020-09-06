package sept.major.common.response;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * @author Brodey Yendall
 * @version %I%, %G%
 * @since 1.0.9
 *
 * POJO used to store details on a validation error for convenient access to readable when giving as an API response.
 *
 */
@Getter
@EqualsAndHashCode
@ToString
public class ValidationError {
    protected String field;
    protected String message;

    public ValidationError(String field, String message) {
        this.field = field;
        this.message = message;
    }

}
