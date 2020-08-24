package sept.major.common.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sept.major.common.annotation.ReadOnly;
import sept.major.common.entity.AbstractEntity;
import sept.major.common.exception.*;
import sept.major.common.patch.PatchValue;
import sept.major.common.response.ResponseError;
import sept.major.common.service.CrudService;

import javax.persistence.Id;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.*;

import static sept.major.common.reflection.ReflectionUtils.*;

public abstract class ControllerHelper<E extends AbstractEntity<ID>, ID> {

    private HashMap<String, FieldReflectionResults> valueFieldsReflection = new HashMap<>();
    private String identifierFieldName;

    /*
        Method for retrieving the linked CrudService in a generic fashion.
        Implemented of this class is expected to also implement their CrudService and as such they may specify it here.
      */
    public abstract CrudService<E, ID> getService();

    /*
        Code for a generic get entity by identifier endpoint.
        Provide value of the identifying field and a API response will be provided.
     */
    public ResponseEntity getEntity(String identifierString, Class<ID> identifierClass) {
        ID id;
        try {
            id = convertIdentifier(identifierFieldName, identifierClass);
        } catch (ResponseErrorException e) {
            return new ResponseEntity(e.getResponseErrors(), HttpStatus.BAD_REQUEST);
        }
        try {
            E entity = getService().read(id); // Call the service's implementation of retrieving a record.
            return new ResponseEntity(entity, HttpStatus.OK);
        } catch (RecordNotFoundException e) {
            // No record was found so return a 404 with a message stating a record wasn't found.
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    /*
        Used for validating input data for used for creating an entity.
        Values provided:
            * Class of the entity this class implements. Done because you cannot get the class of a generic
            * A map which links field name to field value. Normally translated from JSON
         Returns: An entity constructed from the map provided
         Throws:
            * RecordAlreadyExistsException: Record already exists with the same identifying field provided in the map
            * ResponseErrorException: A field is missing or an incorrect type.
     */
    public E validatePostInput(Class<E> entityClass, Map<String, String> responseBody) throws RecordAlreadyExistsException, ResponseErrorException {
         /*
            Used to keep track of errors in the provided map.
            These are stored in a set instead of instantly thrown for user convenience.
         */
        List<ResponseError> responseErrors = new ArrayList<>();

        E entity;
        try {
            // Creating an instance of the entity which this class implements, will cause errors if the classes does not an accessible empty constructor
            entity = entityClass.newInstance();
        } catch (InstantiationException e) {
            throw new InvalidEntityException("Provided entity of class " + entityClass.toString() + " cannot be instantiated");
        } catch (IllegalAccessException e) {
            throw new InvalidEntityException("Provided entity of class " + entityClass.toString() + " does not have accessible constructor");
        }

        getReflectionOfEntity(entityClass);

        for(Map.Entry<String, FieldReflectionResults> fieldReflectionResultsEntry: valueFieldsReflection.entrySet()) {
            FieldReflectionResults fieldReflectionResult = fieldReflectionResultsEntry.getValue();

            Field field = fieldReflectionResult.getField();
            String fieldKey = getKeyForField(field);
            if(field.getAnnotation(ReadOnly.class) != null) {
                if(responseBody.get(fieldKey) != null) {
                    responseErrors.add(new ResponseError(fieldKey, "value cannot be set because field is read only"));
                    continue;
                }
            }

            Object value = responseBody.get(fieldKey);
            if(value != null) {
                Class<?> fieldType = field.getType();
                if(!fieldType.equals(String.class)) {
                    try {
                        value = convertString(fieldType, responseBody.get(fieldKey));
                    } catch (FailedConversionException e) {
                        responseErrors.add(new ResponseError(fieldKey, e.getMessage()));
                    }
                }

                Method setterMethod = fieldReflectionResult.getSetterMethod();
                if(setterMethod == null) {
                    throw new InvalidEntityException("no setter found for field " + fieldKey);
                } else {
                    try {
                        setterMethod.invoke(entity, value);
                    } catch (IllegalAccessException | InvocationTargetException e ) {
                        throw new InvalidEntityException("no setter found for field " + fieldKey);
                    }
                }
            }

        }

        if (!responseErrors.isEmpty()) {
            throw new ResponseErrorException(responseErrors);
        }
        return entity;
    }


    /*
        A convenience method which conducts a generic POST.
        Calls validatePostInput() to validate the input and calls the generic create method in the provided service.
        Handles all exceptions and returns an appropriate API response
     */
    public ResponseEntity validateInputAndPost(Class<E> entityClass, Map<String, String> responseBody) {
        try {
            E entity = validatePostInput(entityClass, responseBody);

            // Call the service's implementation of creating the entity and return it.
            E createdEntity = getService().create(entity);
            return new ResponseEntity(createdEntity, HttpStatus.OK);

        } catch (RecordAlreadyExistsException e) {
            return new ResponseEntity("Failed to create entity because an entity with it's identifier already exists", HttpStatus.CONFLICT);
        } catch (ResponseErrorException e) {
            return new ResponseEntity(e.getResponseErrors(), HttpStatus.BAD_REQUEST);
        }
    }

    /*
        Used for validating input data for used for updating an entity.
        Values provided:
            * Class of the entity this class implements. Done because you cannot get the class of a generic
            * The value of the identifying field for the entity being updated
            * A map which links field name to field value. Normally translated from JSON
         Returns: A map which links field name to field value, the setter for the field and the getter for the field.
         Throws:
            * ResponseErrorException: A field is an incorrect type.
     */
    public List<PatchValue> validatePatchInput(Class<E> entityClass, Map<String, String> responseBody) throws ResponseErrorException {
         /*
            Used to keep track of errors in the provided map.
            These are stored in a set instead of instantly thrown for user convenience.
         */
        List<ResponseError> responseErrors = new ArrayList<>();

        /*
            Maps field name to the
                * The value for the field as provided by the PATCH map
                * The getter for the field
                * The setter for the field
         */
        List<PatchValue> patchValues = new ArrayList<>();

        getReflectionOfEntity(entityClass);

        for(Map.Entry<String, FieldReflectionResults> fieldReflectionResultsEntry: valueFieldsReflection.entrySet()) {
            FieldReflectionResults fieldReflectionResult = fieldReflectionResultsEntry.getValue();

            Field field = fieldReflectionResult.getField();
            String fieldKey = getKeyForField(field);
            if(field.getAnnotation(ReadOnly.class) != null) {
                if(responseBody.get(fieldKey) != null) {
                    responseErrors.add(new ResponseError(fieldKey, "value cannot be updated because field is read only"));
                    continue;
                }
            }

            Object value = responseBody.get(fieldKey);
            if(value != null) {
                Class<?> fieldType = field.getType();
                if(!fieldType.equals(String.class)) {
                    try {
                        value = convertString(fieldType, responseBody.get(fieldKey));
                    } catch (FailedConversionException e) {
                        responseErrors.add(new ResponseError(fieldKey, e.getMessage()));
                    }
                }


                Method setterMethod = fieldReflectionResult.getSetterMethod();
                if(setterMethod == null) {
                    throw new InvalidEntityException("no setter found for field " + fieldKey);
                } else {
                    Method getterMethod = fieldReflectionResult.getGetterMethod();
                    if(getterMethod == null) {
                        throw new InvalidEntityException("no getter found for field " + fieldKey);
                    } else {
                        patchValues.add(new PatchValue(field, setterMethod, getterMethod, value));
                    }
                }
            }

        }


        if (!responseErrors.isEmpty()) {
            throw new ResponseErrorException(responseErrors);
        }

        return patchValues;
    }


    /*
         A convenience method which conducts a generic PATCH.
         Calls validatePatchInput() to validate the input and calls the generic patch method in the provided service.
         Handles all exceptions and returns an appropriate API response
      */
    public ResponseEntity validateInputAndPatch(Class<E> entityClass, String identifierString, Class<ID> identifierType, Map<String, String> responseBody) {
        ID id = null;
        try {
            id = convertIdentifier(identifierString, identifierType);
        } catch (ResponseErrorException e) {
            return new ResponseEntity(e.getResponseErrors(), HttpStatus.BAD_REQUEST);
        }

        try {
            List<PatchValue> patchValues = validatePatchInput(entityClass, responseBody);

            // Calls the service's implementation of the PATCH endpoint
            E patchedEntity = getService().patch(id, patchValues);
            return new ResponseEntity(patchedEntity, HttpStatus.OK);

        } catch (ResponseErrorException e) {
            return new ResponseEntity(e.getResponseErrors(), HttpStatus.BAD_REQUEST);
        } catch (RecordNotFoundException e) {
            return new ResponseEntity(new ResponseError("Identifier field", e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (IdentifierUpdateException e) {
            return new ResponseEntity(new ResponseError("Identifier field", "Cannot update field used for identifying entities"), HttpStatus.BAD_REQUEST);
        }

    }

    /*
        Code for a generic delete entity by identifier endpoint.
        Provide value of the identifying field and a API response will be provided.
     */
    public ResponseEntity deleteEntity(String identifierString, Class<ID> identifierType) {
        ID id = null;
        try {
            id = convertIdentifier(identifierString, identifierType);
        } catch (ResponseErrorException e) {
            return new ResponseEntity(e.getResponseErrors(), HttpStatus.BAD_REQUEST);
        }
        try {
            getService().delete(id);
            return new ResponseEntity("Record successfully deleted", HttpStatus.OK);
        } catch (RecordNotFoundException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


    private void getReflectionOfEntity(Class<E> entityClass) {
        /*
            Firstly we check if methods have already been found, this is done because retrieving methods with reflect is
            expensive and should be done as little as possible.
         */
        if (identifierFieldName == null) {
            HashMap<String, Method> setterMethods = new HashMap<>();
            HashMap<String, Method> getterMethods = new HashMap<>();
            Method[] methods = entityClass.getMethods(); // Get the methods for the entity with reflection

            for (Method method : methods) {
                if (isSetter(method)) {
                    setterMethods.put(removePrefixFromSetter(method), method);
                } else if (isGetter(method)) {
                    getterMethods.put(removePrefixFromGetter(method), method);
                }
            }

            for (Field field : entityClass.getDeclaredFields()) {
                String fieldName = field.getName();
                if (field.getAnnotation(Id.class) != null) {
                    if (identifierFieldName == null) {
                        identifierFieldName = fieldName;
                    } else {
                        throw new MultipleIdentifiersException(entityClass);
                    }
                }
                if(setterMethods.get(fieldName) != null && getterMethods.get(fieldName) != null) {
                    valueFieldsReflection.put(fieldName, new FieldReflectionResults(field, setterMethods.get(fieldName), getterMethods.get(fieldName)));
                }
            }


        }
    }

    private String getKeyForField(Field field) {
        JsonProperty jsonProperty = field.getAnnotation(JsonProperty.class);
        if (jsonProperty == null) {
            return field.getName();
        } else {
            return jsonProperty.value();
        }
    }

    private Object convertString(Class classToConvertTo, String toConvert) throws FailedConversionException {
        if(classToConvertTo.equals(Integer.class)) {
            try {
                return new Integer(toConvert);
            } catch (NumberFormatException e) {
                throw new FailedConversionException("must be an integer (whole number)");
            }
        } else if(classToConvertTo.equals(Double.class)) {
            try {
                return new Double(toConvert);
            } catch (NumberFormatException e) {
                throw new FailedConversionException("must be numeric");
            }
        } else if(classToConvertTo.equals(LocalDate.class)) {
            try {
                return LocalDate.parse(toConvert);
            } catch (DateTimeParseException e) {
                throw new FailedConversionException("must be formatted yyyy/mm/dd");
            }
        } else if(classToConvertTo.equals(LocalDateTime.class)) {
            try {
                return LocalDateTime.parse(toConvert);
            } catch (DateTimeParseException e) {
                throw new FailedConversionException("must be formatted yyyy/MM/ddTHH:mm:ss.SSSSSSSSS");
            }
        }
        return new FailedConversionException(String.format("Conversion to %s has not been implemented", classToConvertTo));
    }

    private ID convertIdentifier(String identifierString, Class<ID> identifierType) throws ResponseErrorException {
        if(identifierType.equals(String.class)) {
            return (ID)identifierString;
        } else {
            try {
                return (ID) convertString(identifierType, identifierString);
            } catch (FailedConversionException e) {
                throw new ResponseErrorException((Arrays.asList(new ResponseError("id", e.getMessage()))));
            }
        }
    }
}
