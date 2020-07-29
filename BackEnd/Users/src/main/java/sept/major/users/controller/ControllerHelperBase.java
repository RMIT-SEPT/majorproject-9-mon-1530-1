package sept.major.users.controller;

import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Character.toLowerCase;

@Service
public abstract class ControllerHelperBase {

    private HashMap<Class, HashMap<String, Method>> classMapHashMap = new HashMap<>();


    protected Map<String, Method> getMethods(Class entityClass) {
        HashMap<String, Method> mappedMethods = classMapHashMap.get(entityClass);
        if (mappedMethods == null) {
            mappedMethods = new HashMap<>();
            Method[] methods = entityClass.getDeclaredMethods();

            for (Method method : methods) {
                if (isSetter(method)) {
                    mappedMethods.put(getAttributeFromSetter(method), method);
                }
            }

            classMapHashMap.put(entityClass, mappedMethods);
        }

        return mappedMethods;
    }

    private boolean isSetter(Method method) {
        return (!Modifier.isStatic(method.getModifiers())
                && !Modifier.isAbstract(method.getModifiers())
                && method.getReturnType() == Void.TYPE
                && method.getParameterCount() == 1
                && method.getName().startsWith("set")
                && Modifier.isPublic(method.getModifiers()));
    }

    private String getAttributeFromSetter(Method method) {
        StringBuilder stringBuilder = new StringBuilder(method.getName().replaceFirst("set", ""));
        stringBuilder.setCharAt(0, toLowerCase(stringBuilder.charAt(0)));
        return stringBuilder.toString();
    }

}
