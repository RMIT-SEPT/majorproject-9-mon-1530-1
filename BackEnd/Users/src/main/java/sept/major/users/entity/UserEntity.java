package sept.major.users.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import sept.major.common.entity.AbstractEntity;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Table(name = "users"/* , schema = "users" */)
@Entity
public class UserEntity implements AbstractEntity<String> {

    @Id
    @Setter(onMethod = @__(@Id))
    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    @Column(name = "usertype")
    private String userType;

    @NotBlank
    private String name;

    @NotBlank
    private String phone;

    @NotBlank
    private String address;

    @Override
    public String getID() {
        return username;
    }
}
