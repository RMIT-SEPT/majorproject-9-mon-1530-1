package sept.major.bookings.unit;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sept.major.bookings.entity.BookingEntity;
import sept.major.common.response.ValidationError;

import java.awt.print.Book;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static sept.major.bookings.BookingsTestHelper.randomAlphanumericString;

public class GetBulkEndpointTests extends UnitTestHelper {

    @Test
    void validTimesWithinRange() {
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = LocalDateTime.now().plus(1, ChronoUnit.HOURS);

        List<BookingEntity> expected = Arrays.asList(
                new BookingEntity(randomAlphanumericString(20), randomAlphanumericString(20), startTime, endTime),
                new BookingEntity(randomAlphanumericString(20), randomAlphanumericString(20), startTime, endTime),
                new BookingEntity(randomAlphanumericString(20), randomAlphanumericString(20), startTime, endTime)
        );

        when(mockedBookingRepository.findAllByStartDateTimeBetween(startTime, endTime)).thenReturn(expected);

        ResponseEntity result = bookingServiceController.getRange(startTime.toString(), endTime.toString(), null, null);

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(expected);
    }

    @Test
    void validTimesOnDay() {
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = startTime.toLocalDate().atStartOfDay().plusDays(1).minusSeconds(1);

        List<BookingEntity> expected = Arrays.asList(
                new BookingEntity(randomAlphanumericString(20), randomAlphanumericString(20), startTime, endTime),
                new BookingEntity(randomAlphanumericString(20), randomAlphanumericString(20), startTime, endTime),
                new BookingEntity(randomAlphanumericString(20), randomAlphanumericString(20), startTime, endTime)
        );

        when(mockedBookingRepository.findAllByStartDateTimeBetween(startTime.toLocalDate().atStartOfDay(), endTime.toLocalDate().atStartOfDay().plusDays(1).minusSeconds(1))).thenReturn(expected);

        ResponseEntity result = bookingServiceController.getDate(startTime.toLocalDate().toString(), null, null);

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(expected);
    }

    @Test
    void validDateOutOfRange() {
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = startTime.toLocalDate().atStartOfDay().plusDays(1).minusSeconds(1);
        LocalDateTime prevDayStartTime = startTime.toLocalDate().minusDays(2).atStartOfDay();
        LocalDateTime prevDayEndTime = prevDayStartTime.toLocalDate().plusDays(1).atStartOfDay().minusSeconds(1);
        LocalDateTime nextDayStartTime = startTime.toLocalDate().plusDays(2).atStartOfDay();
        LocalDateTime nextDayEndTime = nextDayStartTime.toLocalDate().atStartOfDay().plusDays(1).minusSeconds(1);

        List<BookingEntity> data = Arrays.asList(
                new BookingEntity(randomAlphanumericString(20), randomAlphanumericString(20), startTime, endTime),
                new BookingEntity(randomAlphanumericString(20), randomAlphanumericString(20), startTime, endTime),
                new BookingEntity(randomAlphanumericString(20), randomAlphanumericString(20), nextDayStartTime, nextDayEndTime),
                new BookingEntity(randomAlphanumericString(20), randomAlphanumericString(20), nextDayStartTime, nextDayEndTime),
                new BookingEntity(randomAlphanumericString(20), randomAlphanumericString(20), prevDayStartTime, prevDayEndTime),
                new BookingEntity(randomAlphanumericString(20), randomAlphanumericString(20), prevDayStartTime, prevDayEndTime)
        );

        List<BookingEntity> expected = Arrays.asList(data.get(0), data.get(1));

        when(mockedBookingRepository.findAllByStartDateTimeBetween(startTime.toLocalDate().atStartOfDay(), endTime.toLocalDate().atStartOfDay().plusDays(1).minusSeconds(1))).thenReturn(expected);

        ResponseEntity result = bookingServiceController.getDate(startTime.toLocalDate().toString(), null, null);

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(expected);
    }

