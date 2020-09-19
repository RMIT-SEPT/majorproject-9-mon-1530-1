package sept.major.users.controller.unit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sept.major.common.response.ValidationError;
import sept.major.users.entity.UserEntity;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static sept.major.users.UserTestHelper.*;

@SpringBootTest
class PatchEndpointTests extends UnitTestHelper {

    @Test
    @DisplayName("Successfully patch an entity")
    void valid() {
        String username = randomAlphanumericString(20);

        Map<String, String> existingMap = randomEntityMap(username);
        UserEntity existing = createUserEntity(existingMap);

        HashMap patchValues = new HashMap() {{
            put("customerUsername", randomAlphanumericString(20));
        }};
        UserEntity patchedEntity = patchEntity(existing, patchValues);  // Manually update the entity so we can compare the results

        runTest(username, patchValues, Optional.of(existing), patchedEntity,
                new ResponseEntity(patchedEntity, HttpStatus.OK));
    }

    @Test
    @DisplayName("Successfully patch with no patch values")
    void noUpdate() {
        String username = randomAlphanumericString(20);

        Map<String, String> existingMap = randomEntityMap(username);
        UserEntity existing = createUserEntity(existingMap);


        runTest(username, new HashMap<>(), Optional.of(existing), existing,
                new ResponseEntity(existing, HttpStatus.OK));
    }


    @Test
    @DisplayName("Patch attempt on non existing entity")
    void noExisting() {
        String username = randomAlphanumericString(20);

        Map<String, String> existingMap = randomEntityMap(username);
        UserEntity existing = createUserEntity(existingMap);

        HashMap patchValues = new HashMap() {{
            put("customerUsername", randomAlphanumericString(20));
        }};
        UserEntity patchedEntity = patchEntity(existing, patchValues);

        runTest(username, patchValues, Optional.empty(), patchedEntity,
                new ResponseEntity(new ValidationError("Identifier field", String.format("No record with a identifier of %s was found", username)), HttpStatus.NOT_FOUND));
    }

    @Test
    @DisplayName("Update not blank field to blank")
    void emptyFieldUpdate() {
        String username = randomAlphanumericString(20);

        Map<String, String> existingMap = randomEntityMap(username);
        UserEntity existing = createUserEntity(existingMap);

        HashMap patchValues = new HashMap() {{
            put("userType", " ");
        }};
        UserEntity patchedEntity = patchEntity(existing, patchValues);

        runTest(username, patchValues, Optional.of(existing), patchedEntity,
                new ResponseEntity((Arrays.asList(new ValidationError("userType", "must not be blank"))), HttpStatus.BAD_REQUEST));
    }

    @Test
    @DisplayName("Update multiple not blank fields to blank")
    void missingFields() {
        String username = randomAlphanumericString(20);

        Map<String, String> existingMap = randomEntityMap(username);
        UserEntity existing = createUserEntity(existingMap);

        HashMap patchValues = new HashMap() {{
            put("userType", " ");
            put("name", " ");
        }};
        UserEntity patchedEntity = patchEntity(existing, patchValues);

        runTest(username, patchValues, Optional.of(existing), patchedEntity,
                new ResponseEntity((Arrays.asList(
                        new ValidationError("userType", "must not be blank"),
                        new ValidationError("name", "must not be blank")
                )), HttpStatus.BAD_REQUEST));
    }


    private void runTest(String username, Map<String, String> patchValues, Optional<UserEntity> existing, UserEntity patchedEntity, ResponseEntity expected) {
        when(mockedUserRepository.findById(username)).thenReturn(existing);
        when(mockedUserRepository.save(any())).thenReturn(patchedEntity);

        ResponseEntity result = userServiceController.updateUser(username, patchValues);

        assertThat(result).isNotNull();
        if (expected.getBody() instanceof List) {
            assertThat((List) result.getBody()).containsAll((List) expected.getBody());
        } else {
            assertThat(result.getBody()).isEqualTo(expected.getBody());
        }
        assertThat(result.getStatusCode()).isEqualTo(expected.getStatusCode());
    }
}
