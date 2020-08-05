package sept.major.common.patch;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.lang.reflect.Method;


/*
    A simple POJO used for transferring data between ControllerHelper and CrudService.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PatchValue {
    private Method setter;
    private Method getter;
    private Object value;
}
