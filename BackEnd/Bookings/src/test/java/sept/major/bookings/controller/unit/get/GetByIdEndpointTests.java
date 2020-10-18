package sept.major.bookings.controller.unit.get;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sept.major.bookings.controller.unit.UnitTestHelper;
import sept.major.bookings.entity.BookingEntity;
import sept.major.common.response.ValidationError;

import java.util.AbstractMap;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static sept.major.bookings.BookingsTestHelper.randomEntity;
import static sept.major.bookings.BookingsTestHelper.randomInt;

@SpringBootTest
class GetByIdEndpointTests extends UnitTestHelper {

    @Test
    @DisplayName("Successfully retrieve record by id")
    void valid() {
        Integer id = randomInt(4);
        System.out.println(id);

        BookingEntity expected = randomEntity();

        runTest(new ResponseEntity(expected, HttpStatus.OK),
                Optional.of(expected), id.toString());
    }

    @Test
    @DisplayName("No record found with given id")
    void missingValue() {
        Integer id = randomInt(4);


        runTest(new ResponseEntity(new AbstractMap.SimpleEntry<>("message", String.format("No record with a identifier of %s was found", id)), HttpStatus.NOT_FOUND),
                Optional.empty(), id.toString());
    }

    @Test
    @DisplayName("Provided an id that isn't an integer")
    void invalidId() {
        String id = "foo";
        runTest(new ResponseEntity(new ValidationError("id", "must be an integer (whole number)"), HttpStatus.BAD_REQUEST),
                Optional.empty(), id);
    }

    private void runTest(ResponseEntity expected, Optional<BookingEntity> returned, String id) {
        try {
            when(mockedBookingRepository.findById(Integer.parseInt(id))).thenReturn(returned);
        } catch (NumberFormatException e) {
            // Some tests might want to see the interaction of an integer id so we ignore this exception.
        }

        ResponseEntity result = bookingServiceController.getBooking(id);

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(expected.getStatusCode());
        assertThat(result.getBody()).isEqualTo(expected.getBody());
    }

}
