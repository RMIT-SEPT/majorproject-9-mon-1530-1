package sept.major.users.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sept.major.users.entity.User;
import sept.major.users.lov.UserType;
import sept.major.users.response.error.FieldIncorrectTypeError;
import sept.major.users.response.error.MissingFieldError;
import sept.major.users.response.error.ResponseErrorManager;
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
        User user1 = userControllerHelper.analysePostInput(User.class, requestBody);

        System.out.println("lol");

        ResponseErrorManager responseErrorManager = new ResponseErrorManager();
        User user = new User();

        String usernamne = getField(requestBody, "username", responseErrorManager);
        if (usernamne != null) {
            user.setUsername(usernamne);
        }

        String userType = getField(requestBody, "userType", responseErrorManager);
        if (userType != null) {
            user.setUserType(userType);
        }

        String name = getField(requestBody, "name", responseErrorManager);
        if (name != null) {
            user.setName(name);
        }

        String phone = getField(requestBody, "phone", responseErrorManager);
        if (phone != null) {
            user.setPhone(phone);
        }

        String address = getField(requestBody, "address", responseErrorManager);
        if (address != null) {
            user.setAddress(address);
        }

        User modelResponse = userService.createUser(user);
        if (responseErrorManager.hasErrors()) {
            return responseErrorManager.getResponseEntity();
        } else {
            return new ResponseEntity(modelResponse, HttpStatus.ACCEPTED);
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

    private String getField(Map<String, Object> requestBody, String field, ResponseErrorManager responseErrorManager) {
        Object fieldValue = requestBody.get(field);
        if (fieldValue == null) {
            responseErrorManager.addError(new MissingFieldError(field));
        } else {
            if (fieldValue.getClass().equals(String.class)) {
                return (String) fieldValue;
            } else {
                responseErrorManager.addError(new FieldIncorrectTypeError(field, "String", fieldValue.getClass().toString()));
            }
        }

        return null;
    }
}
