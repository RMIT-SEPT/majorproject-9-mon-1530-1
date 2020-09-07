package sept.major.users.unit;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sept.major.users.entity.UserEntity;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static sept.major.users.UserServiceTestHelper.randomAlphanumericString;

@SpringBootTest
class GetBulkEndpointTests extends UserServiceTestHelper {

    @Test
    void valid() {
        String userType = randomAlphanumericString(20);
        
        List<UserEntity> expected = Arrays.asList(
                new UserEntity(randomAlphanumericString(20), "pass123", userType, randomAlphanumericString(20), randomAlphanumericString(20), randomAlphanumericString(20)),
                new UserEntity(randomAlphanumericString(20), "pass123", userType, randomAlphanumericString(20), randomAlphanumericString(20), randomAlphanumericString(20)),
                new UserEntity(randomAlphanumericString(20), "pass123", userType, randomAlphanumericString(20), randomAlphanumericString(20), randomAlphanumericString(20))
        );

        when(mockedUserRepository.findAllByUserType(userType)).thenReturn(expected);
        ResponseEntity result = userServiceController.getBulkUsers(userType);

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(expected);
    }

    @Test
    void validNoUserType() {
        List<UserEntity> expected = Arrays.asList(
                new UserEntity(randomAlphanumericString(20), "pass123", randomAlphanumericString(20), randomAlphanumericString(20), randomAlphanumericString(20), randomAlphanumericString(20)),
                new UserEntity(randomAlphanumericString(20), "pass123", randomAlphanumericString(20), randomAlphanumericString(20), randomAlphanumericString(20), randomAlphanumericString(20)),
                new UserEntity(randomAlphanumericString(20), "pass123", randomAlphanumericString(20), randomAlphanumericString(20), randomAlphanumericString(20), randomAlphanumericString(20))
        );

        when(mockedUserRepository.findAll()).thenReturn(expected);
        ResponseEntity result = userServiceController.getBulkUsers(null);

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(expected);
    }

    @Test
    void missingResult() {
        String userType = randomAlphanumericString(20);

        List<UserEntity> expected = Arrays.asList();

        when(mockedUserRepository.findAllByUserType(userType)).thenReturn(expected);
        ResponseEntity result = userServiceController.getBulkUsers(userType);

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(result.getBody()).isEqualTo(String.format("No record with a userType of %s was found", userType));
    }

    @Test
    void missingResultNoUserType() {
        List<UserEntity> expected = Arrays.asList();

        when(mockedUserRepository.findAll()).thenReturn(expected);
        ResponseEntity result = userServiceController.getBulkUsers(null);

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(result.getBody()).isEqualTo(String.format("No record was found"));
    }

}
