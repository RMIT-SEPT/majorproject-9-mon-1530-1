package sept.major.users.repository;


import java.util.List;
import java.util.Optional;

import javax.persistence.Table;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sept.major.users.entity.UserEntity;

@Table(name = "users"/* , schema = "users" */)
@Repository
public interface UsersRepository extends JpaRepository<UserEntity, String> {
    List<UserEntity> findAllByUserType(String userType);

    Optional<UserEntity> findByUsernameAndPassword(String username, String password);
    
}
