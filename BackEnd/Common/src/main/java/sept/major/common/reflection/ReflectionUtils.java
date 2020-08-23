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
                && (method.getName().startsWith("get") || method.getName().startsWith("is"))
                && Modifier.isPublic(method.getModifiers()));
    }

    public static String removePrefixFromGetter(Method method) {
        String startingString = "";
        if(method.getName().startsWith("get")) {
            startingString = method.getName().replaceFirst("get", "");
        } else if(method.getName().startsWith("is")) {
            startingString = method.getName().replaceFirst("is", "");
        }
        if(startingString.equals("")) {
            return null;
        } else {
            StringBuilder stringBuilder = new StringBuilder(startingString);
            stringBuilder.setCharAt(0, toLowerCase(stringBuilder.charAt(0)));
            return stringBuilder.toString();
        }
    }

    public static String removePrefixFromSetter(Method method) {
        StringBuilder stringBuilder = new StringBuilder(method.getName().replaceFirst("set", ""));
        stringBuilder.setCharAt(0, toLowerCase(stringBuilder.charAt(0)));
        return stringBuilder.toString();
    }
}
