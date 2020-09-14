package sept.major.bookings.controller.unit;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sept.major.bookings.entity.BookingEntity;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static sept.major.bookings.BookingsTestHelper.*;

@SpringBootTest
public class PostEndpointTests extends UnitTestHelper {

    @Test
    void valid() {
        Map<String, String> input = randomEntityMap();
        BookingEntity inputEntity = createBookingEntity(input);

        when(mockedBookingRepository.findById(inputEntity.getBookingId())).thenReturn(Optional.empty());
        when(mockedBookingRepository.save(inputEntity)).thenReturn(inputEntity);

        ResponseEntity result = bookingServiceController.createBooking(input);
        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(inputEntity);
    }

    @Test
    void missingCustomerUsername() {
        String bookingId = randomAlphanumericString(5);
        String workerUsername = randomAlphanumericString(5);
        String startTime = LocalDateTime.now().toString();
        String endTime = LocalDateTime.now().plusHours(4).toString();

        Map<String, String> input = new HashMap<String, String>() {{
            put("bookingId", bookingId);
            put("workerUsername", workerUsername);
            put("startDateTime", startTime);
            put("endDateTime", endTime);
        }};

        ResponseEntity result = bookingServiceController.createBooking(input);

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void missingWorkerUsername() {
        String bookingId = randomAlphanumericString(5);
        String customerUsername = randomAlphanumericString(5);
        String startTime = LocalDateTime.now().toString();
        String endTime = LocalDateTime.now().plusHours(4).toString();

        Map<String, String> input = new HashMap<String, String>() {{
            put("bookingId", bookingId);
            put("customerUsername", customerUsername);
            put("startDateTime", startTime);
            put("endDateTime", endTime);
        }};

        ResponseEntity result = bookingServiceController.createBooking(input);

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void missingStartTime() {
        String bookingId = randomAlphanumericString(5);
        String workerUsername = randomAlphanumericString(5);
        String customerUsername = randomAlphanumericString(5);
        String endTime = LocalDateTime.now().plusHours(4).toString();

        Map<String, String> input = new HashMap<String, String>() {{
            put("bookingId", bookingId);
            put("workerUsername", workerUsername);
            put("customerUsername", customerUsername);
            put("endDateTime", endTime);
        }};

        ResponseEntity result = bookingServiceController.createBooking(input);

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void missingEndTime() {
        String bookingId = randomAlphanumericString(5);
        String workerUsername = randomAlphanumericString(5);
        String customerUsername = randomAlphanumericString(5);
        String startTime = LocalDateTime.now().toString();

        Map<String, String> input = new HashMap<String, String>() {{
            put("bookingId", bookingId);
            put("workerUsername", workerUsername);
            put("customerUsername", customerUsername);
            put("startDateTime", startTime);
        }};

        ResponseEntity result = bookingServiceController.createBooking(input);

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}
