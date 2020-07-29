package sept.major.users.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sept.major.users.entity.User;
import sept.major.users.exception.ResponseErrorFoundException;
import sept.major.users.lov.UserType;
import sept.major.users.service.UserService;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserServiceController {

    @Autowired
    UserService userService;

    @Autowired
    ControllerHelper<User> userControllerHelper;

    @GetMapping("/bulk")
    public ResponseEntity<List<User>> getBulkUsers(@RequestParam(value = "userType", required = false) String userTypeString) {
        return userService.getBulkUsers(
                (userTypeString == null) ? null : UserType.valueOf(userTypeString.toUpperCase())
        );
    }

    @GetMapping()
    public ResponseEntity getUser(@RequestParam() String username) {
        Optional<User> modelResponse = userService.getUser(username);
        if (modelResponse.isPresent()) {
            return new ResponseEntity(modelResponse.get(), HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity(String.format("User with username %s not found", username), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity createUser(@RequestBody Map<String, Object> requestBody) {
        try {
            User user = userControllerHelper.analysePostInput(User.class, requestBody);
            return new ResponseEntity(userService.createUser(user), HttpStatus.OK);
        } catch (ResponseErrorFoundException e) {
            return e.generateResponseEntity();
        }

    }

    @PatchMapping
    public ResponseEntity updateUser() {
        Field[] fields = User.class.getDeclaredFields();
        System.out.println(fields);
        return new ResponseEntity("test", HttpStatus.ACCEPTED);
    }

    @DeleteMapping
    public void deleteUser() {

    }

    @PutMapping("/password")
    public void updatePassword() {

    }

    @PostMapping("/password/compare")
    public void comparePassword() {

    }
}
