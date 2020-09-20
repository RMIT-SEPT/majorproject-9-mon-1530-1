package sept.major.bookings.controller.unit.get;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sept.major.bookings.controller.BookingServiceController;
import sept.major.bookings.entity.BookingEntity;
import sept.major.common.response.ValidationError;

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
class GetInRangeEndpointTests extends GetEndpointUnitTestHelper {

    @Test
    @DisplayName("Start date is random string and end date valid")
    void invalidStartDate() {
        testWithUsernameFilters(new ResponseEntity(new ValidationError("startDate", BookingServiceController.INCORRECT_DATE_TIME_FORMAT_ERROR_MESSAGE), HttpStatus.BAD_REQUEST),
                null, randomAlphanumericString(19), LocalDateTime.now().toString());
    }

    @Test
    @DisplayName("Start date valid and End date is random string")
    void invalidEndDate() {
        testWithUsernameFilters(new ResponseEntity(new ValidationError("endDate", BookingServiceController.INCORRECT_DATE_TIME_FORMAT_ERROR_MESSAGE), HttpStatus.BAD_REQUEST),
                null, LocalDateTime.now().toString(), randomAlphanumericString(19));
    }

    @Test
    @DisplayName("Start date and end date are random")
    void bothDatesInvalid() {
        testWithUsernameFilters(new ResponseEntity(new ValidationError("startDate", BookingServiceController.INCORRECT_DATE_TIME_FORMAT_ERROR_MESSAGE), HttpStatus.BAD_REQUEST),
                null, randomAlphanumericString(19), randomAlphanumericString(19));
    }

    @Test
    @DisplayName("Start date and end date are both valid")
    void bothDatesValid() {
        LocalDateTime startDate = pastDate(1, 2, 3).atStartOfDay();
        LocalDateTime endDate = pastDate(1, 2, 0).atStartOfDay();

        BookingEntity expected = randomEntityWithDateRange(randomInt(4), startDate, endDate);

        testWithUsernameFilters(new ResponseEntity(Arrays.asList(expected), HttpStatus.OK),
                Arrays.asList(expected), startDate.toString(), endDate.toString());
    }

    @Test
    @DisplayName("Start date and end date are both valid but no records found")
    void missingResult() {
        testWithUsernameFilters(new ResponseEntity(new AbstractMap.SimpleEntry<>("message", "No records within provided bounds were found"), HttpStatus.NOT_FOUND),
                Arrays.asList(), LocalDateTime.now().toString(), LocalDateTime.now().toString());
    }

    @Test
    @DisplayName("End date is before start date")
    void endDateBeforeStartDate() {
        testWithUsernameFilters(new ResponseEntity(new ValidationError("date range", "start date must be above the end date"), HttpStatus.BAD_REQUEST),
                Arrays.asList(), LocalDateTime.now().toString(), pastDate(0, 0, 1).atStartOfDay().toString());
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
        LocalDateTime startDateTime = null;
        try {
            startDateTime = LocalDateTime.parse(startDate);
        } catch (DateTimeParseException e) {
            // Some tests might want to see the interaction of the start date not being a valid date so we ignore this exception
        }

        LocalDateTime endDateTime = null;
        try {
            endDateTime = LocalDateTime.parse(endDate);
        } catch (DateTimeParseException e) {
            // Some tests might want to see the interaction of the start date not being a valid date so we ignore this exception
        }


        when(mockedBookingRepository.findAllInRange(startDateTime, endDateTime)).thenReturn(returned);
        ResponseEntity result = bookingServiceController.getRange(startDate, endDate, workerUsername, customerUsername);

        assertThat(result).isNotNull();
        assertThat(result.getBody()).isEqualTo(expected.getBody());
        assertThat(result.getStatusCode()).isEqualTo(expected.getStatusCode());

    }



}
