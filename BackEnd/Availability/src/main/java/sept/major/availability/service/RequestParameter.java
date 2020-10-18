package sept.major.availability.service;

import lombok.*;

/**
 * encapsulation of key/value in a convenience class. 
 *
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
