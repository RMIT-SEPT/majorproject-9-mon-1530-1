package sept.major.common.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author Brodey Yendall
 * @version %I%, %G%
 * @since 1.0.9
 *
 * An entity must implement this interface to be used in ControllerHelper and CrudService.
 * Currently only provides the {@link #getID()} method  however might be needed in future functionality.
 * By implementing this class it assumes the subclass has a public empty constructor.
 *
 * @param <ID> The class of the field used as the identifier for the entity
 */
public interface AbstractEntity<ID> {
    /**
     *
     * Gets the value of the identifying field for this entity, vital for generic update entity functionality.
     * Method is annotated with {@link JsonIgnore} so that id doesn't duplicate when this entity is converted to JSON.
     *
     * @return The value of the identifying field for this entity
     */
    @JsonIgnore
    ID getID();
}
