package sept.major.users;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sept.major.users.entity.UserEntity;
import sept.major.users.response.error.FieldIncorrectTypeError;
import sept.major.users.response.error.ResponseError;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
class PatchEndpointTests extends UserServiceTestHelper {

    @Test
    void updateExistingValue() {
        String username = randomAlphanumericString(20);

        Map<String, Object> input = randomEntityMap(username);
        UserEntity inputEntity = createUserEntity(input);

        Map<String, Object> patchMap = new HashMap<String, Object>() {{
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

        Map<String, Object> input = randomEntityMap(username);
        UserEntity inputEntity = createUserEntity(input);

        Map<String, Object> patchMap = new HashMap<String, Object>() {{
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

        Map<String, Object> input = randomEntityMap(username);
        input.put("userType", null);
        UserEntity inputEntity = createUserEntity(input);

        Map<String, Object> patchMap = new HashMap<String, Object>() {{
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

        Map<String, Object> input = new HashMap<String, Object>() {{
            put("userType", null);
            put("name", null);
            put("phone", null);
            put("address", null);
        }};
        UserEntity inputEntity = createUserEntity(input);

        Map<String, Object> patchMap = new HashMap<String, Object>() {{
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

        Map<String, Object> input = randomEntityMap(username);
        UserEntity inputEntity = createUserEntity(input);

        Map<String, Object> patchMap = new HashMap<String, Object>() {{
            put("username", randomAlphanumericString(20));
        }};

        UserEntity expected = getExpected(inputEntity, patchMap);

        when(mockedUserRepository.findById(username)).thenReturn(Optional.of(inputEntity));
        when(mockedUserRepository.save(expected)).thenReturn(expected);

        ResponseEntity result = userServiceController.updateUser(username, patchMap);

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(result.getBody()).isEqualTo(new ResponseError("Identifier field", "Cannot update field used for identifying entities"));
    }

    @Test
    void noExistingEntity() {
        String username = randomAlphanumericString(20);

        Map<String, Object> patchMap = new HashMap<String, Object>() {{
            put("username", randomAlphanumericString(20));
        }};

        when(mockedUserRepository.findById(username)).thenReturn(Optional.empty());

        ResponseEntity result = userServiceController.updateUser(username, patchMap);

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(result.getBody()).isEqualTo(new ResponseError("Identifier field", String.format("No record with a identifier of %s was found", username)));
    }

    @Test
    void updateToList() {
        String username = randomAlphanumericString(20);

        Map<String, Object> patchMap = new HashMap<String, Object>() {{
            put("userType", Arrays.asList(randomAlphanumericString(20)));
        }};

        ResponseEntity result = userServiceController.updateUser(username, patchMap);

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(result.getBody()).isEqualTo(new HashSet<>(Arrays.asList(
                new FieldIncorrectTypeError("userType", "class java.lang.String", "class java.util.Arrays$ArrayList")
        )));
    }

    @Test
    void updateAllToList() {
        String username = randomAlphanumericString(20);

        Map<String, Object> patchMap = new HashMap<String, Object>() {{
            put("userType", Arrays.asList(randomAlphanumericString(20)));
            put("name", Arrays.asList(randomAlphanumericString(20)));
            put("phone", Arrays.asList(randomAlphanumericString(20)));
            put("address", Arrays.asList(randomAlphanumericString(20)));
        }};

        ResponseEntity result = userServiceController.updateUser(username, patchMap);

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(result.getBody()).isEqualTo(new HashSet<>(Arrays.asList(
                new FieldIncorrectTypeError("userType", "class java.lang.String", "class java.util.Arrays$ArrayList"),
                new FieldIncorrectTypeError("name", "class java.lang.String", "class java.util.Arrays$ArrayList"),
                new FieldIncorrectTypeError("phone", "class java.lang.String", "class java.util.Arrays$ArrayList"),
                new FieldIncorrectTypeError("address", "class java.lang.String", "class java.util.Arrays$ArrayList")
        )));
    }

    @Test
    void updateToMap() {
        String username = randomAlphanumericString(20);

        HashMap<String, String> userTypeMap = new HashMap<>();
        userTypeMap.put("userType", randomAlphanumericString(20));

        Map<String, Object> patchMap = new HashMap<String, Object>() {{
            put("userType", userTypeMap);
        }};

        ResponseEntity result = userServiceController.updateUser(username, patchMap);

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(result.getBody()).isEqualTo(new HashSet<>(Arrays.asList(
                new FieldIncorrectTypeError("userType", "class java.lang.String", "class java.util.HashMap")
        )));
    }

    @Test
    void updateAllToMap() {
        String username = randomAlphanumericString(20);

        HashMap<String, String> userTypeMap = new HashMap<>();
        userTypeMap.put("userType", randomAlphanumericString(20));

        HashMap<String, String> nameMap = new HashMap<>();
        nameMap.put("name", randomAlphanumericString(20));

        HashMap<String, String> phoneMap = new HashMap<>();
        phoneMap.put("phone", randomAlphanumericString(20));

        HashMap<String, String> addressMap = new HashMap<>();
        addressMap.put("address", randomAlphanumericString(20));

        Map<String, Object> patchMap = new HashMap<String, Object>() {{
            put("userType", userTypeMap);
            put("name", nameMap);
            put("phone", phoneMap);
            put("address", addressMap);
        }};

        ResponseEntity result = userServiceController.updateUser(username, patchMap);

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(result.getBody()).isEqualTo(new HashSet<>(Arrays.asList(
                new FieldIncorrectTypeError("userType", "class java.lang.String", "class java.util.HashMap"),
                new FieldIncorrectTypeError("phone", "class java.lang.String", "class java.util.HashMap"),
                new FieldIncorrectTypeError("address", "class java.lang.String", "class java.util.HashMap"),
                new FieldIncorrectTypeError("name", "class java.lang.String", "class java.util.HashMap")
        )));
    }

    @Test
    void updateIdentifierToMap() {
        String username = randomAlphanumericString(20);

        Map<String, Object> input = randomEntityMap(username);
        UserEntity inputEntity = createUserEntity(input);

        HashMap<String, String> usernameMap = new HashMap<>();
        usernameMap.put("username", randomAlphanumericString(20));

        Map<String, Object> patchMap = new HashMap<String, Object>() {{
            put("username", usernameMap);
        }};


        when(mockedUserRepository.findById(username)).thenReturn(Optional.of(inputEntity));

        ResponseEntity result = userServiceController.updateUser(username, patchMap);

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(result.getBody()).isEqualTo(new HashSet<>(Arrays.asList(
                new FieldIncorrectTypeError("username", "class java.lang.String", "class java.util.HashMap")
        )));
    }

    @Test
    void updateIdentifierToList() {
        String username = randomAlphanumericString(20);

        Map<String, Object> input = randomEntityMap(username);
        UserEntity inputEntity = createUserEntity(input);


        Map<String, Object> patchMap = new HashMap<String, Object>() {{
            put("username", Arrays.asList(randomAlphanumericString(20)));
        }};


        when(mockedUserRepository.findById(username)).thenReturn(Optional.of(inputEntity));

        ResponseEntity result = userServiceController.updateUser(username, patchMap);

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(result.getBody()).isEqualTo(new HashSet<>(Arrays.asList(
                new FieldIncorrectTypeError("username", "class java.lang.String", "class java.util.Arrays$ArrayList")
        )));
    }


    private UserEntity getExpected(UserEntity existingValue, Map<String, Object> patchMap) {
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

    private Map<String, Object> randomEntityMap(String username) {
        return new HashMap<String, Object>() {{
            put("username", username);
            put("userType", randomAlphanumericString(20));
            put("name", randomAlphanumericString(20));
            put("phone", randomAlphanumericString(20));
            put("address", randomAlphanumericString(20));
        }};
    }

    private UserEntity createUserEntity(Map<String, Object> entityMap) {
        return new UserEntity(
                (String) entityMap.get("username"),
                (String) entityMap.get("userType"),
                (String) entityMap.get("name"),
                (String) entityMap.get("phone"),
                (String) entityMap.get("address"));
    }

}
