package sept.major.bookings.controller.unit.get;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sept.major.bookings.controller.unit.UnitTestHelper;
import sept.major.bookings.entity.BookingEntity;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static sept.major.bookings.BookingsTestHelper.randomEntity;
import static sept.major.bookings.BookingsTestHelper.randomInt;

@SpringBootTest
class GetEndpointTests extends UnitTestHelper {

    @Test
    void valid() {
        int id = randomInt(4);

        BookingEntity expected = randomEntity();

        runTest(new ResponseEntity(expected, HttpStatus.OK),
                Optional.of(expected), id);
    }

    @Test
    void missingValue() {
        int id = randomInt(4);

        runTest(new ResponseEntity(String.format("No record with a identifier of %s was found", id), HttpStatus.NOT_FOUND),
                Optional.empty(), id);
    }

    private void runTest(ResponseEntity expected, Optional<BookingEntity> returned, Integer id) {
        when(mockedBookingRepository.findById(id)).thenReturn(returned);
        ResponseEntity result = bookingServiceController.getBooking(id.toString());

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(expected.getStatusCode());
        assertThat(result.getBody()).isEqualTo(expected.getBody());
    }

}
