package sept.major.common.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;
import sept.major.common.entity.AbstractEntity;
import sept.major.common.exception.*;
import sept.major.common.patch.PatchValue;
import sept.major.common.response.FieldIncorrectTypeError;
import sept.major.common.response.FieldMissingError;
import sept.major.common.response.ResponseError;
import sept.major.common.service.CrudService;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static sept.major.common.reflection.ReflectionUtils.*;

public abstract class ControllerHelper<E extends AbstractEntity<ID>, ID> {

    private HashMap<String, Method> setterMethods = new HashMap<>(); // All the setter methods for the entity this class implements
    private HashMap<String, Method> getterMethods = new HashMap<>(); // All the getter methods for the entity this class implements

    /*
        Method for retrieving the linked CrudService in a generic fashion.
        Implemented of this class is expected to also implement their CrudService and as such they may specify it here.
      */
    public abstract CrudService<E, ID> getService();

    /*
        Code for a generic get entity by identifier endpoint.
        Provide value of the identifying field and a API response will be provided.
     */
    public ResponseEntity getEntity(ID id) {
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
    public E validatePostInput(Class<E> entityClass, Map<String, Object> responseBody) throws RecordAlreadyExistsException, ReponseErrorException {
         /*
            Used to keep track of errors in the provided map.
            These are stored in a set instead of instantly thrown for user convenience.
         */
        Set<ResponseError> responseErrors = new HashSet<>();

        E entity;
        try {
            // Creating an instance of the entity which this class implements, will cause errors if the classes does not an accessible empty constructor
            entity = entityClass.newInstance();
        } catch (InstantiationException e) {
            throw new InvalidEntityException("Provided entity of class " + entityClass.toString() + " cannot be instantiated");
        } catch (IllegalAccessException e) {
            throw new InvalidEntityException("Provided entity of class " + entityClass.toString() + " does not have accessible constructor");
        }

        findMethods(entityClass); // Gets the getters and setters of the entity this class implements through the use of Reflection

        /*
            Increments through the setter methods of the entity this class implements.
            For each setter we see if the provided map has a vale for it, if so check that types match, if they don't then add an error to the list.
            Every valid value provided by the map is set in the entity created above.
         */
        for (Map.Entry<String, Method> entry : setterMethods.entrySet()) {
            Object responseBodyValue = responseBody.get(entry.getKey()); // The value in the map for the field this setter represents.
            if (responseBodyValue != null) {
                try {
                    // Get the expected type of the field but getting the type of the parameter of the setter. (Setters only have one parameter)
                    Class expectedType = entry.getValue().getParameterTypes()[0];
                    if (!responseBodyValue.getClass().equals(expectedType)) {
                        responseErrors.add(new FieldIncorrectTypeError(entry.getKey(), expectedType.toString(), responseBodyValue.getClass().toString()));
                    } else {
                        // Set the value in the entity but involving the setter method on our created entity.
                        entry.getValue().invoke(entity, responseBodyValue);
                    }

                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new InvalidEntityException("Received method that is invokable. Only invokable methods should be here");
                }
            } else {
                responseErrors.add(new FieldMissingError(entry.getKey())); // TODO Remove this and replace with validation annotations. Keep in mind the identifier field
            }
        }

        try {
            /*
                Checks if a record with provided id already exists.
                This check is done at the end because we do not know what value in the provided map is the identifier field,
                we only know what the identifier field is once the entity has been fully constructed.
             */
            getService().read(entity.getID());
            throw new RecordAlreadyExistsException();
        } catch (RecordNotFoundException e) {
            if (!responseErrors.isEmpty()) {
                throw new ReponseErrorException(responseErrors);
            }
            return entity;
        }
    }


    /*
        A convenience method which conducts a generic POST.
        Calls validatePostInput() to validate the input and calls the generic create method in the provided service.
        Handles all exceptions and returns an appropriate API response
     */
    public ResponseEntity validateInputAndPost(Class<E> entityClass, Map<String, Object> responseBody) {
        try {
            E entity = validatePostInput(entityClass, responseBody);

            // Call the service's implementation of creating the entity and return it.
            E createdEntity = getService().create(entity);
            return new ResponseEntity(createdEntity, HttpStatus.OK);

        } catch (RecordAlreadyExistsException e) {
            return new ResponseEntity("Failed to create entity because an entity with it's identifier already exists", HttpStatus.CONFLICT);
        } catch (ReponseErrorException e) {
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
   public HashMap<String, PatchValue> validatePatchInput(Class<E> entityClass, ID id, Map<String, Object> responseBody) throws ReponseErrorException {
         /*
            Used to keep track of errors in the provided map.
            These are stored in a set instead of instantly thrown for user convenience.
         */
       Set<ResponseError> responseErrors = new HashSet<>();

        /*
            Maps field name to the
                * The value for the field as provided by the PATCH map
                * The getter for the field
                * The setter for the field
         */
       HashMap<String, PatchValue> patchValues = new HashMap<>();

       findMethods(entityClass); // Gets the getters and setters of the entity this class implements through the use of Reflection

       for (Map.Entry<String, Method> entry : setterMethods.entrySet()) {

           String attributeName = entry.getKey(); // Get the field name based on the setters/getters of the entity this class implements

           Object responseBodyValue = responseBody.get(attributeName); // The value for the field
           if (responseBodyValue != null) { // There was value provided for the field
               try {
                   // Get the expected type of the field but getting the type of the parameter of the setter. (Setters only have one parameter)
                   Class expectedType = entry.getValue().getParameterTypes()[0];
                   if (!responseBodyValue.getClass().equals(expectedType)) {
                       responseErrors.add(new FieldIncorrectTypeError(attributeName, expectedType.toString(), responseBodyValue.getClass().toString()));
                   } else {
                       // Add a mapping for the field name to it's value, getter and setter.
                       patchValues.put(attributeName,
                               new PatchValue(setterMethods.get(attributeName), getterMethods.get(attributeName), responseBodyValue));
                   }
               } catch (Exception e) {
                   // This shouldn't happen because only accessible getters and setters are used.
                   throw new InvalidEntityException("Received method that is invokable. Only invokable methods should be here");
               }
           }
       }

       if (!responseErrors.isEmpty()) {
           throw new ReponseErrorException(responseErrors);
       }

       return patchValues;
   }


    /*
         A convenience method which conducts a generic PATCH.
         Calls validatePatchInput() to validate the input and calls the generic patch method in the provided service.
         Handles all exceptions and returns an appropriate API response
      */
    public ResponseEntity validateInputAndPatch(Class<E> entityClass, ID id, Map<String, Object> responseBody) {

        try {
            HashMap<String, PatchValue> patchValues = validatePatchInput(entityClass, id, responseBody);

            // Calls the service's implementation of the PATCH endpoint
            E patchedEntity = getService().patch(id, patchValues);
            return new ResponseEntity(patchedEntity, HttpStatus.OK);

        } catch (ReponseErrorException e) {
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
    public ResponseEntity deleteEntity(ID id) {
        try {
            getService().delete(id);
            return new ResponseEntity("Record successfully deleted", HttpStatus.OK);
        } catch (RecordNotFoundException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    // Finds the getters and setters for classes of the entity which this class implements.
    private void findMethods(Class<E> entityClass) {
        /*
            Firstly we check if methods have already been found, this is done because retrieving methods with reflect is
            expensive and should be done as little as possible.
         */
        if (setterMethods.size() == 0) {
            setterMethods = new HashMap<>();
            Method[] methods = entityClass.getDeclaredMethods(); // Get the methods for the entity with reflection

            for (Method method : methods) {
                if (isSetter(method)) {
                    setterMethods.put(removePrefixFromMethod(method, "set"), method);
                } else if (isGetter(method)) {
                    getterMethods.put(removePrefixFromMethod(method, "get"), method);
                }
            }
            if (getterMethods.size() == 0 || setterMethods.size() == 0) {
                throw new InvalidEntityException(String.format("Provided entity of class %s has no accessible getter/setter methods", entityClass));
            }
        }

        // getId() method as provided by AbstractEntity interface should be ignored.
        getterMethods.remove("iD");
    }
}
