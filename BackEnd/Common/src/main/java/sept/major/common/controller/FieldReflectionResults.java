package sept.major.common.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

@Getter
@Setter
@AllArgsConstructor
public class FieldReflectionResults {
    private Field field;
    private Method setterMethod;
    private Method getterMethod;
}
