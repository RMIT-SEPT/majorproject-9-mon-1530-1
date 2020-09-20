package sept.major.users.controller.unit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static sept.major.users.UserTestHelper.randomAlphanumericString;

public class DeleteEndpointTests extends UnitTestHelper {

    @Test
    @DisplayName("A entity is correctly deleted")
    void valid() {
        String username = randomAlphanumericString(20);

        Mockito.doNothing().when(mockedUserRepository).deleteById(username);

        ResponseEntity result = userServiceController.deleteUser(username);

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo("Record successfully deleted");
    }

    @Test
    @DisplayName("No entity to delete")
    void missing() {
        String username = randomAlphanumericString(20);

        Mockito.doThrow(new EmptyResultDataAccessException(1)).when(mockedUserRepository).deleteById(username);

        ResponseEntity result = userServiceController.deleteUser(username);

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(result.getBody()).isEqualTo(String.format("No record with a identifier of %s was found", username));
    }
}
