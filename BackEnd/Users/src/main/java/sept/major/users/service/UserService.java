package sept.major.users.service;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import sept.major.users.entity.UserEntity;
import sept.major.users.lov.UserType;
import sept.major.users.repository.UsersRepository;

import java.util.List;
import java.util.Map;

@Service
public class UserService extends CrudService<UserEntity, String> {

    @Getter
    private UsersRepository repository;

    @Autowired
    public UserService(UsersRepository usersRepository) {
        this.repository = usersRepository;
    }

    public ResponseEntity<List<UserEntity>> getBulkUsers(UserType userType) {
        return null;
    }

    public UserEntity updateUser(Map<String, Object> requestBody) {
        return null;
    }

    public void updatePassword(String username, Map<String, Object> requestBody) {

    }

    public void comparePassword(Map<String, Object> requestBody) {

    }
}
