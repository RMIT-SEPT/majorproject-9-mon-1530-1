package sept.major.common.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

/*
    Class used by ControllerHelp and CrudService.
    Classes which extend this class should be used for data storage and transfer, particularly with JpaRepositories.
    The setter for the identifying field must have a @Id annotation. (Use @Setter(onMethod = @__(@Id)) if using Lombok).
    Any implementation must also have
     * An accessible (public) empty constructor
     * Getters and setter(s) for at least one field. If you don't wish to use any then you will get no benefit from implementing this class
 */
public interface AbstractEntity<ID> {
    /*
        This method should get the value of the identifying field for the entity.
     */
    @JsonIgnore
    ID getID();
}
