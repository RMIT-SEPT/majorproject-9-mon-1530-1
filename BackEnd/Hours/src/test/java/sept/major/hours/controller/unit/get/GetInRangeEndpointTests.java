package sept.major.hours.controller.unit.get;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sept.major.common.response.ValidationError;
import sept.major.hours.controller.HoursController;
import sept.major.hours.controller.unit.HoursUnitTestHelper;
import sept.major.hours.entity.HoursEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static sept.major.hours.HoursTestHelper.*;

@SpringBootTest
class GetInRangeEndpointTests extends HoursUnitTestHelper {

    @Test
    void noDatesProvided() {
        runTestsWithUsernameFilters(new ResponseEntity(new ValidationError("date range", "You must provide at least one date in the range"), HttpStatus.BAD_REQUEST), null, null, null);
    }

    @Test
    void invalidStartDate() {
        runTestsWithUsernameFilters(new ResponseEntity(new ValidationError("startDate", HoursController.INCORRECT_DATE_TIME_FORMAT_ERROR_MESSAGE), HttpStatus.BAD_REQUEST),
                null, "foo", null);
    }

    @Test
    void invalidEndDate() {
        runTestsWithUsernameFilters(new ResponseEntity(new ValidationError("endDate", HoursController.INCORRECT_DATE_TIME_FORMAT_ERROR_MESSAGE), HttpStatus.BAD_REQUEST),
                null, null, "foo");
    }

    @Test
    void bothDatesInvalid() {
        runTestsWithUsernameFilters(new ResponseEntity(new ValidationError("startDate", HoursController.INCORRECT_DATE_TIME_FORMAT_ERROR_MESSAGE), HttpStatus.BAD_REQUEST),
                null, "foo", "foo");
    }

    @Test
    void startDateValid() {
        LocalDateTime startDate = pastDate(1, 2, 0).atStartOfDay();

        HoursEntity expected = randomEntityWithDateRange(randomInt(4), startDate, null);

        runTestsWithUsernameFilters(new ResponseEntity(Arrays.asList(expected), HttpStatus.ACCEPTED),
                Arrays.asList(expected), startDate.toString(), null);
    }

    @Test
    void endDateValid() {
        LocalDateTime endDate = pastDate(1, 2, 3).atStartOfDay();

        HoursEntity expected = randomEntityWithDateRange(randomInt(4), endDate, null);

        runTestsWithUsernameFilters(new ResponseEntity(Arrays.asList(expected), HttpStatus.ACCEPTED),
                Arrays.asList(expected), null, endDate.toString());
    }

    @Test
    void bothDatesValid() {
        LocalDateTime startDate = pastDate(1, 2, 3).atStartOfDay();
        LocalDateTime endDate = pastDate(1, 2, 0).atStartOfDay();

        HoursEntity expected = randomEntityWithDateRange(randomInt(4), startDate, endDate);

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
        runTestsWithUsernameFilters(new ResponseEntity(new ValidationError("date range", "start date must be above the end date"), HttpStatus.BAD_REQUEST),
                Arrays.asList(), LocalDateTime.now().toString(), pastDate(0, 0, 1).atStartOfDay().toString());
    }


    private void runTestsWithUsernameFilters(ResponseEntity expected, List<HoursEntity> returned, String startDate, String endDate) {
        testWorkerUsernameFilter(expected, returned, startDate, endDate);
        testcreatorUsernameFilter(expected, returned, startDate, endDate);
        testCustomerAndWorkerUsernameFilter(expected, returned, startDate, endDate);
    }

    private void testWorkerUsernameFilter(ResponseEntity expected, List<HoursEntity> returned, String startDate, String endDate) {
        String workerUsername = randomAlphanumericString(20);

        if (returned != null) {
            List<HoursEntity> workerUsernameEntities = deepCopy(returned);
            workerUsernameEntities.forEach(hoursEntity -> hoursEntity.setWorkerUsername(workerUsername));

            workerUsernameEntities.add(randomEntityWithDateRange(randomInt(4), (startDate == null) ? null : LocalDateTime.parse(startDate), (endDate == null) ? null : LocalDateTime.parse(endDate)));

            runTest(updateExpectedWithUsername(expected, workerUsername, null), workerUsernameEntities, startDate, endDate, workerUsername, null);
        } else {
            runTest(updateExpectedWithUsername(expected, workerUsername, null), returned, startDate, endDate, workerUsername, null);
        }


    }

