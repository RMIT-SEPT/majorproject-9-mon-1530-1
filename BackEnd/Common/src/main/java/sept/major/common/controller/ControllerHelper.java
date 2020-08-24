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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static sept.major.common.reflection.ReflectionUtils.*;

/**
 *
 * @author Brodey Yendall
 * @version %I%, %G%
 * @since 1.0.9
 *
 * Used to assist with implementing basic CRUD functionality, both with generic implementations and helper methods to make
 * developers lives easier and controllers shorter with less duplication.
 *
 * @param <E> This is the class of the entity that will be used to transfer data; The entity that will be created, retrieved, updated and deleted.
 * @param <ID> The class of the @Id field within the {@code <E>} parameter.
 */
public abstract class ControllerHelper<E extends AbstractEntity<ID>, ID> {

    /**
     * Used to link field names to the {@link Field} and getter/setter {@link Method} of the field by using {@link FieldReflectionResults}
     */
    private HashMap<String, FieldReflectionResults> valueFieldsReflection = new HashMap<>();

    /**
     * The name of the identifier field in the {@code <E>} entity
     */
    private String identifierFieldName;

    /**
     *
     * Converts provided string to the class provided. Currently converts to any of Integer, Double, LocalDate or LocalDateTime
     *
     * @param classToConvertTo The class to convert to
     * @param toConvert The string to covert
     * @return The string convert to the class provided
     * @throws FailedConversionException Failed to covert the string to the class provided.
     */
    public static Object convertString(Class classToConvertTo, String toConvert) throws FailedConversionException {
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

    /**
     * This class provides generic implementations of CRUD functionality, it does this by calling relevant methods in
     * the service after validating the input. This method is how this class accesses these methods.
     *
     * @return The {@link CrudService} which this class with call methods on when providing generic implementations
     */
    public abstract CrudService<E, ID> getService();

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

    /**
     * The generic implementation of retrieve by id functionality
     *
     * @param identifierString The id value as a {@link String}
     * @param identifierClass The class which the id value should be in
     * @return An API response based on the results of the retrieve by id
     */
    public ResponseEntity getEntity(String identifierString, Class<ID> identifierClass) {
        ID id;
        try {
            id = convertIdentifier(identifierFieldName, identifierClass);
        } catch (FailedConversionException e) {
            String field = (identifierFieldName == null) ? "id": identifierFieldName; // identifierFieldName might not have been found so we just use "id" instead if it isn't
            return new ResponseEntity(new ResponseError(field, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
        try {
            E entity = getService().read(id); // Call the service's implementation of retrieving a record.
            return new ResponseEntity(entity, HttpStatus.OK);
        } catch (RecordNotFoundException e) {
            // No record was found so return a 404 with a message stating a record wasn't found.
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    /**
     *
     * Validates the post body input (provided in the form a map) and returns an entity created from the input.
     * Does not call any {@link #getService()} methods, only validates the input.
     *
     * @param entityClass Class of {@code <E>}. Needed because you cannot get the class of a generic
     * @param responseBody Maps field name to field value. Typically sourced from JSON provided in a request body.
     * @return An entity constructed from the map provided. Fields are assigned the values provided by the map
     * @throws RecordAlreadyExistsException A record already exists with the id provided in the {@code responseBody} parameter
     * @throws ResponseErrorException There was a validation error in entity created from the map provided.
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

    /**
     *
     * Provides generic implementation of create entity functionality.
     * Creates and validates entity constructed from the provided responseBody map and
     * then calls the relevant method in the {@link #getService() service}.
     *
     * @param entityClass Class of {@code <E>}. Needed because you cannot get the class of a generic
     * @param responseBody Maps field name to field value. Typically sourced from JSON provided in a request body.
     * @return An API response based on the results of the create entity
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

    /**
     *
     * Validates values provided by the {@code responseBody} against requirements of the entity {@code <E>}
     *
     * @param entityClass Class of {@code <E>}. Needed because you cannot get the class of a generic
     * @param responseBody Maps field name to field value for fields to update. Typically sourced from JSON provided in a request body.
     * @return A list of values to update along with the {@link Field} to assign the value to and the getter/setter {@link Method} for the field
     * @throws ResponseErrorException There was a validation error in one of the values provided
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

    /**
     *
     * The generic implementation of update entity functionality.
     * Validates the values provided and if correct, changes the values of the existing entity to the values provided.
     *
     * @param entityClass Class of {@code <E>}. Needed because you cannot get the class of a generic
     * @param identifierString The id value of the entity to be updated as a {@link String}
     * @param identifierClass The class which the id value should be in
     * @param responseBody Maps field name to field value for fields to update. Typically sourced from JSON provided in a request body.
     * @return An api response based on the success of the update
     */
    public ResponseEntity validateInputAndPatch(Class<E> entityClass, String identifierString, Class<ID> identifierClass, Map<String, String> responseBody) {
        ID id = null;
        try {
            id = convertIdentifier(identifierString, identifierClass);
        } catch (FailedConversionException e) {
            return new ResponseEntity(new ResponseError(identifierFieldName, e.getMessage()), HttpStatus.BAD_REQUEST);
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

    /**
     *
     * The generic implementation of delete entity functionality. Will delete the entity with the provided id
     *
     * @param identifierString The id value of the entity to be deleted as a {@link String}
     * @param identifierClass The class which the id value should be in
     * @return An api response based on the success of the deletion
     */
    public ResponseEntity deleteEntity(String identifierString, Class<ID> identifierClass) {
        ID id = null;
        try {
            id = convertIdentifier(identifierString, identifierClass);
        } catch (FailedConversionException e) {
            String field = (identifierFieldName == null) ? "id": identifierFieldName; // identifierFieldName might not have been found so we just use "id" instead if it isn't
            return new ResponseEntity(new ResponseError(field, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
        try {
            getService().delete(id);
            return new ResponseEntity("Record successfully deleted", HttpStatus.OK);
        } catch (RecordNotFoundException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    /**
     *
     * Gets the {@link Field} and getter/setter {@link Method} for every field in the entity {@code <E>}.
     * Stores results in {@link #valueFieldsReflection}, also finds and assigns {@link #identifierFieldName}
     *
     * @param entityClass Class of {@code <E>}. Needed because you cannot get the class of a generic
     */
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

    /**
     *
     * Gets the key for the field which when used in a value map such as the response body in {@link #validatePatchInput(Class, Map)} and {@link #validatePatchInput(Class, Map)}
     * will get the value for the field provided.
     *
     * @param field The field to get the key for
     * @return The key for the field
     */
    private String getKeyForField(Field field) {
        JsonProperty jsonProperty = field.getAnnotation(JsonProperty.class);
        if (jsonProperty == null) {
            return field.getName();
        } else {
            return jsonProperty.value();
        }
    }

    /**
     *
     * Converts the identifier in {@link String} form to {@code <ID>} class
     *
     * @param identifierString The identifier to convert
     * @param identifierType The class to convert to. Needed because we cannot get the class of a generic
     * @return The identifier after being converted
     * @throws FailedConversionException Failed to convert the identifier to the {@code <ID>} class
     */
    private ID convertIdentifier(String identifierString, Class<ID> identifierType) throws FailedConversionException {
        if(identifierType.equals(String.class)) {
            return (ID)identifierString;
        } else {
            try {
                return (ID) convertString(identifierType, identifierString);
            } catch (FailedConversionException e) {
                throw new FailedConversionException(String.format("Conversion to %s has not been implemented", identifierType));
            }
        }
    }
}
