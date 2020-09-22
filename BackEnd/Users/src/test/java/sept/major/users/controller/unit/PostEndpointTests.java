package sept.major.users.controller.unit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sept.major.common.response.ValidationError;
import sept.major.users.entity.UserEntity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static sept.major.users.UserTestHelper.*;

@SpringBootTest
public class PostEndpointTests extends UnitTestHelper {

    @Test
    @DisplayName("No username provided")
    void missingUsername() {
        Map<String, String> randomEntity = randomEntityMap();
        randomEntity.remove("username");

        runTest(new ResponseEntity(Arrays.asList(new ValidationError("username", "must not be blank")), HttpStatus.BAD_REQUEST), randomEntity);
    }

    @Test
    @DisplayName("Successfully create an entity")
    void valid() {
        Map<String, String> input = randomEntityMap();
        UserEntity inputEntity = createUserEntity(input);

        runTest(new ResponseEntity(inputEntity, HttpStatus.OK), input);
    }

    @Test
    @DisplayName("No password provided")
    void missingPassword() {
        Map<String, String> randomEntity = randomEntityMap();
        randomEntity.remove("password");

        runTest(new ResponseEntity(Arrays.asList(new ValidationError("password", "must not be blank")), HttpStatus.BAD_REQUEST), randomEntity);
    }

    @Test
    @DisplayName("No username provided")
    void missingUserType() {
        Map<String, String> randomEntity = randomEntityMap();
        randomEntity.remove("userType");

        runTest(new ResponseEntity(Arrays.asList(new ValidationError("userType", "must not be blank")), HttpStatus.BAD_REQUEST), randomEntity);
    }

    @Test
    @DisplayName("No name provided")
    void missingName() {
        Map<String, String> randomEntity = randomEntityMap();
        randomEntity.remove("name");

        runTest(new ResponseEntity(Arrays.asList(new ValidationError("name", "must not be blank")), HttpStatus.BAD_REQUEST), randomEntity);
    }

    @Test
    @DisplayName("No phone provided")
    void missingPhone() {
        Map<String, String> randomEntity = randomEntityMap();
        randomEntity.remove("phone");

        runTest(new ResponseEntity(Arrays.asList(new ValidationError("phone", "must not be blank")), HttpStatus.BAD_REQUEST), randomEntity);
    }

    @Test
    @DisplayName("No address provided")
    void missingAddress() {
        Map<String, String> randomEntity = randomEntityMap();
        randomEntity.remove("address");

        runTest(new ResponseEntity(Arrays.asList(new ValidationError("address", "must not be blank")), HttpStatus.BAD_REQUEST), randomEntity);
    }

    @Test
    @DisplayName("No value fields provided (one username)")
    void missingAllValueFields() {
        Map<String, String> randomEntity = new HashMap<>();
        randomEntity.put("username", randomAlphanumericString(20));

        runTest(new ResponseEntity(Arrays.asList(
                new ValidationError("userType", "must not be blank"),
                new ValidationError("name", "must not be blank"),
                new ValidationError("password", "must not be blank"),
                new ValidationError("phone", "must not be blank"),
                new ValidationError("address", "must not be blank")
        ), HttpStatus.BAD_REQUEST), randomEntity);
    }

    @Test
    @DisplayName("No fields provided (empty POST body)")
    void missingAllFields() {
        Map<String, String> randomEntity = new HashMap<>();

        runTest(new ResponseEntity(Arrays.asList(
                new ValidationError("username", "must not be blank"),
                new ValidationError("userType", "must not be blank"),
                new ValidationError("name", "must not be blank"),
                new ValidationError("password", "must not be blank"),
                new ValidationError("phone", "must not be blank"),
                new ValidationError("address", "must not be blank")
        ), HttpStatus.BAD_REQUEST), randomEntity);
    }

    private void runTest(ResponseEntity expected, Map<String, String> input) {
        UserEntity inputEntity = createUserEntity(input);

        when(mockedUserRepository.save(inputEntity)).thenReturn(inputEntity);

        ResponseEntity result = userServiceController.createUser(input);
        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(expected.getStatusCode());
        if (expected.getBody() instanceof List) {
            assertThat((List) result.getBody()).containsAll((List) expected.getBody());
        } else {
            assertThat(result.getBody()).isEqualTo(expected.getBody());
        }

    }
}
