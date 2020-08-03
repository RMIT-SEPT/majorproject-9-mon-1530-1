package sept.major.users.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sept.major.users.entity.UserEntity;

import javax.persistence.Table;
import java.util.List;

@Table(name = "users", schema = "users")
@Repository
public interface UsersRepository extends JpaRepository<UserEntity, String> {
    List<UserEntity> findAllByUserType(String userType);
}