    @Test
    void getAllBookings() {
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = startTime.toLocalDate().atStartOfDay().plusDays(1).minusSeconds(1);
        LocalDateTime prevDayStartTime = startTime.toLocalDate().minusDays(2).atStartOfDay();
        LocalDateTime prevDayEndTime = prevDayStartTime.toLocalDate().plusDays(1).atStartOfDay().minusSeconds(1);
        LocalDateTime nextDayStartTime = startTime.toLocalDate().plusDays(2).atStartOfDay();
        LocalDateTime nextDayEndTime = nextDayStartTime.toLocalDate().atStartOfDay().plusDays(1).minusSeconds(1);

        List<BookingEntity> data = Arrays.asList(
                new BookingEntity(randomAlphanumericString(20), randomAlphanumericString(20), startTime, endTime),
                new BookingEntity(randomAlphanumericString(20), randomAlphanumericString(20), startTime, endTime),
                new BookingEntity(randomAlphanumericString(20), randomAlphanumericString(20), nextDayStartTime, nextDayEndTime),
                new BookingEntity(randomAlphanumericString(20), randomAlphanumericString(20), nextDayStartTime, nextDayEndTime),
                new BookingEntity(randomAlphanumericString(20), randomAlphanumericString(20), prevDayStartTime, prevDayEndTime),
                new BookingEntity(randomAlphanumericString(20), randomAlphanumericString(20), prevDayStartTime, prevDayEndTime)
        );

        when(mockedBookingRepository.findAll()).thenReturn(data);

        ResponseEntity result = bookingServiceController.getAllBookings(null, null);

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(data);
    }

    @Test
    void getAllBookingsUser() {
        String workerUsername = randomAlphanumericString(20);
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = startTime.toLocalDate().atStartOfDay().plusDays(1).minusSeconds(1);
        LocalDateTime prevDayStartTime = startTime.toLocalDate().minusDays(2).atStartOfDay();
        LocalDateTime prevDayEndTime = prevDayStartTime.toLocalDate().plusDays(1).atStartOfDay().minusSeconds(1);
        LocalDateTime nextDayStartTime = startTime.toLocalDate().plusDays(2).atStartOfDay();
        LocalDateTime nextDayEndTime = nextDayStartTime.toLocalDate().atStartOfDay().plusDays(1).minusSeconds(1);

        List<BookingEntity> data = Arrays.asList(
                new BookingEntity(workerUsername, randomAlphanumericString(20), startTime, endTime),
                new BookingEntity(workerUsername, randomAlphanumericString(20), nextDayStartTime, nextDayEndTime),
                new BookingEntity(randomAlphanumericString(20), randomAlphanumericString(20), startTime, endTime),
                new BookingEntity(randomAlphanumericString(20), randomAlphanumericString(20), nextDayStartTime, nextDayEndTime),
                new BookingEntity(randomAlphanumericString(20), randomAlphanumericString(20), prevDayStartTime, prevDayEndTime),
                new BookingEntity(randomAlphanumericString(20), randomAlphanumericString(20), prevDayStartTime, prevDayEndTime)
        );

        List<BookingEntity> expected = Arrays.asList(data.get(0), data.get(1));

        when(mockedBookingRepository.findAll()).thenReturn(data);

        ResponseEntity result = bookingServiceController.getAllBookings(workerUsername, null);

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(expected);
    }

    @Test
    void getAllBookingsCustomer() {
        String customerUsername = randomAlphanumericString(20);
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = startTime.toLocalDate().atStartOfDay().plusDays(1).minusSeconds(1);
        LocalDateTime prevDayStartTime = startTime.toLocalDate().minusDays(2).atStartOfDay();
        LocalDateTime prevDayEndTime = prevDayStartTime.toLocalDate().plusDays(1).atStartOfDay().minusSeconds(1);
        LocalDateTime nextDayStartTime = startTime.toLocalDate().plusDays(2).atStartOfDay();
        LocalDateTime nextDayEndTime = nextDayStartTime.toLocalDate().atStartOfDay().plusDays(1).minusSeconds(1);

        List<BookingEntity> data = Arrays.asList(
                new BookingEntity(randomAlphanumericString(20), customerUsername, startTime, endTime),
                new BookingEntity(randomAlphanumericString(20), customerUsername, nextDayStartTime, nextDayEndTime),
                new BookingEntity(randomAlphanumericString(20), randomAlphanumericString(20), startTime, endTime),
                new BookingEntity(randomAlphanumericString(20), randomAlphanumericString(20), nextDayStartTime, nextDayEndTime),
                new BookingEntity(randomAlphanumericString(20), randomAlphanumericString(20), prevDayStartTime, prevDayEndTime),
                new BookingEntity(randomAlphanumericString(20), randomAlphanumericString(20), prevDayStartTime, prevDayEndTime)
        );

        List<BookingEntity> expected = Arrays.asList(data.get(0), data.get(1));

        when(mockedBookingRepository.findAll()).thenReturn(data);

        ResponseEntity result = bookingServiceController.getAllBookings(null, customerUsername);

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(expected);
    }

