package sept.major.users.controller;

import sept.major.users.exception.InvalidEntityException;
import sept.major.users.exception.ResponseErrorFoundException;
import sept.major.users.response.error.FieldIncorrectTypeError;
import sept.major.users.response.error.FieldMissingError;
import sept.major.users.response.error.ResponseError;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class ControllerHelper<T> extends ControllerHelperBase {

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

        Map<String, Method> entryClassMethods = getMethods(entityClass);

        for (Map.Entry<String, Method> entry : entryClassMethods.entrySet()) {
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
}
