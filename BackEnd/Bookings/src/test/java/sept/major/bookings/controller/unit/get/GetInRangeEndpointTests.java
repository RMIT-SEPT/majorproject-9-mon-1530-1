package sept.major.bookings.controller.unit.get;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sept.major.bookings.controller.BookingServiceController;
import sept.major.bookings.controller.unit.UnitTestHelper;
import sept.major.bookings.entity.BookingEntity;
import sept.major.common.response.ValidationError;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static sept.major.bookings.BookingsTestHelper.*;

@SpringBootTest
class GetInRangeEndpointTests extends UnitTestHelper {

    @Test
    void invalidStartDate() {
        runTestsWithUsernameFilters(new ResponseEntity(new ValidationError("startDate", BookingServiceController.INCORRECT_DATE_TIME_FORMAT_ERROR_MESSAGE), HttpStatus.BAD_REQUEST),
                null, "foo", LocalDateTime.now().toString());
    }

    @Test
    void invalidEndDate() {
        runTestsWithUsernameFilters(new ResponseEntity(new ValidationError("endDate", BookingServiceController.INCORRECT_DATE_TIME_FORMAT_ERROR_MESSAGE), HttpStatus.BAD_REQUEST),
                null, LocalDateTime.now().toString(), "foo");
    }

    @Test
    void bothDatesInvalid() {
        runTestsWithUsernameFilters(new ResponseEntity(new ValidationError("startDate", BookingServiceController.INCORRECT_DATE_TIME_FORMAT_ERROR_MESSAGE), HttpStatus.BAD_REQUEST),
                null, "foo", "foo");
    }

    @Test
    void startDateValid() {
        LocalDateTime startDate = pastDate(2, 2, 0).atStartOfDay();
        LocalDateTime endDate = pastDate(1, 2, 0).atStartOfDay();

        BookingEntity expected = randomEntityWithDateRange(randomInt(4), startDate, endDate);

        runTestsWithUsernameFilters(new ResponseEntity(Arrays.asList(expected), HttpStatus.OK),
                Arrays.asList(expected), startDate.toString(), endDate.toString());
    }

    @Test
    void endDateValid() {
        LocalDateTime endDate = pastDate(1, 2, 3).atStartOfDay();
        LocalDateTime startDate = pastDate(2, 2, 3).atStartOfDay();

        BookingEntity expected = randomEntityWithDateRange(randomInt(4), startDate, endDate);

        runTestsWithUsernameFilters(new ResponseEntity(Arrays.asList(expected), HttpStatus.OK),
                Arrays.asList(expected), startDate.toString(), endDate.toString());
    }