    @Test
    void getAllBookingsNoneCustomerExist() {
        String customerUsername = randomAlphanumericString(20);
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = startTime.toLocalDate().atStartOfDay().plusDays(1).minusSeconds(1);
        LocalDateTime prevDayStartTime = startTime.toLocalDate().minusDays(2).atStartOfDay();
        LocalDateTime prevDayEndTime = prevDayStartTime.toLocalDate().plusDays(1).atStartOfDay().minusSeconds(1);
        LocalDateTime nextDayStartTime = startTime.toLocalDate().plusDays(2).atStartOfDay();
        LocalDateTime nextDayEndTime = nextDayStartTime.toLocalDate().atStartOfDay().plusDays(1).minusSeconds(1);

        List<BookingEntity> data = Arrays.asList(
                new BookingEntity(randomAlphanumericString(20),randomAlphanumericString(20), startTime, endTime),
                new BookingEntity(randomAlphanumericString(20), randomAlphanumericString(20), startTime, endTime),
                new BookingEntity(randomAlphanumericString(20), randomAlphanumericString(20), nextDayStartTime, nextDayEndTime),
                new BookingEntity(randomAlphanumericString(20), randomAlphanumericString(20), nextDayStartTime, nextDayEndTime),
                new BookingEntity(randomAlphanumericString(20), randomAlphanumericString(20), prevDayStartTime, prevDayEndTime),
                new BookingEntity(randomAlphanumericString(20), randomAlphanumericString(20), prevDayStartTime, prevDayEndTime)
        );

        List<BookingEntity> expected = Arrays.asList(data.get(0), data.get(1));

        when(mockedBookingRepository.findAll()).thenReturn(data);

        ResponseEntity result = bookingServiceController.getAllBookings(null, customerUsername);

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(result.getBody()).isEqualTo("No records within provided bounds were found");
    }

    @Test
    void getAllBookingsNoneWorkerExist() {
        String workerUsername = randomAlphanumericString(20);
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = startTime.toLocalDate().atStartOfDay().plusDays(1).minusSeconds(1);
        LocalDateTime prevDayStartTime = startTime.toLocalDate().minusDays(2).atStartOfDay();
        LocalDateTime prevDayEndTime = prevDayStartTime.toLocalDate().plusDays(1).atStartOfDay().minusSeconds(1);
        LocalDateTime nextDayStartTime = startTime.toLocalDate().plusDays(2).atStartOfDay();
        LocalDateTime nextDayEndTime = nextDayStartTime.toLocalDate().atStartOfDay().plusDays(1).minusSeconds(1);

        List<BookingEntity> data = Arrays.asList(
                new BookingEntity(randomAlphanumericString(20),randomAlphanumericString(20), startTime, endTime),
                new BookingEntity(randomAlphanumericString(20), randomAlphanumericString(20), startTime, endTime),
                new BookingEntity(randomAlphanumericString(20), randomAlphanumericString(20), nextDayStartTime, nextDayEndTime),
                new BookingEntity(randomAlphanumericString(20), randomAlphanumericString(20), nextDayStartTime, nextDayEndTime),
                new BookingEntity(randomAlphanumericString(20), randomAlphanumericString(20), prevDayStartTime, prevDayEndTime),
                new BookingEntity(randomAlphanumericString(20), randomAlphanumericString(20), prevDayStartTime, prevDayEndTime)
        );

        List<BookingEntity> expected = Arrays.asList(data.get(0), data.get(1));

        when(mockedBookingRepository.findAll()).thenReturn(data);

        ResponseEntity result = bookingServiceController.getAllBookings(workerUsername,null);

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(result.getBody()).isEqualTo("No records within provided bounds were found");
    }

