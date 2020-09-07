package sept.major.common.service;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.jpa.repository.JpaRepository;
import sept.major.common.entity.AbstractEntity;
import sept.major.common.exception.*;
import sept.major.common.patch.PatchValue;
import sept.major.common.response.ValidationError;

import javax.persistence.Id;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Brodey Yendall
 * @version %I%, %G%
 * @since 1.0.9
 *
 * Implements basic CRUD functionality. Used by ControllerHelper for basic CRUD functionality implementation.
 *
 * @param <E> This is the class of the entity that will be used to transfer data; The entity that will be created, retrieved, updated and deleted.
 * @param <ID> The class of the @Id field within the {@code <E>} parameter.
 */
public abstract class CrudService<E extends AbstractEntity<ID>, ID> {

    private ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private Validator validator = factory.getValidator();

    /**
     * The method this class uses to access the repository for it's CRUD functionality
     *
     * @return The repository this class with use for CRUD functionality
     */
    protected abstract JpaRepository<E, ID> getRepository();

    /**
     *
     * Performs basic retrieve entity from database functionality
     *
     * @param id The identifying value for the entity to retrieve
     * @return The entity that was retrieved
     * @throws RecordNotFoundException No entity with the provided identifying value could be found
     */
    public E read(ID id) throws RecordNotFoundException {
        Optional<E> entity = getRepository().findById(id); // Get the entity from the repository
        if (entity.isPresent()) {
            return entity.get();
        } else {
            throw new RecordNotFoundException(String.format("No record with a identifier of %s was found", id));
        }
    }

    /**
     * Validates provided entity and then creates the entity in the database
     *
     * @param entity The entity to be created in the database
     * @return The entity after being created. Includes values given when the entity is created.
     * @throws ValidationErrorException The provided entity had validation errors
     * @throws RecordAlreadyExistsException A record with the same identifying value as the entity provided already exists.
     */
    public E create(E entity) throws ValidationErrorException, RecordAlreadyExistsException {
        if(entity.getID() != null) {
            try {
                read(entity.getID());
            } catch (RecordNotFoundException e) {
                return saveEntity(entity);
            }
        } else {
            return saveEntity(entity);
        }

        throw new RecordAlreadyExistsException();
    }

    /**
     *
     * Updates the entity represented by the provided identifier value with the values provided
     *
     * @param id The identifying value of the entity to update
     * @param patchValues The values to update fields with along with the field to update.
     * @return The entity after being updated.
     * @throws RecordNotFoundException No entity with the provided identifying value could be found
     * @throws IdentifierUpdateException There was an attempt to update the identifying field the entity.
     * @throws ValidationErrorException There were validation errors in the entity after being updated
     */
    public E patch(ID id, List<PatchValue> patchValues) throws RecordNotFoundException, IdentifierUpdateException, ValidationErrorException, RecordAlreadyExistsException {

        // Gets the existing record by calling the generic read method implemented in this class. Returns RecordNotFoundException if record doesn't exist.
        E existingEntity = read(id);

        for (PatchValue patchValue : patchValues) {
            /*
                The id field cannot be updated, to prevent this we check if the setter is used for setting the id.
                It is assumed that the developer put a @Id annotation on the setter, otherwise the code will fail.
             */
            if (patchValue.getField().getAnnotation(Id.class) == null) {
                try {
                    /*
                        Get the value for the field from the existing entity but involving the relevent getter on existing entity
                     */
                    Object existingValue = patchValue.getGetter().invoke(existingEntity);
                    if (existingValue == null) {
                        if (patchValue.getValue() != null) {

                            patchValue.getSetter().invoke(existingEntity, patchValue.getValue());
                        }
                    } else if (!existingValue.equals(patchValue.getValue())) {
                        // Update the existing entity with the new value by involving the relevant setter on the entity.
                        patchValue.getSetter().invoke(existingEntity, patchValue.getValue());
                    }
                } catch (IllegalAccessException | InvocationTargetException e) {
                    /*
                        Both of these exceptions shouldn't occur unless there was a poor code change.
                        Only accessible getters and setters should reach this point
                     */
                    throw new InvalidEntityException("Received method that is invokable. Only invokable methods should be here");
                }
            } else {
                throw new IdentifierUpdateException();
            }
        }

        return saveEntity(existingEntity);
    }

    /**
     * @param entity The entity to validate and save
     * @return The entity after being validated and saved
     * @throws ValidationErrorException     There were validation errors in the entity after being updated
     * @throws RecordAlreadyExistsException Entity provided conflicts with an existing one. Generic implementation does not use this but implementations might
     * @since 1.1.1
     * Validates the provided entity and then saves it to the repository ({@link #getRepository()}
     */
    protected E saveEntity(E entity) throws ValidationErrorException, RecordAlreadyExistsException {
        validateEntity(entity);
        return getRepository().save(entity);
    }

    /**
     * Delete the entity from the database which has the provided identifying field
     *
     * @param id The identifying value of the entity to delete
     * @throws RecordNotFoundException There was no record with the provided identifying field
     */
    /*
        Generic implementation of delete record by id
     */
    public void delete(ID id) throws RecordNotFoundException {
        try {
            getRepository().deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new RecordNotFoundException(String.format("No record with a identifier of %s was found", id));
        }
    }

    /**
     *
     * Uses javax validation (bean validation) to validate the provided entity.
     *
     * @param entity The entity to validate
     * @throws ValidationErrorException There were validation errors in the provided entity
     */
    protected void validateEntity(E entity) throws ValidationErrorException {
        /*
            Validates the provided entity with JSR 380/Bean Validation 2.0
            Allows developers to annotate fields with validation such as NotBlank for convenient field validation.
         */
        Set<ConstraintViolation<E>> violations = validator.validate(entity); // Validate the entity and record violations

        /*
            Go through all the violations and translate the violations into ResponseErrors.
            Done so that the API returns the standard error response, allowing for automatic analysis and higher human readability.
         */
        List<ValidationError> validationErrors = violations.stream()
                .map(violation -> new ValidationError(violation.getPropertyPath().toString(), violation.getMessage()))
                .collect(Collectors.toList());

        if (!validationErrors.isEmpty()) {
            throw new ValidationErrorException(validationErrors);
        }
    }
}
