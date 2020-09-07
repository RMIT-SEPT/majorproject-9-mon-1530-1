package sept.major.common.reflection;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import static java.lang.Character.toLowerCase;

/**
 * @author Brodey Yendall
 * @version %I%, %G%
 * @since 1.0.9
 * <p>
 * A class consisting of various methods intended to help using Reflection.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE) // Private constructor so that class cannot be initialized or extended
public class ReflectionUtils {
    /**
     *
     * Evaluates if the provided method is a setter by checking if it contains properties typical for a setter method.
     * These: Not abstract, void return type, one parameter, name prefixed with "set" and is public
     *
     * @param method The method to be checked if it is a setter
     * @return true when the provided method was a setter
     */
    public static boolean isSetter(Method method) {
        return (!Modifier.isStatic(method.getModifiers())
                && !Modifier.isAbstract(method.getModifiers())
                && method.getReturnType() == Void.TYPE
                && method.getParameterCount() == 1
                && method.getName().startsWith("set")
                && Modifier.isPublic(method.getModifiers()));
    }

    /**
     *
     * Evaluates if the provided method is a getter by checking if it contains properties typical for a getter method.
     * These: Not abstract, return type isn't void, has no parameters, name prefixed with either "get" or "is" and is public
     *
     * @param method The method to be checked if it is a getter
     * @return true when the provided method was a getter
     */
    public static boolean isGetter(Method method) {
        return (!Modifier.isStatic(method.getModifiers())
                && !Modifier.isAbstract(method.getModifiers())
                && method.getReturnType() != Void.TYPE
                && method.getParameterCount() == 0
                && (method.getName().startsWith("get") || method.getName().startsWith("is"))
                && Modifier.isPublic(method.getModifiers()));
    }

    /**
     *
     * Removes the prefix from a getter method to retrieve the name of the field it gets the value of.
     *
     * @param method The getter method to remove the prefix from
     * @return The getter method without the prefix
     */
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

    /**
     *
     * Removes the prefix from a setter method to retrieve the name of the field it sets a value to
     *
     * @param method The setter method to remove the prefix from
     * @return The setter method without the prefix
     */
    public static String removePrefixFromSetter(Method method) {
        StringBuilder stringBuilder = new StringBuilder(method.getName().replaceFirst("set", ""));
        stringBuilder.setCharAt(0, toLowerCase(stringBuilder.charAt(0)));
        return stringBuilder.toString();
    }
}