    @Test
    void startTimeAndEndTimeSwapped() {
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = startTime.toLocalDate().atStartOfDay().plusDays(1).minusSeconds(1);
        LocalDateTime prevDayStartTime = startTime.toLocalDate().minusDays(2).atStartOfDay();
        LocalDateTime prevDayEndTime = prevDayStartTime.toLocalDate().plusDays(1).atStartOfDay().minusSeconds(1);
        LocalDateTime nextDayStartTime = startTime.toLocalDate().plusDays(2).atStartOfDay();
        LocalDateTime nextDayEndTime = nextDayStartTime.toLocalDate().atStartOfDay().plusDays(1).minusSeconds(1);

        List<BookingEntity> data = Arrays.asList(
                new BookingEntity(randomAlphanumericString(20), randomAlphanumericString(20), startTime, endTime),
                new BookingEntity(randomAlphanumericString(20), randomAlphanumericString(20), startTime, endTime),
                new BookingEntity(randomAlphanumericString(20), randomAlphanumericString(20), nextDayStartTime, nextDayEndTime),
                new BookingEntity(randomAlphanumericString(20), randomAlphanumericString(20), nextDayStartTime, nextDayEndTime),
                new BookingEntity(randomAlphanumericString(20), randomAlphanumericString(20), prevDayStartTime, prevDayEndTime),
                new BookingEntity(randomAlphanumericString(20), randomAlphanumericString(20), prevDayStartTime, prevDayEndTime)
        );

        when(mockedBookingRepository.findAllByStartDateTimeBetween(endTime, startTime)).thenReturn(Arrays.asList());

        ResponseEntity result = bookingServiceController.getRange(endTime.toString(), startTime.toString(), null, null);

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(result.getBody()).isEqualTo(new ValidationError("date range", "start date must be above the end date"));
    }

    @Test
    void noRecordOnDate() {
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = startTime.toLocalDate().atStartOfDay().plusDays(1).minusSeconds(1);
        LocalDateTime prevDayStartTime = startTime.toLocalDate().minusDays(2).atStartOfDay();
        LocalDateTime prevDayEndTime = prevDayStartTime.toLocalDate().plusDays(1).atStartOfDay().minusSeconds(1);
        LocalDateTime nextDayStartTime = startTime.toLocalDate().plusDays(2).atStartOfDay();
        LocalDateTime nextDayEndTime = nextDayStartTime.toLocalDate().atStartOfDay().plusDays(1).minusSeconds(1);

        List<BookingEntity> data = Arrays.asList(
                new BookingEntity(randomAlphanumericString(20), randomAlphanumericString(20), nextDayStartTime, nextDayEndTime),
                new BookingEntity(randomAlphanumericString(20), randomAlphanumericString(20), nextDayStartTime, nextDayEndTime),
                new BookingEntity(randomAlphanumericString(20), randomAlphanumericString(20), prevDayStartTime, prevDayEndTime),
                new BookingEntity(randomAlphanumericString(20), randomAlphanumericString(20), prevDayStartTime, prevDayEndTime)
        );

        when(mockedBookingRepository.findAllByStartDateTimeBetween(startTime, endTime)).thenReturn(Arrays.asList());

        ResponseEntity result = bookingServiceController.getDate(startTime.toLocalDate().toString(), null, null);

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(result.getBody()).isEqualTo(String.format("No records between %s and %s were found", startTime.toLocalDate().atStartOfDay(), endTime.toLocalDate().atStartOfDay().plusDays(1).minusSeconds(1)));
    }

    @Test
    void notDateTypeOnDate() {
        String date = "Today";

        when(mockedBookingRepository.findAllByStartDateTimeBetween(any(LocalDateTime.class), any(LocalDateTime.class))).thenThrow(new IllegalArgumentException());

        ResponseEntity result = bookingServiceController.getDate(date, null, null);

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(result.getBody()).isEqualTo(new ValidationError("startDate", bookingServiceController.INCORRECT_DATE_FORMAT_ERROR_MESSAGE));
    }

