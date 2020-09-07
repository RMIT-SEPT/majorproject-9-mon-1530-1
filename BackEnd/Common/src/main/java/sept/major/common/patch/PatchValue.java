package sept.major.common.patch;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.lang.reflect.Field;
import java.lang.reflect.Method;


/**
 * @author Brodey Yendall
 * @version %I%, %G%
 * @since 1.0.9
 *
 * Simple POJO that links a field's {@link Field}, getter/setter {@link Method} and new value.
 * Used when transferring data between ControllerHelper and CrudService.
 *
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PatchValue {
    private Field field;
    private Method setter;
    private Method getter;
    private Object value;
}
