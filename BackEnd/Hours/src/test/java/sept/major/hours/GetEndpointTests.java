package sept.major.hours;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sept.major.hours.entity.HoursEntity;

import javax.xml.ws.Response;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
class GetEndpointTests extends UserServiceTestHelper {

    @Test
    void valid() {
        String id = randomAlphanumericString(20);

        HoursEntity expected = randomEntity(id);

        runTest(new ResponseEntity(expected, HttpStatus.OK),
                Optional.of(expected), id);
    }

    @Test
    void missingValue() {
        String id = randomAlphanumericString(20);

        runTest(new ResponseEntity(String.format("No record with a identifier of %s was found", id), HttpStatus.NOT_FOUND),
                Optional.empty(), id);
    }

    private void runTest(ResponseEntity expected, Optional<HoursEntity> returned, String id) {
        when(mockedUserRepository.findById(id)).thenReturn(returned);
        ResponseEntity result = hoursController.getHours(id);

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(expected.getStatusCode());
        assertThat(result.getBody()).isEqualTo(expected.getBody());
    }

}
