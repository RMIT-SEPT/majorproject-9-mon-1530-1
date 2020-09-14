package sept.major.bookings.controller.unit.get;

import javafx.util.Pair;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sept.major.bookings.controller.unit.UnitTestHelper;
import sept.major.bookings.entity.BookingEntity;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static sept.major.bookings.BookingsTestHelper.*;

@SpringBootTest
class GetAllEndpointTests extends UnitTestHelper {

    @Test
    void valid() {
        List<BookingEntity> expected = Arrays.asList(
                randomEntity(),
                randomEntity(),
                randomEntity()
        );

        runTest(new ResponseEntity(expected, HttpStatus.OK),
                expected, null, null);
    }

    @Test
    void missingResult() {
        runTest(new ResponseEntity("No records within provided bounds were found", HttpStatus.NOT_FOUND),
                Arrays.asList(), null, null);
    }

    @Test
    void workerUsernameProvidedValid() {
        BookingEntity expected = randomEntity();
        String workerUsername = randomAlphanumericString(20);
        expected.setWorkerUsername(workerUsername);

        List<BookingEntity> returned = Arrays.asList(
                randomEntity(),
                expected,
                randomEntity()
        );

        runTest(new ResponseEntity(Arrays.asList(expected), HttpStatus.OK),
                returned, workerUsername, null);
    }

    @Test
    void workerUsernameProvidedMissing() {
        List<BookingEntity> returned = Arrays.asList(
                randomEntity(),
                randomEntity(),
                randomEntity()
        );

        runTest(new ResponseEntity("No records within provided bounds were found", HttpStatus.NOT_FOUND),
                returned, randomAlphanumericString(4), null);
    }

    @Test
    void customerUsernameProvidedValid() {
        BookingEntity expected = randomEntity();
        String customerUsername = randomAlphanumericString(20);
        expected.setCustomerUsername(customerUsername);

        List<BookingEntity> returned = Arrays.asList(
                randomEntity(),
                expected,
                randomEntity()
        );

        runTest(new ResponseEntity(Arrays.asList(expected), HttpStatus.OK),
                returned, null, customerUsername);
    }

    @Test
    void customerUsernameProvidedMissing() {
        List<BookingEntity> returned = Arrays.asList(
                randomEntity(),
                randomEntity(),
                randomEntity()
        );

        runTest(new ResponseEntity("No records within provided bounds were found", HttpStatus.NOT_FOUND),
                returned, null, randomAlphanumericString(4));
    }

    @Test
    void customerUsernameAndWorkerUsernameProvidedValid() {
        String customerUsername = randomAlphanumericString(20);
        String workerUsername = randomAlphanumericString(20);

        BookingEntity expected = randomEntity();
        expected.setCustomerUsername(customerUsername);
        expected.setWorkerUsername(workerUsername);

        BookingEntity entityOne = randomEntity();
        entityOne.setCustomerUsername(customerUsername);

        BookingEntity entityTwo = randomEntity();
        entityTwo.setWorkerUsername(workerUsername);

        List<BookingEntity> returned = Arrays.asList(
                entityOne, expected, entityTwo
        );

        runTest(new ResponseEntity(Arrays.asList(expected), HttpStatus.OK),
                returned, workerUsername, customerUsername);
    }

    @Test
    void customerUsernameAndWorkerUsernameProvidedMissing() {
        String customerUsername = randomAlphanumericString(20);
        String workerUsername = randomAlphanumericString(20);

        BookingEntity entityOne = randomEntity();
        entityOne.setCustomerUsername(customerUsername);

        BookingEntity entityTwo = randomEntity();
        entityTwo.setWorkerUsername(workerUsername);

        List<BookingEntity> returned = Arrays.asList(
                entityOne, randomEntity(), entityTwo
        );

        runTest(new ResponseEntity("No records within provided bounds were found", HttpStatus.NOT_FOUND),
                returned, workerUsername, customerUsername);
    }

    // TOOD DELETE THIS
    @Test
    void quickTest() {

        HashMap<String, Pair<LocalDateTime, LocalDateTime>> dates = new HashMap<>();

        Pair<LocalDateTime, LocalDateTime> pastDate = new Pair<>(pastDateTime(0, 0, 3),
                pastDateTime(0, 0, 3));
        dates.put("pastDate", pastDate);

        Pair<LocalDateTime, LocalDateTime> futureDate = new Pair<>(futureDateTime(0, 0, 1),
                futureDateTime(0, 0, 1));
        dates.put("futureDate", futureDate);

        Pair<LocalDateTime, LocalDateTime> correctDayPastTime = new Pair<>(LocalDateTime.of(pastDate(0, 0, 2), LocalTime.of(7, 30, 0)),
                LocalDateTime.of(pastDate(0, 0, 2), LocalTime.of(8, 30, 0)));
        dates.put("correctDayPastTime", correctDayPastTime);

        Pair<LocalDateTime, LocalDateTime> correctDayFutureTime = new Pair<>(LocalDateTime.of(pastDate(0, 0, 1), LocalTime.of(18, 30, 0)),
                LocalDateTime.of(pastDate(0, 0, 1), LocalTime.of(19, 30, 0)));
        dates.put("correctDayFutureTime", correctDayFutureTime);

        Pair<LocalDateTime, LocalDateTime> firstDateInRange = new Pair<>(LocalDateTime.of(pastDate(0, 0, 2), LocalTime.of(9, 30, 0)),
                LocalDateTime.of(pastDate(0, 0, 2), LocalTime.of(10, 15, 0)));
        dates.put("firstDateInRange", firstDateInRange);

        Pair<LocalDateTime, LocalDateTime> secondDateInRange = new Pair<>(LocalDateTime.of(pastDate(0, 0, 1), LocalTime.of(17, 0, 0)),
                LocalDateTime.of(pastDate(0, 0, 1), LocalTime.of(18, 0, 0)));
        dates.put("secondDateInRange", secondDateInRange);

        Pair<LocalDateTime, LocalDateTime> thirdDateInRange = new Pair<>(LocalDateTime.of(pastDate(0, 0, 1), LocalTime.of(1, 0)),
                LocalDateTime.of(pastDate(0, 0, 1), LocalTime.of(2, 0, 0)));
        dates.put("thirdDateInRange", thirdDateInRange);


        LocalDateTime lower = LocalDateTime.of(pastDate(0, 0, 2), LocalTime.of(9, 30, 0));
        LocalDateTime upper = LocalDateTime.of(pastDate(0, 0, 1), LocalTime.of(17, 30, 0));

        for (Map.Entry<String, Pair<LocalDateTime, LocalDateTime>> entry : dates.entrySet()) {
            LocalDateTime entryLower = entry.getValue().getKey();
            LocalDateTime entryHigher = entry.getValue().getValue();
            if (((lower.isBefore(entryLower) || lower.isEqual(entryLower)) && (upper.isBefore(entryLower) || upper.isEqual(entryLower)))
                    || (lower.isAfter(entryHigher))) {
                System.out.println(entry.getKey() + " Not in range");
            } else {
                System.out.println(entry.getKey() + " In range");
            }
        }
    }

    private void runTest(ResponseEntity expected, List<BookingEntity> returned, String workerUsername, String customerUsername) {
        when(mockedBookingRepository.findAll()).thenReturn(returned);
        ResponseEntity result = bookingServiceController.getAllBookings(workerUsername, customerUsername);

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(expected.getStatusCode());
        assertThat(result.getBody()).isEqualTo(expected.getBody());
    }


}
