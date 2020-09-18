package sept.major.bookings.controller.unit.get;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sept.major.bookings.entity.BookingEntity;
import sept.major.common.response.ValidationError;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static sept.major.bookings.BookingsTestHelper.randomEntity;

@SpringBootTest
public class GetAllEndpointTests extends GetEndpointUnitTestHelper {

    @Test
    @DisplayName("Successfully retrieve one record")
    void valid() {
        List<BookingEntity> expected = Arrays.asList(
                randomEntity(),
                randomEntity(),
                randomEntity()
        );

        testWithUsernameFilters(new ResponseEntity(expected, HttpStatus.OK),
                expected);
    }


    @Test
    @DisplayName("No record found")
    void missingResult() {
        testWithUsernameFilters(new ResponseEntity("No records within provided bounds were found", HttpStatus.NOT_FOUND),
                Arrays.asList());
    }


    @Test
    @DisplayName("Successfully retrieve many records")
    void retrieveMany() {
        List<BookingEntity> bookingEntities = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            bookingEntities.add(randomEntity());
        }
        testWithUsernameFilters(new ResponseEntity(bookingEntities, HttpStatus.OK), bookingEntities);
    }

    protected void testWithUsernameFilters(ResponseEntity expected, List<BookingEntity> returned) {
        super.testWithUsernameFilters(expected, returned, null, null);
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
    protected void runTest(ResponseEntity expected, List<BookingEntity> returned, String startDate, String endDate, String workerUsername, String customerUsername) {
        when(mockedBookingRepository.findAll()).thenReturn(returned);
        ResponseEntity result = bookingServiceController.getAllBookings(workerUsername, customerUsername);

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(expected.getStatusCode());
        assertThat(result.getBody()).isEqualTo(expected.getBody());
    }


}
