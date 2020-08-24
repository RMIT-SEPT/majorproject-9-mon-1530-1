package sept.major.common.service;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.jpa.repository.JpaRepository;
import sept.major.common.entity.AbstractEntity;
import sept.major.common.exception.*;
import sept.major.common.patch.PatchValue;
import sept.major.common.response.ResponseError;

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

/*
    A abstract implementation of generic service functionality.
    This service will satisfy the requirements of a simple CRUD service.
 */
public abstract class CrudService<E extends AbstractEntity<ID>, ID> {

    private ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private Validator validator = factory.getValidator();

    /*
        Method for retrieving the linked repository in a generic fashion.
        A basic CRUD service requires a repository to write to, this provides it.
      */
    protected abstract JpaRepository<E, ID> getRepository();

    /*
        Generic get entity by id functionality
     */
    public E read(ID id) throws RecordNotFoundException {
        Optional<E> entity = getRepository().findById(id); // Get the entity from the repository
        if (entity.isPresent()) {
            return entity.get();
        } else {
            throw new RecordNotFoundException(String.format("No record with a identifier of %s was found", id));
        }
    }

    /*
        Generic create entity functionality
     */
    public E create(E entity) throws ResponseErrorException, RecordAlreadyExistsException {
        validateEntity(entity); // Validate entity and return responseErrorException
        if(entity.getID() != null) {
            try {
                read(entity.getID());
            } catch (RecordNotFoundException e) {
                return getRepository().save(entity);
            }
        } else {
            return getRepository().save(entity);
        }

        throw new RecordAlreadyExistsException();
    }

    /*
        Generic update record with given id functionality.
        Values provided:
            * The id of the entity which will be updated
            * A map that links field name to field value, field getter and field setter.
        Returns: The entity after being updated
        Throws:
            * RecordNotFoundException: No record with the given id exists. Cannot update a record if there is no record.
            * IdentifierUpdateException: Occurs when the user attempts to update the identifying field. This would break database logic.
     */
    public E patch(ID id, List<PatchValue> patchValues) throws RecordNotFoundException, IdentifierUpdateException, ResponseErrorException {

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

        validateEntity(existingEntity);

        // Use the generic create entity logic implemented in this class. Done so there is no repeated logic.
        return getRepository().save(existingEntity);
    }

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

    private void validateEntity(E entity) throws ResponseErrorException {
        /*
            Validates the provided entity with JSR 380/Bean Validation 2.0
            Allows developers to annotate fields with validation such as NotBlank for convenient field validation.
         */
        Set<ConstraintViolation<E>> violations = validator.validate(entity); // Validate the entity and record violations

        /*
            Go through all the violations and translate the violations into ResponseErrors.
            Done so that the API returns the standard error response, allowing for automatic analysis and higher human readability.
         */
        Set<ResponseError> responseErrors = violations.stream()
                .map(violation -> new ResponseError(violation.getPropertyPath().toString(), violation.getMessage()))
                .collect(Collectors.toSet());

        if(!responseErrors.isEmpty()) {
            throw new ResponseErrorException(responseErrors);
        }
    }
}
