package sept.major.users.entity;

import lombok.*;
import sept.major.users.response.ResponseObject;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users", schema = "users")
@Entity
public class User implements ResponseObject {
    @Id
    private String username;
    private String userType;
    private String name;
    private String phone;
    private String address;
}
