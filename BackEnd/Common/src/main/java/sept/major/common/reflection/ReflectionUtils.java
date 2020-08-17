package sept.major.common.reflection;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import static java.lang.Character.toLowerCase;

public class ReflectionUtils {
    public static boolean isSetter(Method method) {
        return (!Modifier.isStatic(method.getModifiers())
                && !Modifier.isAbstract(method.getModifiers())
                && method.getReturnType() == Void.TYPE
                && method.getParameterCount() == 1
                && method.getName().startsWith("set")
                && Modifier.isPublic(method.getModifiers()));
    }

    public static boolean isGetter(Method method) {
        return (!Modifier.isStatic(method.getModifiers())
                && !Modifier.isAbstract(method.getModifiers())
                && method.getReturnType() != Void.TYPE
                && method.getParameterCount() == 0
                && method.getName().startsWith("get")
                && Modifier.isPublic(method.getModifiers()));
    }

    public static String removePrefixFromMethod(Method method, String prefix) {
        StringBuilder stringBuilder = new StringBuilder(method.getName().replaceFirst(prefix, ""));
        stringBuilder.setCharAt(0, toLowerCase(stringBuilder.charAt(0)));
        return stringBuilder.toString();
    }
}
