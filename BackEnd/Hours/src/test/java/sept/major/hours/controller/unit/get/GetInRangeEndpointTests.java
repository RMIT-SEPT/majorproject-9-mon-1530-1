package sept.major.hours.controller.unit.get;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sept.major.common.response.ResponseError;
import sept.major.hours.controller.HoursController;
import sept.major.hours.controller.unit.HoursUnitTestHelper;
import sept.major.hours.entity.HoursEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class GetInRangeEndpointTests extends HoursUnitTestHelper {

    @Test
    void noDatesProvided() {
        runTestsWithUsernameFilters(new ResponseEntity(new ResponseError("date range", "You must provide at least one date in the range"), HttpStatus.BAD_REQUEST), null, null, null);
    }

    @Test
    void invalidStartDate() {
        runTestsWithUsernameFilters(new ResponseEntity(new ResponseError("startDate", HoursController.INCORRECT_DATE_TIME_FORMAT_ERROR_MESSAGE), HttpStatus.BAD_REQUEST),
                null, "foo", null);
    }

    @Test
    void invalidEndDate() {
        runTestsWithUsernameFilters(new ResponseEntity(new ResponseError("endDate", HoursController.INCORRECT_DATE_TIME_FORMAT_ERROR_MESSAGE), HttpStatus.BAD_REQUEST),
                null, null, "foo");
    }

    @Test
    void bothDatesInvalid() {
        runTestsWithUsernameFilters(new ResponseEntity(new ResponseError("startDate", HoursController.INCORRECT_DATE_TIME_FORMAT_ERROR_MESSAGE), HttpStatus.BAD_REQUEST),
                null, "foo", "foo");
    }

    @Test
    void startDateValid() {
        LocalDateTime startDate = pastDate(1, 2, 0).atStartOfDay();

        HoursEntity expected = randomEntityWithDateRange(randomAlphanumericString(4), startDate.toString(), null);

        runTestsWithUsernameFilters(new ResponseEntity(Arrays.asList(expected), HttpStatus.ACCEPTED),
                Arrays.asList(expected), startDate.toString(), null);
    }

    @Test
    void endDateValid() {
        LocalDateTime endDate = pastDate(1, 2, 3).atStartOfDay();

        HoursEntity expected = randomEntityWithDateRange(randomAlphanumericString(4), endDate.toString(), null);

        runTestsWithUsernameFilters(new ResponseEntity(Arrays.asList(expected), HttpStatus.ACCEPTED),
                Arrays.asList(expected), null, endDate.toString());
    }

    @Test
    void bothDatesValid() {
        LocalDateTime startDate = pastDate(1, 2, 3).atStartOfDay();
        LocalDateTime endDate = pastDate(1, 2, 0).atStartOfDay();

        HoursEntity expected = randomEntityWithDateRange(randomAlphanumericString(4), startDate.toString(), endDate.toString());

        runTestsWithUsernameFilters(new ResponseEntity(Arrays.asList(expected), HttpStatus.ACCEPTED),
                Arrays.asList(expected), startDate.toString(), endDate.toString());
    }


    @Test
    void startDateMissingResult() {
        runTestsWithUsernameFilters(new ResponseEntity("No records within provided bounds were found", HttpStatus.NOT_FOUND),
                Arrays.asList(), LocalDateTime.now().toString(), null);
    }

    @Test
    void endDateMissingResult() {
        runTestsWithUsernameFilters(new ResponseEntity("No records within provided bounds were found", HttpStatus.NOT_FOUND),
                Arrays.asList(), null, LocalDateTime.now().toString());
    }

    @Test
    void bothDateMissingResult() {
        runTestsWithUsernameFilters(new ResponseEntity("No records within provided bounds were found", HttpStatus.NOT_FOUND),
                Arrays.asList(), LocalDateTime.now().toString(), LocalDateTime.now().toString());
    }

    @Test
    void endDateBeforeStartDate() {
        runTestsWithUsernameFilters(new ResponseEntity(new ResponseError("date range", "start date must be above the end date"), HttpStatus.BAD_REQUEST),
                Arrays.asList(), LocalDateTime.now().toString(), pastDate(0, 0, 1).atStartOfDay().toString());
    }


    private void runTestsWithUsernameFilters(ResponseEntity expected, List<HoursEntity> returned, String startDate, String endDate) {
        testWorkerUsernameFilter(expected, returned, startDate, endDate);
        testCustomerUsernameFilter(expected, returned, startDate, endDate);
        testCustomerAndWorkerUsernameFilter(expected, returned, startDate, endDate);
    }

    private void testWorkerUsernameFilter(ResponseEntity expected, List<HoursEntity> returned, String startDate, String endDate) {
        String workerUsername = randomAlphanumericString(20);

        if (returned != null) {
            List<HoursEntity> workerUsernameEntities = deepCopy(returned);
            workerUsernameEntities.forEach(hoursEntity -> hoursEntity.setWorkerUsername(workerUsername));

            workerUsernameEntities.add(randomEntityWithDateRange(randomAlphanumericString(4), startDate, endDate));

            runTest(updateExpectedWithUsername(expected, workerUsername, null), workerUsernameEntities, startDate, endDate, workerUsername, null);
        } else {
            runTest(updateExpectedWithUsername(expected, workerUsername, null), returned, startDate, endDate, workerUsername, null);
        }


    }

    private void testCustomerUsernameFilter(ResponseEntity expected, List<HoursEntity> returned, String startDate, String endDate) {
        String customerUsername = randomAlphanumericString(20);

        if (returned != null) {
            List<HoursEntity> customerUsernameEntities = deepCopy(returned);
            customerUsernameEntities.forEach(hoursEntity -> hoursEntity.setCustomerUsername(customerUsername));

            customerUsernameEntities.add(randomEntityWithDateRange(randomAlphanumericString(4), startDate, endDate));

            runTest(updateExpectedWithUsername(expected, null, customerUsername), customerUsernameEntities, startDate, endDate, null, customerUsername);
        } else {
            runTest(updateExpectedWithUsername(expected, null, customerUsername), returned, startDate, endDate, null, customerUsername);
        }


    }

    private void testCustomerAndWorkerUsernameFilter(ResponseEntity expected, List<HoursEntity> returned, String startDate, String endDate) {
        String customerUsername = randomAlphanumericString(20);
        String workerUsername = randomAlphanumericString(20);

        if (returned != null) {
            List<HoursEntity> usernameEntities = deepCopy(returned);
            usernameEntities.forEach(hoursEntity -> hoursEntity.setCustomerUsername(customerUsername));
            usernameEntities.forEach(hoursEntity -> hoursEntity.setWorkerUsername(workerUsername));

            HoursEntity customerUsernameEntity = randomEntityWithDateRange(randomAlphanumericString(4), startDate, endDate);
            customerUsernameEntity.setCustomerUsername(customerUsername);
            usernameEntities.add(customerUsernameEntity);

            HoursEntity workerUsernameEntity = randomEntityWithDateRange(randomAlphanumericString(4), startDate, endDate);
            workerUsernameEntity.setWorkerUsername(workerUsername);
            usernameEntities.add(workerUsernameEntity);

            usernameEntities.add(randomEntityWithDateRange(randomAlphanumericString(4), startDate, endDate));

            runTest(updateExpectedWithUsername(expected, workerUsername, customerUsername), usernameEntities, startDate, endDate, workerUsername, customerUsername);
        } else {
            runTest(updateExpectedWithUsername(expected, workerUsername, customerUsername), null, startDate, endDate, workerUsername, customerUsername);
        }

    }


    private void runTest(ResponseEntity expected, List<HoursEntity> returned, String startDate, String endDate, String workerUsername, String customerUsername) {
        when(mockedUserRepository.findAllBetweenDates(any(), any())).thenReturn(returned);
        ResponseEntity result = hoursController.getHoursInRange(startDate, endDate, workerUsername, customerUsername);

        assertThat(result).isNotNull();
        assertThat(result.getBody()).isEqualTo(expected.getBody());
        assertThat(result.getStatusCode()).isEqualTo(expected.getStatusCode());

    }

    private ResponseEntity updateExpectedWithUsername(ResponseEntity expected, String workerUsername, String customerUsername) {
        Object expectedBody = expected.getBody();
        if (expectedBody instanceof HoursEntity) {
            HoursEntity hoursEntity = (HoursEntity) expectedBody;
            if (workerUsername != null) {
                hoursEntity.setWorkerUsername(workerUsername);
            }
            if (customerUsername != null) {
                hoursEntity.setCustomerUsername(customerUsername);
            }
            return new ResponseEntity(hoursEntity, expected.getStatusCode());
        }
        if (expectedBody instanceof List) {
            List<HoursEntity> entityList = (List<HoursEntity>) expectedBody;
            entityList.forEach(hoursEntity -> {
                if (workerUsername != null) {
                    hoursEntity.setWorkerUsername(workerUsername);
                }
                if (customerUsername != null) {
                    hoursEntity.setCustomerUsername(customerUsername);
                }
            });

            return new ResponseEntity(entityList, expected.getStatusCode());
        }

        return expected;
    }


}
