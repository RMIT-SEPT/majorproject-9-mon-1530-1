package sept.major.users.service;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.jpa.repository.JpaRepository;
import sept.major.users.entity.AbstractEntity;
import sept.major.users.exception.IdentifierUpdateException;
import sept.major.users.exception.InvalidEntityException;
import sept.major.users.exception.RecordNotFoundException;
import sept.major.users.patch.PatchValue;

import javax.persistence.Id;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public abstract class CrudService<E extends AbstractEntity<ID>, ID> {

    protected abstract JpaRepository<E, ID> getRepository();

    public E read(ID id) throws RecordNotFoundException {
        Optional<E> entity = getRepository().findById(id);
        if (entity.isPresent()) {
            return entity.get();
        } else {
            throw new RecordNotFoundException(String.format("No record with a username of %s was found", id));
        }
    }

    public E create(E entity) {
        // TODO Entity validation goes here
        return getRepository().save(entity);
    }

    public E patch(ID id, HashMap<String, PatchValue> patchValues) throws RecordNotFoundException, IdentifierUpdateException {

        E existingEntity = read(id);

        for (Map.Entry<String, PatchValue> patchValueEntry : patchValues.entrySet()) {
            PatchValue patchValue = patchValueEntry.getValue();
            if (patchValue.getSetter().getAnnotation(Id.class) == null) {
                try {
                    Object existingValue = patchValue.getGetter().invoke(existingEntity);
                    if (existingValue == null) {
                        if (patchValue.getValue() != null) {
                            patchValue.getSetter().invoke(existingEntity, patchValue.getValue());
                        }
                    } else if (!existingValue.equals(patchValue.getValue())) {
                        patchValue.getSetter().invoke(existingEntity, patchValue.getValue());
                    }
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new InvalidEntityException("Received method that is invokable. Only invokable methods should be here");
                }
            } else {
                throw new IdentifierUpdateException();
            }
        }

        return create(existingEntity);
    }

    public void delete(ID id) throws RecordNotFoundException {
        try {
            getRepository().deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new RecordNotFoundException(String.format("No record with a username of %s was found", id));
        }
    }
}