    private void testcreatorUsernameFilter(ResponseEntity expected, List<HoursEntity> returned, String startDate, String endDate) {
        String creatorUsername = randomAlphanumericString(20);

        if (returned != null) {
            List<HoursEntity> creatorUsernameEntities = deepCopy(returned);
            creatorUsernameEntities.forEach(hoursEntity -> hoursEntity.setCreatorUsername(creatorUsername));

            creatorUsernameEntities.add(randomEntityWithDateRange(randomInt(4), (startDate == null) ? null : LocalDateTime.parse(startDate), (endDate == null) ? null : LocalDateTime.parse(endDate)));

            runTest(updateExpectedWithUsername(expected, null, creatorUsername), creatorUsernameEntities, startDate, endDate, null, creatorUsername);
        } else {
            runTest(updateExpectedWithUsername(expected, null, creatorUsername), returned, startDate, endDate, null, creatorUsername);
        }


    }

    private void testCustomerAndWorkerUsernameFilter(ResponseEntity expected, List<HoursEntity> returned, String startDate, String endDate) {
        String creatorUsername = randomAlphanumericString(20);
        String workerUsername = randomAlphanumericString(20);

        if (returned != null) {
            List<HoursEntity> usernameEntities = deepCopy(returned);
            usernameEntities.forEach(hoursEntity -> hoursEntity.setCreatorUsername(creatorUsername));
            usernameEntities.forEach(hoursEntity -> hoursEntity.setWorkerUsername(workerUsername));

            HoursEntity creatorUsernameEntity = randomEntityWithDateRange(randomInt(4), (startDate == null) ? null : LocalDateTime.parse(startDate), (endDate == null) ? null : LocalDateTime.parse(endDate));
            creatorUsernameEntity.setCreatorUsername(creatorUsername);
            usernameEntities.add(creatorUsernameEntity);

            HoursEntity workerUsernameEntity = randomEntityWithDateRange(randomInt(4), (startDate == null) ? null : LocalDateTime.parse(startDate), (endDate == null) ? null : LocalDateTime.parse(endDate));
            workerUsernameEntity.setWorkerUsername(workerUsername);
            usernameEntities.add(workerUsernameEntity);

            usernameEntities.add(randomEntityWithDateRange(randomInt(4), (startDate == null) ? null : LocalDateTime.parse(startDate), (endDate == null) ? null : LocalDateTime.parse(endDate)));

            runTest(updateExpectedWithUsername(expected, workerUsername, creatorUsername), usernameEntities, startDate, endDate, workerUsername, creatorUsername);
        } else {
            runTest(updateExpectedWithUsername(expected, workerUsername, creatorUsername), null, startDate, endDate, workerUsername, creatorUsername);
        }

    }


    private void runTest(ResponseEntity expected, List<HoursEntity> returned, String startDate, String endDate, String workerUsername, String creatorUsername) {
        when(mockedUserRepository.findAllByStartDateTimeBetween(any(), any())).thenReturn(returned);
        ResponseEntity result = hoursController.getHoursInRange(startDate, endDate, workerUsername, creatorUsername);

        assertThat(result).isNotNull();
        assertThat(result.getBody()).isEqualTo(expected.getBody());
        assertThat(result.getStatusCode()).isEqualTo(expected.getStatusCode());

    }

    private ResponseEntity updateExpectedWithUsername(ResponseEntity expected, String workerUsername, String creatorUsername) {
        Object expectedBody = expected.getBody();
        if (expectedBody instanceof HoursEntity) {
            HoursEntity hoursEntity = (HoursEntity) expectedBody;
            if (workerUsername != null) {
                hoursEntity.setWorkerUsername(workerUsername);
            }
            if (creatorUsername != null) {
                hoursEntity.setCreatorUsername(creatorUsername);
            }
            return new ResponseEntity(hoursEntity, expected.getStatusCode());
        }
        if (expectedBody instanceof List) {
            List<HoursEntity> entityList = (List<HoursEntity>) expectedBody;
            entityList.forEach(hoursEntity -> {
                if (workerUsername != null) {
                    hoursEntity.setWorkerUsername(workerUsername);
                }
                if (creatorUsername != null) {
                    hoursEntity.setCreatorUsername(creatorUsername);
                }
            });

            return new ResponseEntity(entityList, expected.getStatusCode());
        }

        return expected;
    }


}
