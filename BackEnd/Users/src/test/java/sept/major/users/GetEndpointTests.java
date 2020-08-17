package sept.major.users;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sept.major.users.entity.UserEntity;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
class GetEndpointTests extends UserServiceTestHelper {

    @Test
    void valid() {
        String username = randomAlphanumericString(20);

        UserEntity expected = new UserEntity(
                username,
                "pass123",
                randomAlphanumericString(20),
                randomAlphanumericString(20),
                randomAlphanumericString(20),
                randomAlphanumericString(20)
        );

        when(mockedUserRepository.findById(username)).thenReturn(Optional.of(expected));
        ResponseEntity result = userServiceController.getUser(username);

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(expected);
    }

    @Test
    void missingValue() {
        String username = randomAlphanumericString(20);

        when(mockedUserRepository.findById(username)).thenReturn(Optional.empty());
        ResponseEntity result = userServiceController.getUser(username);

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(result.getBody()).isEqualTo(String.format("No record with a identifier of %s was found", username));
    }

}
