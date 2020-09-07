package sept.major.common.testing;

import lombok.*;


/**
 * @author Brodey Yendall
 * @version %I%, %G%
 * @since 1.1.1
 * <p>
 * POJO used represent a request parameter. Contains the key for the request parameter and it's value
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class RequestParameter {
    private String key;
    private String value;

    @Override
    public String toString() {
        return String.format("%s=%s", key, value);
    }
}
