package sept.major.users;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sept.major.common.response.ValidationError;
import sept.major.users.entity.UserEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
class PatchEndpointTests extends UserServiceTestHelper {

    @Test
    void updateExistingValue() {
        String username = randomAlphanumericString(20);

        Map<String, String> input = randomEntityMap(username);
        UserEntity inputEntity = createUserEntity(input);

        Map<String, String> patchMap = new HashMap<String, String>() {{
            put("userType", randomAlphanumericString(20));
        }};

        UserEntity expected = getExpected(inputEntity, patchMap);

        when(mockedUserRepository.findById(username)).thenReturn(Optional.of(inputEntity));
        when(mockedUserRepository.save(expected)).thenReturn(expected);

        ResponseEntity result = userServiceController.updateUser(username, patchMap);

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(expected);
    }

    @Test
    void updateAllNonIdentifiersExistingValues() {
        String username = randomAlphanumericString(20);

        Map<String, String> input = randomEntityMap(username);
        UserEntity inputEntity = createUserEntity(input);

        Map<String, String> patchMap = new HashMap<String, String>() {{
            put("userType", randomAlphanumericString(20));
            put("name", randomAlphanumericString(20));
            put("phone", randomAlphanumericString(20));
            put("address", randomAlphanumericString(20));
        }};

        UserEntity expected = getExpected(inputEntity, patchMap);

        when(mockedUserRepository.findById(username)).thenReturn(Optional.of(inputEntity));
        when(mockedUserRepository.save(expected)).thenReturn(expected);

        ResponseEntity result = userServiceController.updateUser(username, patchMap);

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(expected);
    }

    @Test
    void updateNullValue() {
        String username = randomAlphanumericString(20);

        Map<String, String> input = randomEntityMap(username);
        input.put("userType", null);
        UserEntity inputEntity = createUserEntity(input);

        Map<String, String> patchMap = new HashMap<String, String>() {{
            put("userType", randomAlphanumericString(20));
        }};

        UserEntity expected = getExpected(inputEntity, patchMap);

        when(mockedUserRepository.findById(username)).thenReturn(Optional.of(inputEntity));
        when(mockedUserRepository.save(expected)).thenReturn(expected);

        ResponseEntity result = userServiceController.updateUser(username, patchMap);

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(expected);
    }

    @Test
    void updateAllNonIdentifiersNullValues() {
        String username = randomAlphanumericString(20);

        Map<String, String> input = new HashMap<String, String>() {{
            put("username", username);
            put("userType", null);
            put("name", null);
            put("phone", null);
            put("address", null);
        }};
        UserEntity inputEntity = createUserEntity(input);

        Map<String, String> patchMap = new HashMap<String, String>() {{
            put("userType", randomAlphanumericString(20));
            put("name", randomAlphanumericString(20));
            put("phone", randomAlphanumericString(20));
            put("address", randomAlphanumericString(20));
        }};

        UserEntity expected = getExpected(inputEntity, patchMap);

        when(mockedUserRepository.findById(username)).thenReturn(Optional.of(inputEntity));
        when(mockedUserRepository.save(expected)).thenReturn(expected);

        ResponseEntity result = userServiceController.updateUser(username, patchMap);

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(expected);
    }

    @Test
    void updateIdentifierValue() {
        String username = randomAlphanumericString(20);

        Map<String, String> input = randomEntityMap(username);
        UserEntity inputEntity = createUserEntity(input);

        Map<String, String> patchMap = new HashMap<String, String>() {{
            put("username", randomAlphanumericString(20));
        }};

        UserEntity expected = getExpected(inputEntity, patchMap);

        when(mockedUserRepository.findById(username)).thenReturn(Optional.of(inputEntity));
        when(mockedUserRepository.save(expected)).thenReturn(expected);

        ResponseEntity result = userServiceController.updateUser(username, patchMap);

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(result.getBody()).isEqualTo(new ValidationError("Identifier field", "Cannot update field used for identifying entities"));
    }

    @Test
    void noExistingEntity() {
        String username = randomAlphanumericString(20);

        Map<String, String> patchMap = new HashMap<String, String>() {{
            put("username", randomAlphanumericString(20));
        }};

        when(mockedUserRepository.findById(username)).thenReturn(Optional.empty());

        ResponseEntity result = userServiceController.updateUser(username, patchMap);

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(result.getBody()).isEqualTo(new ValidationError("Identifier field", String.format("No record with a identifier of %s was found", username)));
    }

    private UserEntity getExpected(UserEntity existingValue, Map<String, String> patchMap) {
        if (patchMap.get("username") != null) {
            existingValue.setUsername((String) patchMap.get("username"));
        }

        if (patchMap.get("userType") != null) {
            existingValue.setUserType((String) patchMap.get("userType"));
        }

        if (patchMap.get("name") != null) {
            existingValue.setName((String) patchMap.get("name"));
        }

        if (patchMap.get("phone") != null) {
            existingValue.setPhone((String) patchMap.get("phone"));
        }

        if (patchMap.get("address") != null) {
            existingValue.setAddress((String) patchMap.get("address"));
        }

        return existingValue;
    }

    private Map<String, String> randomEntityMap(String username) {
        return new HashMap<String, String>() {{
            put("username", username);
            put("userType", randomAlphanumericString(20));
            put("name", randomAlphanumericString(20));
            put("phone", randomAlphanumericString(20));
            put("address", randomAlphanumericString(20));
        }};
    }

    private UserEntity createUserEntity(Map<String, String> entityMap) {
        return new UserEntity(
                (String) entityMap.get("username"),
                "pass123",
                (String) entityMap.get("userType"),
                (String) entityMap.get("name"),
                (String) entityMap.get("phone"),
                (String) entityMap.get("address"));
    }

}
