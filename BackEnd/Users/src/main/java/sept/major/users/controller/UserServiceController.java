package sept.major.users.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sept.major.common.exception.RecordNotFoundException;
import sept.major.users.entity.UserEntity;
import sept.major.users.service.UserService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserServiceController {

    UserService userService;
    UserServiceControllerHelper userControllerHelper;

    @Autowired
    public UserServiceController(UserService userService, UserServiceControllerHelper userControllerHelper) {
        this.userService = userService;
        this.userControllerHelper = userControllerHelper;
    }

    @GetMapping()
    public ResponseEntity getUser(@RequestParam String username) {
        return userControllerHelper.getEntity(username);
    }

    @PostMapping
    public ResponseEntity createUser(@RequestBody Map<String, Object> requestBody) {
        return userControllerHelper.validateInputAndPost(UserEntity.class, requestBody);
    }

    @DeleteMapping
    public ResponseEntity deleteUser(@RequestParam String username) {
        return userControllerHelper.deleteEntity(username);
    }

    @PatchMapping
    public ResponseEntity updateUser(@RequestParam String username, @RequestBody Map<String, Object> requestBody) {
        return userControllerHelper.validateInputAndPatch(UserEntity.class, username, requestBody);
    }

    @GetMapping("/bulk")
    public ResponseEntity<List<UserEntity>> getBulkUsers(@RequestParam(required = false) String userType) {
        try {
            List<UserEntity> entityList = userService.readBulkUsers(userType);
            return new ResponseEntity(entityList, HttpStatus.OK);
        } catch (RecordNotFoundException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


    /**
     * Endpoint for changing user password
     */
    @PutMapping("/password")
    public void updatePassword() {
    	
    }

    /**
     * Endpoint to receive user password compare calls
     * @param username
     * @param password
     * @return
     */
    @GetMapping("/password/compare")
    public ResponseEntity comparePassword(@RequestParam String username , String password) {
    	System.out.println("username:"+ username + " password:" + password);
    	boolean result = userService.comparePassword(username, password);
    	
        return new ResponseEntity("inpput," + "username:"+ username + " password:" + password + ", password compare:" + result, HttpStatus.ACCEPTED);
    }
}
