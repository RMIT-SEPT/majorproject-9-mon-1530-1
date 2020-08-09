package sept.major.hours;

import org.apache.tomcat.jni.Local;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sept.major.hours.entity.HoursEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
class GetOnDateEndpointTests extends UserServiceTestHelper {

    @Test
    void noDateValid() {
        List<HoursEntity> expected = Arrays.asList(
                randomEntity(randomAlphanumericString(4)),
                randomEntity(randomAlphanumericString(4)),
                randomEntity(randomAlphanumericString(4))
        );

        runTest(new ResponseEntity(expected, HttpStatus.ACCEPTED),
                expected,null, null, null);
    }

    @Test
    void noDateMissingResult() {
        runTest(new ResponseEntity("No records within provided bounds were found", HttpStatus.NOT_FOUND),
                Arrays.asList(), null, null, randomAlphanumericString(4));
    }

    @Test
    void dateValid() {
        LocalDate date = pastDate(1,2,3);

        HoursEntity expected = randomEntityWithDate(randomAlphanumericString(4), date);

        runTest(new ResponseEntity(Arrays.asList(expected), HttpStatus.ACCEPTED),
                Arrays.asList(expected), date, null, null);
    }

    @Test
    void dateMissingResult() {
        runTest(new ResponseEntity("No records within provided bounds were found", HttpStatus.NOT_FOUND),
                Arrays.asList(), LocalDate.now(), null, randomAlphanumericString(4));
    }




    private void runTest(ResponseEntity expected, List<HoursEntity> returned, LocalDate date, String workerUsername, String customerUsername) {
        when(mockedUserRepository.findAllByDate(date)).thenReturn(returned);
        ResponseEntity result = hoursController.getHoursInDate((date == null ? null: date.toString()), workerUsername, customerUsername);

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(expected.getStatusCode());
        assertThat(expected.getBody()).isEqualTo(result.getBody());
    }






}
