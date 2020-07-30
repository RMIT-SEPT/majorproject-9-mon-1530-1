package sept.major.users.controller;

import sept.major.users.exception.InvalidEntityException;
import sept.major.users.exception.ResponseErrorFoundException;
import sept.major.users.response.error.FieldIncorrectTypeError;
import sept.major.users.response.error.FieldMissingError;
import sept.major.users.response.error.ResponseError;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static java.lang.Character.toLowerCase;

public abstract class ControllerHelper<T> {

    private HashMap<String, Method> entityMethods = new HashMap<>();

    public T analysePostInput(Class<T> entityClass, Map<String, Object> responseBody) throws ResponseErrorFoundException {
        Set<ResponseError> responseErrors = new HashSet<>();
        T entity;

        try {
            entity = entityClass.newInstance();
        } catch (InstantiationException e) {
            throw new InvalidEntityException("Provided entity of class " + entityClass.toString() + " cannot be instantiated");
        } catch (IllegalAccessException e) {
            throw new InvalidEntityException("Provided entity of class " + entityClass.toString() + " does not have accessible constructor");
        }

        updateMethods(entityClass);

        for (Map.Entry<String, Method> entry : entityMethods.entrySet()) {
            Object responseBodyValue = responseBody.get(entry.getKey());
            if (responseBodyValue != null) {
                try {
                    Class expectedType = entry.getValue().getParameterTypes()[0];
                    if (!responseBodyValue.getClass().equals(expectedType)) {
                        responseErrors.add(new FieldIncorrectTypeError(entry.getKey(), expectedType.toString(), responseBodyValue.getClass().toString()));
                    } else {
                        entry.getValue().invoke(entity, responseBodyValue);
                    }

                } catch (Exception e) {
                    throw new RuntimeException("Received method that is invokable. Only invokable methods should be here");
                }
            } else {
                responseErrors.add(new FieldMissingError(entry.getKey()));
            }
        }

        if (!responseErrors.isEmpty()) {
            throw new ResponseErrorFoundException(responseErrors);
        }

        return entity;
    }

    private void updateMethods(Class<T> entityClass) {
        if (entityMethods == null) {
            entityMethods = new HashMap<>();
            Method[] methods = entityClass.getDeclaredMethods();

            for (Method method : methods) {
                if (isSetter(method)) {
                    entityMethods.put(getAttributeFromSetter(method), method);
                }
            }

        }
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
