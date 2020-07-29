package sept.major.users.controller;

import sept.major.users.exception.InvalidEntityException;

import java.lang.reflect.Method;
import java.util.Map;

public abstract class ControllerHelper<T> extends ControllerHelperBase {

    public T analysePostInput(Class<T> entityClass, Map<String, Object> responseBody) {
        T entity;
        try {
            entity = entityClass.newInstance();
        } catch (InstantiationException e) {
            throw new InvalidEntityException("Provided entity of class " + entityClass.toString() + " cannot be instantiated");
        } catch (IllegalAccessException e) {
            throw new InvalidEntityException("Provided entity of class " + entityClass.toString() + " does not have accessible constructor");
        }

        Map<String, Method> entryClassMethods = getMethods(entityClass);

        for (Map.Entry<String, Method> entry : entryClassMethods.entrySet()) {
            Object responseBodyValue = responseBody.get(entry.getKey());
            if (responseBodyValue != null) {
                try {
                    entry.getValue().invoke(entity, responseBodyValue);
                } catch (Exception e) {
                    throw new RuntimeException("Received method that is invokable. Only invokable methods should be here");
                }
            }
        }

        return entity;
    }
}
