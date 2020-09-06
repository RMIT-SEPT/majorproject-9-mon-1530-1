package sept.major.bookings;

import org.junit.Ignore;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sept.major.bookings.entity.BookingEntity;
import org.mockito.Mock.*;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
public class PostEndpointTests extends BookingServiceTestHelper {

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

    @Ignore
    @Test
    void existing() {
        Map<String, String> input = randomEntityMap();
        BookingEntity inputEntity = createBookingEntity(input);

        when(mockedBookingRepository.findById(inputEntity.getBookingId())).thenReturn(Optional.of(inputEntity));
        when(mockedBookingRepository.save(inputEntity)).thenReturn(inputEntity);

        ResponseEntity result = bookingServiceController.createBooking(input);

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(result.getBody()).isEqualTo("Failed to create entity because an entity with it's identifier already exists");
    }

    @Test
    void missingCustomerId() {
        String bookingId = randomAlphanumericString(5);
        String workerId = randomAlphanumericString(5);
        String startTime = LocalDateTime.now().toString();
        String endTime = LocalDateTime.now().plusHours(4).toString();

        Map<String, String> input = new HashMap<String, String>() {{
            put("bookingId", bookingId);
            put("workerId", workerId);
            put("startDateTime", startTime);
            put("endDateTime", endTime);
        }};

        ResponseEntity result = bookingServiceController.createBooking(input);

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void missingWorkerId() {
        String bookingId = randomAlphanumericString(5);
        String customerId = randomAlphanumericString(5);
        String startTime = LocalDateTime.now().toString();
        String endTime = LocalDateTime.now().plusHours(4).toString();

        Map<String, String> input = new HashMap<String, String>() {{
            put("bookingId", bookingId);
            put("customerId", customerId);
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
        String workerId = randomAlphanumericString(5);
        String customerId = randomAlphanumericString(5);
        String endTime = LocalDateTime.now().plusHours(4).toString();

        Map<String, String> input = new HashMap<String, String>() {{
            put("bookingId", bookingId);
            put("workerId", workerId);
            put("customerId", customerId);
            put("endDateTime", endTime);
        }};

        ResponseEntity result = bookingServiceController.createBooking(input);

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void missingEndTime() {
        String bookingId = randomAlphanumericString(5);
        String workerId = randomAlphanumericString(5);
        String customerId = randomAlphanumericString(5);
        String startTime = LocalDateTime.now().toString();

        Map<String, String> input = new HashMap<String, String>() {{
            put("bookingId", bookingId);
            put("workerId", workerId);
            put("customerId", customerId);
            put("startDateTime", startTime);
        }};

        ResponseEntity result = bookingServiceController.createBooking(input);

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}
