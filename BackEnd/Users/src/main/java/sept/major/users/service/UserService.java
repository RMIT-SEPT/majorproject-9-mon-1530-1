package sept.major.users.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import sept.major.users.entity.User;
import sept.major.users.lov.UserType;
import sept.major.users.repository.UsersRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UsersRepository usersRepository;

    public ResponseEntity<List<User>> getBulkUsers(UserType userType) {
        return null;
    }

    public Optional<User> getUser(String username) {
        return usersRepository.findByUsername(username);
    }

    public User createUser(User user) {
        usersRepository.save(user);
        return user;
    }

    public User updateUser(Map<String, Object> requestBody) {
        return null;
    }

    public void deleteUser(String username) {
        usersRepository.deleteByUsername(username);
    }

    public void updatePassword(String username, Map<String, Object> requestBody) {

    }

    public void comparePassword(Map<String, Object> requestBody) {

    }
}
