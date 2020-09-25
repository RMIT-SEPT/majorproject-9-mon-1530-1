package sept.major.bookings.controller.unit.get;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sept.major.bookings.entity.BookingEntity;
import sept.major.common.response.ValidationError;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static sept.major.bookings.BookingsTestHelper.*;

@SpringBootTest
class GetOnDateEndpointTests extends GetEndpointUnitTestHelper {

    @Test
    @DisplayName("Provided date is valid and a record is retrieved")
    void dateValid() {
        LocalDate date = pastDate(1, 2, 3);

        BookingEntity expected = randomEntityWithDate(randomInt(4), date);

        testWithUsernameFilters(new ResponseEntity(Arrays.asList(expected), HttpStatus.OK),
                Arrays.asList(expected), date.toString());
    }

    @Test
    @DisplayName("Provided date is a random string")
    void invalidDate() {
        testWithUsernameFilters(new ResponseEntity(new ValidationError("date", bookingServiceController.INCORRECT_DATE_FORMAT_ERROR_MESSAGE), HttpStatus.BAD_REQUEST),
                null, randomAlphanumericString(10));
    }

    @Test
    @DisplayName("Provided date is valid but no record could be found")
    void dateMissingResult() {

        testWithUsernameFilters(new ResponseEntity(new AbstractMap.SimpleEntry<>("message", "No records within provided bounds were found"), HttpStatus.NOT_FOUND),
                Arrays.asList(), LocalDate.now().toString());
    }

    private void testWithUsernameFilters(ResponseEntity expected, List<BookingEntity> returned, String date) {
        try {
            LocalDate localDate = LocalDate.parse(date);
            super.testWithUsernameFilters(expected, returned, localDate.atStartOfDay().toString(), localDate.atTime(23, 59, 59).toString());
        } catch (DateTimeParseException e) {
            super.testWithUsernameFilters(expected, returned, date, date);
        }


    }

    @Test
    @DisplayName("Invalid usernames provided")
    void invalidUsernameProvided() {
        ResponseEntity expected = new ResponseEntity(new ValidationError("workerUsername", "must be a valid username"), HttpStatus.BAD_REQUEST);

        String startDate = LocalDateTime.now().toString();
        String endDate = LocalDateTime.now().toString();

        runTest(expected, Collections.emptyList(), startDate, endDate, "null", null);
        runTest(expected, Collections.emptyList(), startDate, endDate, "", null);
        runTest(expected, Collections.emptyList(), startDate, endDate, "  ", null);
        runTest(expected, Collections.emptyList(), startDate, endDate, "\t", null);
        runTest(expected, Collections.emptyList(), startDate, endDate, "   \t   ", null);

        expected = new ResponseEntity(new ValidationError("customerUsername", "must be a valid username"), HttpStatus.BAD_REQUEST);

        runTest(expected, Collections.emptyList(), startDate, endDate, null, "null");
        runTest(expected, Collections.emptyList(), startDate, endDate, null, "");
        runTest(expected, Collections.emptyList(), startDate, endDate, null, "  ");
        runTest(expected, Collections.emptyList(), startDate, endDate, null, "\t");
        runTest(expected, Collections.emptyList(), startDate, endDate, null, "   \t  ");
    }


    @Override
    public void runTest(ResponseEntity expected, List<BookingEntity> returned, String startDate, String endDate, String workerUsername, String customerUsername) {
        /*
            The super method requires start date and end date but were we only need date.
            The startDate should always be the date provided in this tests but after being converted to a LocalDateTime and back to a string.
            Here we parse the string and return it to a LocalDate. It is an unfortunate process that we need.
         */
        LocalDate date = null;
        try {
            date = LocalDateTime.parse(startDate).toLocalDate();
            when(mockedBookingRepository.findAllInRange(date.atStartOfDay(), date.atTime(23, 59, 59))).thenReturn(returned);
        } catch (DateTimeParseException e) {
        } // Some tests may want to make an invalid date so we ignore this exception


        // If the date is null then that means we couldn't converted startDate to a LocalDateTime.
        // The likely cause of this is the test testing an invalid date so we provided the invalid date to the controller.
        ResponseEntity result = bookingServiceController.getDate(date == null ? startDate : date.toString(), workerUsername, customerUsername);

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(expected.getStatusCode());
        assertThat(result.getBody()).isEqualTo(expected.getBody());
    }

}
