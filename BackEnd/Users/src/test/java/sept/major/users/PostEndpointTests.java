package sept.major.users;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sept.major.common.response.FieldIncorrectTypeError;
import sept.major.common.response.ResponseError;
import sept.major.users.entity.UserEntity;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
class PostEndpointTests extends UserServiceTestHelper {

    @Test
    void valid() {
        String username = randomAlphanumericString(20);

        Map<String, Object> input = randomEntityMap(username);
        UserEntity inputEntity = createUserEntity(input);

        when(mockedUserRepository.findById(username)).thenReturn(Optional.empty());
        when(mockedUserRepository.save(inputEntity)).thenReturn(inputEntity);

        ResponseEntity result = userServiceController.createUser(input);

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(inputEntity);
    }

    @Test
    void existing() {
        String username = randomAlphanumericString(20);

        Map<String, Object> input = randomEntityMap(username);
        UserEntity inputEntity = createUserEntity(input);

        when(mockedUserRepository.findById(username)).thenReturn(Optional.of(createUserEntity(randomEntityMap(username))));
        when(mockedUserRepository.save(inputEntity)).thenReturn(inputEntity);

        ResponseEntity result = userServiceController.createUser(input);

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(result.getBody()).isEqualTo("Failed to create entity because an entity with it's identifier already exists");
    }

    @Test
    void missingField() {
        String username = randomAlphanumericString(20);
        String password = randomAlphanumericString(20);
        String name = randomAlphanumericString(20);
        String phone = randomAlphanumericString(20);
        String address = randomAlphanumericString(20);

        Map<String, Object> input = new HashMap<String, Object>() {{
            put("username", username);
            put("password", password);
            put("name", name);
            put("phone", phone);
            put("address", address);
        }};

        ResponseEntity result = userServiceController.createUser(input);

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(result.getBody()).isEqualTo(new HashSet<>(Arrays.asList(new ResponseError("userType", "must not be blank"))));
    }

    @Test
    void missingAllValueFields() {
        String username = randomAlphanumericString(20);

        Map<String, Object> input = new HashMap<String, Object>() {{
            put("username", username);
        }};

        ResponseEntity result = userServiceController.createUser(input);

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(result.getBody()).isEqualTo(new HashSet<>(Arrays.asList(
                new ResponseError("password", "must not be blank"),
                new ResponseError("userType", "must not be blank"),
                new ResponseError("name", "must not be blank"),
                new ResponseError("phone", "must not be blank"),
                new ResponseError("address", "must not be blank")
        )));
    }

    @Test
    void missingIdentifier() {
        String userType = randomAlphanumericString(20);
        String password = randomAlphanumericString(20);
        String name = randomAlphanumericString(20);
        String phone = randomAlphanumericString(20);
        String address = randomAlphanumericString(20);

        Map<String, Object> input = new HashMap<String, Object>() {{
            put("userType", userType);
            put("password", password);
            put("name", name);
            put("phone", phone);
            put("address", address);
        }};

        ResponseEntity result = userServiceController.createUser(input);

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(result.getBody()).isEqualTo(new HashSet<>(Arrays.asList(new ResponseError("username", "must not be blank"))));
    }

    @Test
    void missingAllFields() {
        Map<String, Object> input = new HashMap<>();

        ResponseEntity result = userServiceController.createUser(input);

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(result.getBody()).isEqualTo(new HashSet<>(Arrays.asList(
                new ResponseError("username", "must not be blank"),
                new ResponseError("password", "must not be blank"),
                new ResponseError("userType", "must not be blank"),
                new ResponseError("name", "must not be blank"),
                new ResponseError("phone", "must not be blank"),
                new ResponseError("address", "must not be blank")
        )));
    }


    @Test
    void listField() {
        String username = randomAlphanumericString(20);
        ArrayList<String> userType = new ArrayList<>(Arrays.asList(randomAlphanumericString(20)));
        String password = randomAlphanumericString(20);
        String name = randomAlphanumericString(20);
        String phone = randomAlphanumericString(20);
        String address = randomAlphanumericString(20);

        Map<String, Object> input = new HashMap<String, Object>() {{
            put("username", username);
            put("password", password);
            put("userType", userType);
            put("name", name);
            put("phone", phone);
            put("address", address);
        }};

        ResponseEntity result = userServiceController.createUser(input);

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(result.getBody()).isEqualTo(new HashSet<>(Arrays.asList(new FieldIncorrectTypeError("userType", "class java.lang.String", "class java.util.ArrayList"))));
    }

