package sept.major.users.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sept.major.users.entity.AbstractEntity;
import sept.major.users.exception.IdentifierUpdateException;
import sept.major.users.exception.InvalidEntityException;
import sept.major.users.exception.RecordNotFoundException;
import sept.major.users.patch.PatchValue;
import sept.major.users.response.error.FieldIncorrectTypeError;
import sept.major.users.response.error.FieldMissingError;
import sept.major.users.response.error.ResponseError;
import sept.major.users.service.CrudService;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static java.lang.Character.toLowerCase;

public abstract class ControllerHelper<E extends AbstractEntity<ID>, ID> {

    private HashMap<String, Method> setterMethods = new HashMap<>();
    private HashMap<String, Method> getterMethods = new HashMap<>();

    public abstract CrudService<E, ID> getService();


    public ResponseEntity getEntity(ID id) {
        try {
            E entity = getService().read(id);
            return new ResponseEntity(entity, HttpStatus.OK);
        } catch (RecordNotFoundException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity createEntity(Class<E> entityClass, Map<String, Object> responseBody) {

        Set<ResponseError> responseErrors = new HashSet<>();
        E entity;

        try {
            entity = entityClass.newInstance();
        } catch (InstantiationException e) {
            throw new InvalidEntityException("Provided entity of class " + entityClass.toString() + " cannot be instantiated");
        } catch (IllegalAccessException e) {
            throw new InvalidEntityException("Provided entity of class " + entityClass.toString() + " does not have accessible constructor");
        }

        findMethods(entityClass);

        for (Map.Entry<String, Method> entry : setterMethods.entrySet()) {
            Object responseBodyValue = responseBody.get(entry.getKey());
            if (responseBodyValue != null) {
                try {
                    Class expectedType = entry.getValue().getParameterTypes()[0];
                    if (!responseBodyValue.getClass().equals(expectedType)) {
                        responseErrors.add(new FieldIncorrectTypeError(entry.getKey(), expectedType.toString(), responseBodyValue.getClass().toString()));
                    } else {
                        entry.getValue().invoke(entity, responseBodyValue);
                    }

                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new InvalidEntityException("Received method that is invokable. Only invokable methods should be here");
                }
            } else {
                responseErrors.add(new FieldMissingError(entry.getKey()));
            }
        }

        try {
            getService().read(entity.getID());
            return new ResponseEntity("Failed to create entity because an entity with it's identifier already exists", HttpStatus.CONFLICT);
        } catch (RecordNotFoundException e) {
            if (!responseErrors.isEmpty()) {
                return new ResponseEntity(responseErrors, HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity(getService().create(entity), HttpStatus.OK);
        }
    }

    public ResponseEntity updateEntity(Class<E> entityClass, ID id, Map<String, Object> responseBody) {
        // Get fields in response. Fields linked to setter and getter
        Set<ResponseError> responseErrors = new HashSet<>();
        HashMap<String, PatchValue> patchValues = new HashMap<>();

        findMethods(entityClass);

        for (Map.Entry<String, Method> entry : setterMethods.entrySet()) {

            String attributeName = entry.getKey();

            Object responseBodyValue = responseBody.get(attributeName);
            if (responseBodyValue != null) {
                try {
                    Class expectedType = entry.getValue().getParameterTypes()[0];
                    if (!responseBodyValue.getClass().equals(expectedType)) {
                        responseErrors.add(new FieldIncorrectTypeError(attributeName, expectedType.toString(), responseBodyValue.getClass().toString()));
                    } else {
                        patchValues.put(attributeName,
                                new PatchValue(setterMethods.get(attributeName), getterMethods.get(attributeName), responseBodyValue));
                    }
                } catch (Exception e) {
                    throw new InvalidEntityException("Received method that is invokable. Only invokable methods should be here");
                }
            }
        }

        if (!responseErrors.isEmpty()) {
            return new ResponseEntity(responseErrors, HttpStatus.BAD_REQUEST);
        }

        try {
            return new ResponseEntity(getService().patch(id, patchValues), HttpStatus.OK);
        } catch (RecordNotFoundException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (IdentifierUpdateException e) {
            return new ResponseEntity(new ResponseError("Identifier field", "Cannot update field used for identifing entites"), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity deleteEntity(ID id) {
        try {
            getService().delete(id);
            return new ResponseEntity("Record successfully deleted", HttpStatus.OK);
        } catch (RecordNotFoundException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    private void findMethods(Class<E> entityClass) {
        if (setterMethods.size() == 0) {
            setterMethods = new HashMap<>();
            Method[] methods = entityClass.getDeclaredMethods();

            for (Method method : methods) {
                if (isSetter(method)) {
                    setterMethods.put(removePrefixFromMethod(method, "set"), method);
                } else if (isGetter(method)) {
                    getterMethods.put(removePrefixFromMethod(method, "get"), method);
                }
            }
            if (getterMethods.size() == 0 || setterMethods.size() == 0) {
                throw new InvalidEntityException(String.format("Provided entity of class %s has no accessible getter methods", entityClass));
            }
        }

        setterMethods.remove("iD");
        getterMethods.remove("iD");
    }

    private boolean isSetter(Method method) {
        return (!Modifier.isStatic(method.getModifiers())
                && !Modifier.isAbstract(method.getModifiers())
                && method.getReturnType() == Void.TYPE
                && method.getParameterCount() == 1
                && method.getName().startsWith("set")
                && Modifier.isPublic(method.getModifiers()));
    }

    private boolean isGetter(Method method) {
        return (!Modifier.isStatic(method.getModifiers())
                && !Modifier.isAbstract(method.getModifiers())
                && method.getReturnType() != Void.TYPE
                && method.getParameterCount() == 0
                && method.getName().startsWith("get")
                && Modifier.isPublic(method.getModifiers()));
    }

    private String removePrefixFromMethod(Method method, String prefix) {
        StringBuilder stringBuilder = new StringBuilder(method.getName().replaceFirst(prefix, ""));
        stringBuilder.setCharAt(0, toLowerCase(stringBuilder.charAt(0)));
        return stringBuilder.toString();
    }

}
