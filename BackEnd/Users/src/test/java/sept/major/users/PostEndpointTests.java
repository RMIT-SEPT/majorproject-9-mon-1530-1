package sept.major.users;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sept.major.common.response.ValidationError;
import sept.major.users.entity.UserEntity;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@SpringBootTest
class PostEndpointTests extends UserServiceTestHelper {

    @Test
    void valid() {
        String username = randomAlphanumericString(20);

        Map<String, String> input = randomEntityMap(username);
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

        Map<String, String> input = randomEntityMap(username);
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

        Map<String, String> input = new HashMap<String, String>() {{
            put("username", username);
            put("password", password);
            put("name", name);
            put("phone", phone);
            put("address", address);
        }};

        ResponseEntity result = userServiceController.createUser(input);

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(result.getBody()).isEqualTo(Arrays.asList(new ValidationError("userType", "must not be blank")));
    }

    @Test
    void missingAllValueFields() {
        String username = randomAlphanumericString(20);

        Map<String, String> input = new HashMap<String, String>() {{
            put("username", username);
        }};

        ResponseEntity result = userServiceController.createUser(input);

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertTrue(((List) result.getBody()).containsAll(Arrays.asList(
                new ValidationError("password", "must not be blank"),
                new ValidationError("userType", "must not be blank"),
                new ValidationError("name", "must not be blank"),
                new ValidationError("phone", "must not be blank"),
                new ValidationError("address", "must not be blank")
        )));
    }

    @Test
    void missingIdentifier() {
        String userType = randomAlphanumericString(20);
        String password = randomAlphanumericString(20);
        String name = randomAlphanumericString(20);
        String phone = randomAlphanumericString(20);
        String address = randomAlphanumericString(20);

        Map<String, String> input = new HashMap<String, String>() {{
            put("userType", userType);
            put("password", password);
            put("name", name);
            put("phone", phone);
            put("address", address);
        }};

        ResponseEntity result = userServiceController.createUser(input);

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(result.getBody()).isEqualTo(Arrays.asList(new ValidationError("username", "must not be blank")));
    }

    @Test
    void missingAllFields() {
        Map<String, String> input = new HashMap<>();

        ResponseEntity result = userServiceController.createUser(input);

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertTrue(((List) result.getBody()).containsAll(Arrays.asList(
                new ValidationError("username", "must not be blank"),
                new ValidationError("password", "must not be blank"),
                new ValidationError("userType", "must not be blank"),
                new ValidationError("name", "must not be blank"),
                new ValidationError("phone", "must not be blank"),
                new ValidationError("address", "must not be blank")
        )));


    }

    private Map<String, String> randomEntityMap(String username) {
        return new HashMap<String, String>() {{
            put("username", username);
            put("password", randomAlphanumericString(20));
            put("userType", randomAlphanumericString(20));
            put("name", randomAlphanumericString(20));
            put("phone", randomAlphanumericString(20));
            put("address", randomAlphanumericString(20));
        }};
    }

    private UserEntity createUserEntity(Map<String, String> entityMap) {
        return new UserEntity(
                entityMap.get("username"),
                entityMap.get("password"),
                entityMap.get("userType"),
                entityMap.get("name"),
                entityMap.get("phone"),
                entityMap.get("address"));
    }

}
