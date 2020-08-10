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

        runTestsWithUsernameFilters(new ResponseEntity(expected, HttpStatus.ACCEPTED),
                expected,null);
    }

    @Test
    void noDateMissingResult() {
        runTestsWithUsernameFilters(new ResponseEntity("No records within provided bounds were found", HttpStatus.NOT_FOUND),
                Arrays.asList(), null);
    }

    @Test
    void dateValid() {
        LocalDate date = pastDate(1,2,3);

        HoursEntity expected = randomEntityWithDate(randomAlphanumericString(4), date);

        runTestsWithUsernameFilters(new ResponseEntity(Arrays.asList(expected), HttpStatus.ACCEPTED),
                Arrays.asList(expected), date);
    }

    @Test
    void dateMissingResult() {
        runTestsWithUsernameFilters(new ResponseEntity("No records within provided bounds were found", HttpStatus.NOT_FOUND),
                Arrays.asList(), LocalDate.now());
    }


    private void runTestsWithUsernameFilters(ResponseEntity expected, List<HoursEntity> returned, LocalDate date) {
        testWorkerUsernameFilter(expected,returned,date);
        testCustomerUsernameFilter(expected,returned,date);
        testCustomerAndWorkerUsernameFilter(expected,returned,date);
    }

    private void testWorkerUsernameFilter(ResponseEntity expected, List<HoursEntity> returned, LocalDate date) {
        String workerUsername = randomAlphanumericString(20);

        List<HoursEntity> workerUsernameEntities = new ArrayList<>(returned);
        workerUsernameEntities.forEach(hoursEntity -> hoursEntity.setWorkerUsername(workerUsername));

        workerUsernameEntities.add(randomEntityWithDate(randomAlphanumericString(4), date));

        runTest(expected, returned, date);
    }

    private void testCustomerUsernameFilter(ResponseEntity expected, List<HoursEntity> returned, LocalDate date) {
        String customerUsername = randomAlphanumericString(20);

        List<HoursEntity> customerUsernameEntities = new ArrayList<>(returned);
        customerUsernameEntities.forEach(hoursEntity -> hoursEntity.setCustomerUsername(customerUsername));

        customerUsernameEntities.add(randomEntityWithDate(randomAlphanumericString(4), date));

        runTest(expected, returned, date);
    }

    private void testCustomerAndWorkerUsernameFilter(ResponseEntity expected, List<HoursEntity> returned, LocalDate date) {
        String customerUsername = randomAlphanumericString(20);
        String workerUsername = randomAlphanumericString(20);

        List<HoursEntity> usernameEntities = new ArrayList<>(returned);
        usernameEntities.forEach(hoursEntity -> hoursEntity.setCustomerUsername(customerUsername));
        usernameEntities.forEach(hoursEntity -> hoursEntity.setWorkerUsername(workerUsername));

        HoursEntity customerUsernameEntity = randomEntityWithDate(randomAlphanumericString(4), date);
        customerUsernameEntity.setCustomerUsername(customerUsername);
        usernameEntities.add(customerUsernameEntity);

        HoursEntity workerUsernameEntity = randomEntityWithDate(randomAlphanumericString(4), date);
        workerUsernameEntity.setWorkerUsername(workerUsername);
        usernameEntities.add(workerUsernameEntity);

        usernameEntities.add(randomEntityWithDate(randomAlphanumericString(4), date));

        runTest(expected, returned, date);
    }



    private void runTest(ResponseEntity expected, List<HoursEntity> returned, LocalDate date) {
        when(mockedUserRepository.findAllByDate(date)).thenReturn(returned);
        ResponseEntity result = hoursController.getHoursInDate((date == null ? null: date.toString()), null, null);

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(expected.getStatusCode());
        assertThat(expected.getBody()).isEqualTo(result.getBody());
    }






}
