package sept.major.users.entity;

import lombok.*;
import sept.major.common.entity.AbstractEntity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Table(name = "users", schema = "users")
@Entity
public class UserEntity implements AbstractEntity<String> {

    @Id
    @Setter(onMethod = @__(@Id))
    private String username;

    private String userType;
    private String name;
    private String phone;
    private String address;

    @Override
    public String getID() {
        return username;
    }
}
