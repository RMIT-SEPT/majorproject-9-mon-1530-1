package sept.major.hours.controller.unit.get;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sept.major.hours.controller.unit.HoursUnitTestHelper;
import sept.major.hours.entity.HoursEntity;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
class GetAllEndpointTests extends HoursUnitTestHelper {

    @Test
    void valid() {
        List<HoursEntity> expected = Arrays.asList(
                randomEntity(randomAlphanumericString(4)),
                randomEntity(randomAlphanumericString(4)),
                randomEntity(randomAlphanumericString(4))
        );

        runTest(new ResponseEntity(expected, HttpStatus.ACCEPTED),
                expected, null, null);
    }

    @Test
    void missingResult() {
        runTest(new ResponseEntity("No records within provided bounds were found", HttpStatus.NOT_FOUND),
                Arrays.asList(), null, randomAlphanumericString(4));
    }

    @Test
    void workerUsernameProvidedValid() {
        HoursEntity expected = randomEntity(randomAlphanumericString(4));
        String workerUsername = randomAlphanumericString(20);
        expected.setWorkerUsername(workerUsername);

        List<HoursEntity> returned = Arrays.asList(
                randomEntity(randomAlphanumericString(4)),
                expected,
                randomEntity(randomAlphanumericString(4))
        );

        runTest(new ResponseEntity(Arrays.asList(expected), HttpStatus.ACCEPTED),
                returned, workerUsername, null);
    }

    @Test
    void workerUsernameProvidedMissing() {
        List<HoursEntity> returned = Arrays.asList(
                randomEntity(randomAlphanumericString(4)),
                randomEntity(randomAlphanumericString(4)),
                randomEntity(randomAlphanumericString(4))
        );

        runTest(new ResponseEntity("No records within provided bounds were found", HttpStatus.NOT_FOUND),
                returned, randomAlphanumericString(4), null);
    }

    @Test
    void customerUsernameProvidedValid() {
        HoursEntity expected = randomEntity(randomAlphanumericString(4));
        String customerUsername = randomAlphanumericString(20);
        expected.setCustomerUsername(customerUsername);

        List<HoursEntity> returned = Arrays.asList(
                randomEntity(randomAlphanumericString(4)),
                expected,
                randomEntity(randomAlphanumericString(4))
        );

        runTest(new ResponseEntity(Arrays.asList(expected), HttpStatus.ACCEPTED),
                returned, null, customerUsername);
    }

    @Test
    void customerUsernameProvidedMissing() {
        List<HoursEntity> returned = Arrays.asList(
                randomEntity(randomAlphanumericString(4)),
                randomEntity(randomAlphanumericString(4)),
                randomEntity(randomAlphanumericString(4))
        );

        runTest(new ResponseEntity("No records within provided bounds were found", HttpStatus.NOT_FOUND),
                returned, null, randomAlphanumericString(4));
    }

    @Test
    void customerUsernameAndWorkerUsernameProvidedValid() {
        String customerUsername = randomAlphanumericString(20);
        String workerUsername = randomAlphanumericString(20);

        HoursEntity expected = randomEntity(randomAlphanumericString(4));
        expected.setCustomerUsername(customerUsername);
        expected.setWorkerUsername(workerUsername);

        HoursEntity entityOne = randomEntity(randomAlphanumericString(4));
        entityOne.setCustomerUsername(customerUsername);

        HoursEntity entityTwo = randomEntity(randomAlphanumericString(4));
        entityTwo.setWorkerUsername(workerUsername);

        List<HoursEntity> returned = Arrays.asList(
                entityOne, expected, entityTwo
        );

        runTest(new ResponseEntity(Arrays.asList(expected), HttpStatus.ACCEPTED),
                returned, workerUsername, customerUsername);
    }

    @Test
    void customerUsernameAndWorkerUsernameProvidedMissing() {
        String customerUsername = randomAlphanumericString(20);
        String workerUsername = randomAlphanumericString(20);

        HoursEntity entityOne = randomEntity(randomAlphanumericString(4));
        entityOne.setCustomerUsername(customerUsername);

        HoursEntity entityTwo = randomEntity(randomAlphanumericString(4));
        entityTwo.setWorkerUsername(workerUsername);

        List<HoursEntity> returned = Arrays.asList(
                entityOne, randomEntity(randomAlphanumericString(4)), entityTwo
        );

        runTest(new ResponseEntity("No records within provided bounds were found", HttpStatus.NOT_FOUND),
                returned, workerUsername, customerUsername);
    }

    private void runTest(ResponseEntity expected, List<HoursEntity> returned, String workerUsername, String customerUsername) {
        when(mockedUserRepository.findAll()).thenReturn(returned);
        ResponseEntity result = hoursController.getAllHours(workerUsername, customerUsername);

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(expected.getStatusCode());
        assertThat(result.getBody()).isEqualTo(expected.getBody());
    }


}
