package sept.major.common.service;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.jpa.repository.JpaRepository;
import sept.major.common.entity.AbstractEntity;
import sept.major.common.exception.IdentifierUpdateException;
import sept.major.common.exception.InvalidEntityException;
import sept.major.common.exception.RecordNotFoundException;
import sept.major.common.patch.PatchValue;

import javax.persistence.Id;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/*
    A abstract implementation of generic service functionality.
    This service will satisfy the requirements of a simple CRUD service.
 */
public abstract class CrudService<E extends AbstractEntity<ID>, ID> {

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
    public E create(E entity) {
        // TODO Entity validation goes here
        return getRepository().save(entity);
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
    public E patch(ID id, HashMap<String, PatchValue> patchValues) throws RecordNotFoundException, IdentifierUpdateException {

        // Gets the existing record by calling the generic read method implemented in this class. Returns RecordNotFoundException if record doesn't exist.
        E existingEntity = read(id);

        for (Map.Entry<String, PatchValue> patchValueEntry : patchValues.entrySet()) {
            PatchValue patchValue = patchValueEntry.getValue();

            /*
                The id field cannot be updated, to prevent this we check if the setter is used for setting the id.
                It is assumed that the developer put a @Id annotation on the setter, otherwise the code will fail.
             */
            if (patchValue.getSetter().getAnnotation(Id.class) == null) {
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

        // Use the generic create entity logic implemented in this class. Done so there is no repeated logic.
        return create(existingEntity);
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
}
