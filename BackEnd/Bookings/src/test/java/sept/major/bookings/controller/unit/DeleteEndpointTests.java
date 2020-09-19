package sept.major.bookings.controller.unit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sept.major.common.response.ValidationError;

import static org.assertj.core.api.Assertions.assertThat;
import static sept.major.bookings.BookingsTestHelper.randomAlphanumericString;
import static sept.major.bookings.BookingsTestHelper.randomInt;

public class DeleteEndpointTests extends UnitTestHelper {

    @Test
    @DisplayName("A entity is correctly deleted")
    void valid() {
        int bookingId = randomInt(4);

        Mockito.doNothing().when(mockedBookingRepository).deleteById(bookingId);

        ResponseEntity result = bookingServiceController.deleteBooking(Integer.toString(bookingId));

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo("Record successfully deleted");
    }

    @Test
    @DisplayName("No entity to delete")
    void missing() {
        int bookingId = randomInt(4);

        Mockito.doThrow(new EmptyResultDataAccessException(1)).when(mockedBookingRepository).deleteById(bookingId);

        ResponseEntity result = bookingServiceController.deleteBooking(Integer.toString(bookingId));

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(result.getBody()).isEqualTo(String.format("No record with a identifier of %s was found", bookingId));
    }

    @Test
    @DisplayName("Provided id was not an integer")
    void IdInvalid() {
        ResponseEntity result = bookingServiceController.deleteBooking(randomAlphanumericString(4));

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(result.getBody()).isEqualTo(new ValidationError("id", "must be an integer (whole number)"));
    }
}
