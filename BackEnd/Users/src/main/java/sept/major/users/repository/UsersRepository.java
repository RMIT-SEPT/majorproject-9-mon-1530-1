package sept.major.users.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sept.major.users.entity.User;

import javax.persistence.Table;
import java.util.Optional;

@Table(name = "users", schema = "users")
@Repository
public interface UsersRepository extends JpaRepository<User, String> {
    Optional<User> findByUsername(String username);

    void deleteByUsername(String username);
}
