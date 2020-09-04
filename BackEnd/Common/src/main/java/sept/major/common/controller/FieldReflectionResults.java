package sept.major.common.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author Brodey Yendall
 * @version %I%, %G%
 * @since 1.0.9
 *
 * POJO for storing a field's {@link Field} and getter/setter {@link Method}
 *
 */
@Getter
@Setter
@AllArgsConstructor
public class FieldReflectionResults {
    private Field field;
    private Method setterMethod;
    private Method getterMethod;
}