    @Test
    void noRecordOnDateRange() {
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = startTime.toLocalDate().atStartOfDay().plusDays(1).minusSeconds(1);
        LocalDateTime prevDayStartTime = startTime.toLocalDate().minusDays(2).atStartOfDay();
        LocalDateTime prevDayEndTime = prevDayStartTime.toLocalDate().plusDays(1).atStartOfDay().minusSeconds(1);
        LocalDateTime nextDayStartTime = startTime.toLocalDate().plusDays(2).atStartOfDay();
        LocalDateTime nextDayEndTime = nextDayStartTime.toLocalDate().atStartOfDay().plusDays(1).minusSeconds(1);

        List<BookingEntity> data = Arrays.asList(
                new BookingEntity(randomAlphanumericString(20), randomAlphanumericString(20), nextDayStartTime, nextDayEndTime),
                new BookingEntity(randomAlphanumericString(20), randomAlphanumericString(20), nextDayStartTime, nextDayEndTime),
                new BookingEntity(randomAlphanumericString(20), randomAlphanumericString(20), prevDayStartTime, prevDayEndTime),
                new BookingEntity(randomAlphanumericString(20), randomAlphanumericString(20), prevDayStartTime, prevDayEndTime)
        );

        when(mockedBookingRepository.findAllByStartDateTimeBetween(startTime, endTime)).thenReturn(Arrays.asList());

        ResponseEntity result = bookingServiceController.getRange(startTime.toString(), endTime.toString(), null, null);

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(result.getBody()).isEqualTo(String.format("No records between %s and %s were found", startTime, endTime));
    }

    @Test
    void badStartDate() {
        String startTime = "Today";
        String endTime = "Tomorrow";

        //when(mockedBookingRepository.findAllByStartDateTimeBetween(any(LocalDateTime.class), any(LocalDateTime.class))).thenThrow(new IllegalArgumentException());

        ResponseEntity result = bookingServiceController.getRange(startTime, endTime, null, null);

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(result.getBody()).isEqualTo(new ValidationError("startDate", bookingServiceController.INCORRECT_DATE_TIME_FORMAT_ERROR_MESSAGE));
    }

    @Test
    void badEndDate() {
        String startTime = "Today";
        String endTime = "Tomorrow";

        //when(mockedBookingRepository.findAllByStartDateTimeBetween(any(LocalDateTime.class), any(LocalDateTime.class))).thenThrow(new IllegalArgumentException());

        ResponseEntity result = bookingServiceController.getRange(LocalDateTime.now().toString(), endTime, null, null);

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(result.getBody()).isEqualTo(new ValidationError("endDate", bookingServiceController.INCORRECT_DATE_TIME_FORMAT_ERROR_MESSAGE));
    }

    @Test
    void nullDate() {
        when(mockedBookingRepository.findAllByStartDateTimeBetween(any(LocalDateTime.class), any(LocalDateTime.class))).thenThrow(new IllegalArgumentException());

        ResponseEntity result = bookingServiceController.getDate(null, null, null);

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(result.getBody()).isEqualTo("No start time was defined");
    }

    @Test
    void getSpecificBooking() {
        String workerUsername = randomAlphanumericString(20);
        String customerUsername = randomAlphanumericString(20);
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = startTime.toLocalDate().atStartOfDay().plusDays(1).minusSeconds(1);

        BookingEntity entity = new BookingEntity(workerUsername, customerUsername, startTime, endTime);
        entity.setBookingId(1);
        when(mockedBookingRepository.findById(entity.getBookingId())).thenReturn(Optional.of(entity));

        ResponseEntity result = bookingServiceController.getBooking(entity.getBookingId().toString());

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(entity);
    }

    @Test
    void getSpecificBookingNotExist() {
        when(mockedBookingRepository.findById(1)).thenReturn(Optional.empty());

        ResponseEntity result = bookingServiceController.getBooking("1");

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(result.getBody()).isEqualTo(String.format("No booking entity for id %d",1));
    }

    @Test
    void getSpecificBookingBadDate() {
        String id = LocalDateTime.now().toString();
        ResponseEntity result = bookingServiceController.getBooking(id);

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(result.getBody()).isEqualTo(String.format("For input string: \"%s\"",id));
    }
}
