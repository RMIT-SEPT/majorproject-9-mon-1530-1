package sept.major.bookings.controller.unit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sept.major.bookings.entity.BookingEntity;
import sept.major.common.response.ValidationError;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static sept.major.bookings.BookingsTestHelper.*;

@SpringBootTest
public class PostEndpointTests extends UnitTestHelper {

    @Test
    @DisplayName("Successfully create an entity")
    void valid() {
        Map<String, String> input = randomEntityMap();
        BookingEntity inputEntity = createBookingEntity(input);

        runTest(new ResponseEntity(inputEntity, HttpStatus.OK), input);
    }

    @Test
    @DisplayName("No customer username provided")
    void missingCustomerUsername() {
        String workerUsername = randomAlphanumericString(5);
        String startTime = LocalDateTime.now().toString();
        String endTime = LocalDateTime.now().plusHours(4).toString();

        Map<String, String> input = new HashMap<String, String>() {{
            put("workerUsername", workerUsername);
            put("startDateTime", startTime);
            put("endDateTime", endTime);
        }};

        runTest(new ResponseEntity(Arrays.asList(new ValidationError("customerUsername", "must not be blank")), HttpStatus.BAD_REQUEST), input);
    }

    @Test
    @DisplayName("No worker username provided")
    void missingWorkerUsername() {
        String customerUsername = randomAlphanumericString(5);
        String startTime = LocalDateTime.now().toString();
        String endTime = LocalDateTime.now().plusHours(4).toString();

        Map<String, String> input = new HashMap<String, String>() {{
            put("customerUsername", customerUsername);
            put("startDateTime", startTime);
            put("endDateTime", endTime);
        }};

        runTest(new ResponseEntity(Arrays.asList(new ValidationError("workerUsername", "must not be blank")), HttpStatus.BAD_REQUEST), input);
    }

    @Test
    @DisplayName("No start date provided")
    void missingStartTime() {
        String workerUsername = randomAlphanumericString(5);
        String customerUsername = randomAlphanumericString(5);
        String endTime = LocalDateTime.now().plusHours(4).toString();

        Map<String, String> input = new HashMap<String, String>() {{
            put("workerUsername", workerUsername);
            put("customerUsername", customerUsername);
            put("endDateTime", endTime);
        }};

        runTest(new ResponseEntity(Arrays.asList(new ValidationError("startDateTime", "must not be null")), HttpStatus.BAD_REQUEST), input);
    }

    @Test
    @DisplayName("No end start provided")
    void missingEndTime() {
        String workerUsername = randomAlphanumericString(5);
        String customerUsername = randomAlphanumericString(5);
        String startTime = LocalDateTime.now().toString();

        Map<String, String> input = new HashMap<String, String>() {{
            put("workerUsername", workerUsername);
            put("customerUsername", customerUsername);
            put("startDateTime", startTime);
        }};

        runTest(new ResponseEntity(Arrays.asList(new ValidationError("endDateTime", "must not be null")), HttpStatus.BAD_REQUEST), input);
    }

    @Test
    @DisplayName("Booking id provided")
    void bookingIdProvided() {
        Integer bookingId = randomInt(4);

        Map<String, String> input = randomEntityMap();
        input.put("bookingId", bookingId.toString());
        BookingEntity inputEntity = createBookingEntity(input);
        inputEntity.setBookingId(bookingId);

        runTest(new ResponseEntity(Arrays.asList(new ValidationError("bookingId", "value cannot be set because field is read only")), HttpStatus.BAD_REQUEST), input);
    }

    private void runTest(ResponseEntity expected, Map<String, String> input) {
        BookingEntity inputEntity = createBookingEntity(input);

        when(mockedBookingRepository.save(inputEntity)).thenReturn(inputEntity);

        ResponseEntity result = bookingServiceController.createBooking(input);
        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(expected.getStatusCode());
        assertThat(result.getBody()).isEqualTo(expected.getBody());
    }
}