    @Test
    void mapField() {
        String username = randomAlphanumericString(20);
        HashMap<String, String> userType = new HashMap<>();
        userType.put("userType", randomAlphanumericString(20));
        String password = randomAlphanumericString(20);
        String name = randomAlphanumericString(20);
        String phone = randomAlphanumericString(20);
        String address = randomAlphanumericString(20);

        Map<String, Object> input = new HashMap<String, Object>() {{
            put("username", username);
            put("password", password);
            put("userType", userType);
            put("name", name);
            put("phone", phone);
            put("address", address);
        }};

        ResponseEntity result = userServiceController.createUser(input);

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(result.getBody()).isEqualTo(new HashSet<>(Arrays.asList(new FieldIncorrectTypeError("userType", "class java.lang.String", "class java.util.HashMap"))));
    }

    @Test
    void listIdentifier() {
        ArrayList<String> username = new ArrayList<>(Arrays.asList(randomAlphanumericString(20)));
        String password = randomAlphanumericString(20);
        String userType = randomAlphanumericString(20);
        String name = randomAlphanumericString(20);
        String phone = randomAlphanumericString(20);
        String address = randomAlphanumericString(20);

        Map<String, Object> input = new HashMap<String, Object>() {{
            put("username", username);
            put("password", password);
            put("userType", userType);
            put("name", name);
            put("phone", phone);
            put("address", address);
        }};

        ResponseEntity result = userServiceController.createUser(input);

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(result.getBody()).isEqualTo(new HashSet<>(Arrays.asList(new FieldIncorrectTypeError("username", "class java.lang.String", "class java.util.ArrayList"))));
    }

    @Test
    void mapIdentifier() {
        HashMap<String, String> username = new HashMap<>();
        username.put("username", randomAlphanumericString(20));
        String password = randomAlphanumericString(20);
        String userType = randomAlphanumericString(20);
        String name = randomAlphanumericString(20);
        String phone = randomAlphanumericString(20);
        String address = randomAlphanumericString(20);

        Map<String, Object> input = new HashMap<String, Object>() {{
            put("username", username);
            put("password", password);
            put("userType", userType);
            put("name", name);
            put("phone", phone);
            put("address", address);
        }};

        ResponseEntity result = userServiceController.createUser(input);

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(result.getBody()).isEqualTo(new HashSet<>(Arrays.asList(new FieldIncorrectTypeError("username", "class java.lang.String", "class java.util.HashMap"))));
    }

    @Test
    void mixedIncorrectType() {
        HashMap<String, String> username = new HashMap<>();
        username.put("username", randomAlphanumericString(20));

        HashMap<String, String> userType = new HashMap<>();
        username.put("userType", randomAlphanumericString(20));

        HashMap<String, String> password = new HashMap<>();
        username.put("password", randomAlphanumericString(20));

        ArrayList<String> name = new ArrayList<>(Arrays.asList(randomAlphanumericString(20)));
        ArrayList<String> phone = new ArrayList<>(Arrays.asList(randomAlphanumericString(20)));

        HashMap<String, String> address = new HashMap<>();
        username.put("address", randomAlphanumericString(20));

        Map<String, Object> input = new HashMap<String, Object>() {{
            put("username", username);
            put("password", password);
            put("userType", userType);
            put("name", name);
            put("phone", phone);
            put("address", address);
        }};

        ResponseEntity result = userServiceController.createUser(input);

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(result.getBody()).isEqualTo(new HashSet<>(Arrays.asList(
                new FieldIncorrectTypeError("username", "class java.lang.String", "class java.util.HashMap"),
                new FieldIncorrectTypeError("password", "class java.lang.String", "class java.util.HashMap"),
                new FieldIncorrectTypeError("userType", "class java.lang.String", "class java.util.HashMap"),
                new FieldIncorrectTypeError("name", "class java.lang.String", "class java.util.ArrayList"),
                new FieldIncorrectTypeError("phone", "class java.lang.String", "class java.util.ArrayList"),
                new FieldIncorrectTypeError("address", "class java.lang.String", "class java.util.HashMap")

        )));
    }


    private Map<String, Object> randomEntityMap(String username) {
        return new HashMap<String, Object>() {{
            put("username", username);
            put("password", randomAlphanumericString(20));
            put("userType", randomAlphanumericString(20));
            put("name", randomAlphanumericString(20));
            put("phone", randomAlphanumericString(20));
            put("address", randomAlphanumericString(20));
        }};
    }

    private UserEntity createUserEntity(Map<String, Object> entityMap) {
        return new UserEntity(
                (String) entityMap.get("username"),
                (String) entityMap.get("password"),
                (String) entityMap.get("userType"),
                (String) entityMap.get("name"),
                (String) entityMap.get("phone"),
                (String) entityMap.get("address"));
    }

}
