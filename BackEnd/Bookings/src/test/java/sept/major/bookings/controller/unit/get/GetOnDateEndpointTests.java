package sept.major.bookings.controller.unit.get;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sept.major.bookings.controller.unit.UnitTestHelper;
import sept.major.bookings.entity.BookingEntity;
import sept.major.common.response.ValidationError;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static sept.major.bookings.BookingsTestHelper.*;

@SpringBootTest
class GetOnDateEndpointTests extends UnitTestHelper {
    @Test
    void invalidDate() {
        runTest(new ResponseEntity(new ValidationError("date", bookingServiceController.INCORRECT_DATE_FORMAT_ERROR_MESSAGE), HttpStatus.BAD_REQUEST),
                null, "foo", null, null);
    }

    @Test
    void dateValid() {
        LocalDate date = pastDate(1, 2, 3);

        BookingEntity expected = randomEntityWithDate(randomInt(4), date);

        runTestsWithUsernameFilters(new ResponseEntity(Arrays.asList(expected), HttpStatus.OK),
                Arrays.asList(expected), date.toString());
    }

    @Test
    void dateMissingResult() {
        LocalDate currentDate = LocalDate.now();

        runTestsWithUsernameFilters(new ResponseEntity("No records within provided bounds were found", HttpStatus.NOT_FOUND),
                Arrays.asList(), LocalDate.now().toString());
    }


    private void runTestsWithUsernameFilters(ResponseEntity expected, List<BookingEntity> returned, String date) {
        testWorkerUsernameFilter(expected, returned, date);
        testCustomerUsernameFilter(expected, returned, date);
        testCustomerAndWorkerUsernameFilter(expected, returned, date);
    }

    private void testWorkerUsernameFilter(ResponseEntity expected, List<BookingEntity> returned, String date) {
        String workerUsername = randomAlphanumericString(20);

        List<BookingEntity> workerUsernameEntities = deepCopy(returned);
        workerUsernameEntities.forEach(hoursEntity -> hoursEntity.setWorkerUsername(workerUsername));

        workerUsernameEntities.add(randomEntityWithDate(randomInt(4), LocalDate.parse(date)));

        runTest(updateExpectedWithUsername(expected, workerUsername, null), workerUsernameEntities, date, workerUsername, null);
    }

    private void testCustomerUsernameFilter(ResponseEntity expected, List<BookingEntity> returned, String date) {
        String customerUsername = randomAlphanumericString(20);

        List<BookingEntity> customerUsernameEntities = deepCopy(returned);
        customerUsernameEntities.forEach(hoursEntity -> hoursEntity.setCustomerUsername(customerUsername));

        customerUsernameEntities.add(randomEntityWithDate(randomInt(4), LocalDate.parse(date)));

        runTest(updateExpectedWithUsername(expected, null, customerUsername), customerUsernameEntities, date, null, customerUsername);
    }

    private void testCustomerAndWorkerUsernameFilter(ResponseEntity expected, List<BookingEntity> returned, String date) {
        String customerUsername = randomAlphanumericString(20);
        String workerUsername = randomAlphanumericString(20);

        List<BookingEntity> usernameEntities = deepCopy(returned);
        usernameEntities.forEach(hoursEntity -> hoursEntity.setCustomerUsername(customerUsername));
        usernameEntities.forEach(hoursEntity -> hoursEntity.setWorkerUsername(workerUsername));

        BookingEntity customerUsernameEntity = randomEntityWithDate(randomInt(4), LocalDate.parse(date));
        customerUsernameEntity.setCustomerUsername(customerUsername);
        usernameEntities.add(customerUsernameEntity);

        BookingEntity workerUsernameEntity = randomEntityWithDate(randomInt(4), LocalDate.parse(date));
        workerUsernameEntity.setWorkerUsername(workerUsername);
        usernameEntities.add(workerUsernameEntity);

        usernameEntities.add(randomEntityWithDate(randomInt(4), LocalDate.parse(date)));

        runTest(updateExpectedWithUsername(expected, workerUsername, customerUsername), usernameEntities, date, workerUsername, customerUsername);
    }


    private void runTest(ResponseEntity expected, List<BookingEntity> returned, String date, String workerUsername, String customerUsername) {
        try {
            if (date != null) {
                LocalDate parsedDate = LocalDate.parse(date);
                when(mockedBookingRepository.findAllInRange(parsedDate.atStartOfDay(), parsedDate.atTime(23, 59))).thenReturn(returned);
            }
        } catch (DateTimeParseException e) {

        }
        ResponseEntity result = bookingServiceController.getDate(date, workerUsername, customerUsername);

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(expected.getStatusCode());
        assertThat(result.getBody()).isEqualTo(expected.getBody());
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
