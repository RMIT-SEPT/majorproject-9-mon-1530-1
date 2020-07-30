package sept.major.users.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sept.major.users.entity.UserEntity;
import sept.major.users.lov.UserType;
import sept.major.users.service.UserService;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserServiceController {

    @Autowired
    UserService userService;

    @Autowired
    UserServiceControllerHelper userControllerHelper;

    @GetMapping()
    public ResponseEntity getUser(@RequestParam String username) {
        return userControllerHelper.getEntity(username);
    }

    @PostMapping
    public ResponseEntity createUser(@RequestBody Map<String, Object> requestBody) {
        return userControllerHelper.createEntity(UserEntity.class, requestBody);
    }

    @DeleteMapping
    public ResponseEntity deleteUser(@RequestParam String username) {
        return userControllerHelper.deleteEntity(username);
    }

    @PatchMapping
    public ResponseEntity updateUser() {
        Field[] fields = UserEntity.class.getDeclaredFields();
        System.out.println(fields);
        return new ResponseEntity("test", HttpStatus.ACCEPTED);
    }

    @GetMapping("/bulk")
    public ResponseEntity<List<UserEntity>> getBulkUsers(@RequestParam(value = "userType", required = false) String userTypeString) {
        return userService.getBulkUsers(
                (userTypeString == null) ? null : UserType.valueOf(userTypeString.toUpperCase())
        );
    }


    @PutMapping("/password")
    public void updatePassword() {

    }

    @PostMapping("/password/compare")
    public void comparePassword() {

    }
}
