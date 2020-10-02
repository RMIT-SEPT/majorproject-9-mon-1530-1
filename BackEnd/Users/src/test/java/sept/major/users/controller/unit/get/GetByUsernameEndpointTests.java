package sept.major.users.controller.unit.get;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sept.major.users.controller.unit.UnitTestHelper;
import sept.major.users.entity.UserEntity;

import java.util.AbstractMap;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static sept.major.users.UserTestHelper.randomAlphanumericString;
import static sept.major.users.UserTestHelper.randomEntity;

@SpringBootTest
class GetByUsernameEndpointTests extends UnitTestHelper {

    @Test
    @DisplayName("Successfully retrieve record by username")
    void valid() {
        String username = randomAlphanumericString(20);

        UserEntity expected = randomEntity();

        runTest(new ResponseEntity(expected, HttpStatus.OK),
                Optional.of(expected), username);
    }

    @Test
    @DisplayName("No record found with given username")
    void missingValue() {
        String username = randomAlphanumericString(20);

        runTest(new ResponseEntity(new AbstractMap.SimpleEntry<>("message", String.format("No record with a identifier of %s was found", username)), HttpStatus.NOT_FOUND),
                Optional.empty(), username);
    }

    private void runTest(ResponseEntity expected, Optional<UserEntity> returned, String username) {
        when(mockedUserRepository.findById(username)).thenReturn(returned);

        ResponseEntity result = userServiceController.getUser(username);

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(expected.getStatusCode());
        assertThat(result.getBody()).isEqualTo(expected.getBody());
    }

}
