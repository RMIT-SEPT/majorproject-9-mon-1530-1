package sept.major.bookings;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

public class DeleteEndpointTests extends BookingServiceTestHelper {

    @Test
    void valid() {
        String bookingId = randomAlphanumericString(20);

        Mockito.doNothing().when(mockedBookingRepository).deleteById(bookingId);

        ResponseEntity result = bookingServiceController.deleteBooking(bookingId);

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo("Record successfully deleted");
    }

    @Test
    void missing() {
        String bookingId = randomAlphanumericString(20);

        Mockito.doThrow(new EmptyResultDataAccessException(1)).when(mockedBookingRepository).deleteById(bookingId);

        ResponseEntity result = bookingServiceController.deleteBooking(bookingId);

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(result.getBody()).isEqualTo(String.format("No record with a identifier of %s was found", bookingId));
    }
}