    @Test
    void bothDatesValid() {
        LocalDateTime startDate = pastDate(1, 2, 3).atStartOfDay();
        LocalDateTime endDate = pastDate(1, 2, 0).atStartOfDay();

        BookingEntity expected = randomEntityWithDateRange(randomInt(4), startDate, endDate);

        runTestsWithUsernameFilters(new ResponseEntity(Arrays.asList(expected), HttpStatus.OK),
                Arrays.asList(expected), startDate.toString(), endDate.toString());
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


    private void runTestsWithUsernameFilters(ResponseEntity expected, List<BookingEntity> returned, String startDate, String endDate) {
        testWorkerUsernameFilter(expected, returned, startDate, endDate);
        testcustomerUsernameFilter(expected, returned, startDate, endDate);
        testCustomerAndWorkerUsernameFilter(expected, returned, startDate, endDate);
    }

    private void testWorkerUsernameFilter(ResponseEntity expected, List<BookingEntity> returned, String startDate, String endDate) {
        String workerUsername = randomAlphanumericString(20);

        if (returned != null) {
            List<BookingEntity> workerUsernameEntities = deepCopy(returned);
            workerUsernameEntities.forEach(hoursEntity -> hoursEntity.setWorkerUsername(workerUsername));

            workerUsernameEntities.add(randomEntityWithDateRange(randomInt(4), (startDate == null) ? null : LocalDateTime.parse(startDate), (endDate == null) ? null : LocalDateTime.parse(endDate)));

            runTest(updateExpectedWithUsername(expected, workerUsername, null), workerUsernameEntities, startDate, endDate, workerUsername, null);
        } else {
            runTest(updateExpectedWithUsername(expected, workerUsername, null), returned, startDate, endDate, workerUsername, null);
        }


    }

    private void testcustomerUsernameFilter(ResponseEntity expected, List<BookingEntity> returned, String startDate, String endDate) {
        String customerUsername = randomAlphanumericString(20);

        if (returned != null) {
            List<BookingEntity> customerUsernameEntities = deepCopy(returned);
            customerUsernameEntities.forEach(hoursEntity -> hoursEntity.setCustomerUsername(customerUsername));

            customerUsernameEntities.add(randomEntityWithDateRange(randomInt(4), (startDate == null) ? null : LocalDateTime.parse(startDate), (endDate == null) ? null : LocalDateTime.parse(endDate)));

            runTest(updateExpectedWithUsername(expected, null, customerUsername), customerUsernameEntities, startDate, endDate, null, customerUsername);
        } else {
            runTest(updateExpectedWithUsername(expected, null, customerUsername), returned, startDate, endDate, null, customerUsername);
        }


    }

    private void testCustomerAndWorkerUsernameFilter(ResponseEntity expected, List<BookingEntity> returned, String startDate, String endDate) {
        String customerUsername = randomAlphanumericString(20);
        String workerUsername = randomAlphanumericString(20);

        if (returned != null) {
            List<BookingEntity> usernameEntities = deepCopy(returned);
            usernameEntities.forEach(hoursEntity -> hoursEntity.setCustomerUsername(customerUsername));
            usernameEntities.forEach(hoursEntity -> hoursEntity.setWorkerUsername(workerUsername));

            BookingEntity customerUsernameEntity = randomEntityWithDateRange(randomInt(4), (startDate == null) ? null : LocalDateTime.parse(startDate), (endDate == null) ? null : LocalDateTime.parse(endDate));
            customerUsernameEntity.setCustomerUsername(customerUsername);
            usernameEntities.add(customerUsernameEntity);

            BookingEntity workerUsernameEntity = randomEntityWithDateRange(randomInt(4), (startDate == null) ? null : LocalDateTime.parse(startDate), (endDate == null) ? null : LocalDateTime.parse(endDate));
            workerUsernameEntity.setWorkerUsername(workerUsername);
            usernameEntities.add(workerUsernameEntity);

            usernameEntities.add(randomEntityWithDateRange(randomInt(4), (startDate == null) ? null : LocalDateTime.parse(startDate), (endDate == null) ? null : LocalDateTime.parse(endDate)));

            runTest(updateExpectedWithUsername(expected, workerUsername, customerUsername), usernameEntities, startDate, endDate, workerUsername, customerUsername);
        } else {
            runTest(updateExpectedWithUsername(expected, workerUsername, customerUsername), null, startDate, endDate, workerUsername, customerUsername);
        }

    }


    private void runTest(ResponseEntity expected, List<BookingEntity> returned, String startDate, String endDate, String workerUsername, String customerUsername) {
        when(mockedBookingRepository.findAllInRange(any(), any())).thenReturn(returned);
        ResponseEntity result = bookingServiceController.getRange(startDate, endDate, workerUsername, customerUsername);

        assertThat(result).isNotNull();
        assertThat(result.getBody()).isEqualTo(expected.getBody());
        assertThat(result.getStatusCode()).isEqualTo(expected.getStatusCode());

    }

    private ResponseEntity updateExpectedWithUsername(ResponseEntity expected, String workerUsername, String customerUsername) {
        Object expectedBody = expected.getBody();
        if (expectedBody instanceof BookingEntity) {
            BookingEntity hoursEntity = (BookingEntity) expectedBody;
            if (workerUsername != null) {
                hoursEntity.setWorkerUsername(workerUsername);
            }
            if (customerUsername != null) {
                hoursEntity.setCustomerUsername(customerUsername);
            }
            return new ResponseEntity(hoursEntity, expected.getStatusCode());
        }
        if (expectedBody instanceof List) {
            List<BookingEntity> entityList = (List<BookingEntity>) expectedBody;
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
