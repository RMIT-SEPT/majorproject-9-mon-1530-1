package sept.major.users.service;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.jpa.repository.JpaRepository;
import sept.major.users.entity.AbstractEntity;
import sept.major.users.exception.RecordNotFoundException;

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

    public E patch(ID id) {
        return null; // TODO patch logic
    }

    public void delete(ID id) throws RecordNotFoundException {
        try {
            getRepository().deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new RecordNotFoundException(String.format("No record with a username of %s was found", id));
        }
    }
}
