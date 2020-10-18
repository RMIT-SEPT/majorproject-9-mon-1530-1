package sept.major.users.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sept.major.users.entity.UserEntity;

import javax.persistence.Table;
import java.util.List;
import java.util.Optional;

@Table(name = "users"/* , schema = "users" */)
@Repository
public interface UsersRepository extends JpaRepository<UserEntity, String> {
    List<UserEntity> findAllByUserType(String userType);

    Optional<UserEntity> findByUsername(String username);
    
    
    Optional<UserEntity> findByUsernameAndPassword(String username, String password);

    Optional<UserEntity> findByUsernameAndToken(String username, String token);

}
