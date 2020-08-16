package sept.major.bookings;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sept.major.bookings.entity.BookingEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAmount;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class GetBulkEndpointTests extends BookingServiceTestHelper {

    @Test
    void validTimesWithinRange() {
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = LocalDateTime.now().plus(1, ChronoUnit.HOURS);

        List<BookingEntity> expected = Arrays.asList(
                new BookingEntity(randomAlphanumericString(20), randomAlphanumericString(20), randomAlphanumericString(20), startTime.toString(), endTime.toString()),
                new BookingEntity(randomAlphanumericString(20), randomAlphanumericString(20), randomAlphanumericString(20), startTime.toString(), endTime.toString()),
                new BookingEntity(randomAlphanumericString(20), randomAlphanumericString(20), randomAlphanumericString(20), startTime.toString(), endTime.toString())
        );

        when(mockedBookingRepository.findAllBetweenDates(startTime, endTime)).thenReturn(expected);

        ResponseEntity result = bookingServiceController.getRange(startTime, endTime);

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(expected);
    }

    @Test
    void validTimesOnDay() {
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = startTime.toLocalDate().atStartOfDay().plusDays(1).minusSeconds(1);

        List<BookingEntity> expected = Arrays.asList(
                new BookingEntity(randomAlphanumericString(20), randomAlphanumericString(20), randomAlphanumericString(20), startTime.toString(), endTime.toString()),
                new BookingEntity(randomAlphanumericString(20), randomAlphanumericString(20), randomAlphanumericString(20), startTime.toString(), endTime.toString()),
                new BookingEntity(randomAlphanumericString(20), randomAlphanumericString(20), randomAlphanumericString(20), startTime.toString(), endTime.toString())
        );

        when(mockedBookingRepository.findAllBetweenDates(startTime, endTime)).thenReturn(expected);

        ResponseEntity result = bookingServiceController.getRange(startTime);

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
                new BookingEntity(randomAlphanumericString(20), randomAlphanumericString(20), randomAlphanumericString(20), startTime.toString(), endTime.toString()),
                new BookingEntity(randomAlphanumericString(20), randomAlphanumericString(20), randomAlphanumericString(20), startTime.toString(), endTime.toString()),
                new BookingEntity(randomAlphanumericString(20), randomAlphanumericString(20), randomAlphanumericString(20), nextDayStartTime.toString(), nextDayEndTime.toString()),
                new BookingEntity(randomAlphanumericString(20), randomAlphanumericString(20), randomAlphanumericString(20), nextDayStartTime.toString(), nextDayEndTime.toString()),
                new BookingEntity(randomAlphanumericString(20), randomAlphanumericString(20), randomAlphanumericString(20), prevDayStartTime.toString(), prevDayEndTime.toString()),
                new BookingEntity(randomAlphanumericString(20), randomAlphanumericString(20), randomAlphanumericString(20), prevDayStartTime.toString(), prevDayEndTime.toString())
        );

        List<BookingEntity> expected = Arrays.asList(data.get(0), data.get(1));

        when(mockedBookingRepository.findAllBetweenDates(startTime, endTime)).thenReturn(expected);

        ResponseEntity result = bookingServiceController.getRange(startTime);

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(expected);
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
                new BookingEntity(randomAlphanumericString(20), randomAlphanumericString(20), randomAlphanumericString(20), startTime.toString(), endTime.toString()),
                new BookingEntity(randomAlphanumericString(20), randomAlphanumericString(20), randomAlphanumericString(20), startTime.toString(), endTime.toString()),
                new BookingEntity(randomAlphanumericString(20), randomAlphanumericString(20), randomAlphanumericString(20), nextDayStartTime.toString(), nextDayEndTime.toString()),
                new BookingEntity(randomAlphanumericString(20), randomAlphanumericString(20), randomAlphanumericString(20), nextDayStartTime.toString(), nextDayEndTime.toString()),
                new BookingEntity(randomAlphanumericString(20), randomAlphanumericString(20), randomAlphanumericString(20), prevDayStartTime.toString(), prevDayEndTime.toString()),
                new BookingEntity(randomAlphanumericString(20), randomAlphanumericString(20), randomAlphanumericString(20), prevDayStartTime.toString(), prevDayEndTime.toString())
        );

        when(mockedBookingRepository.findAllBetweenDates(endTime, startTime)).thenReturn(Arrays.asList());

        ResponseEntity result = bookingServiceController.getRange(endTime, startTime);

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(result.getBody()).isEqualTo(String.format("No records between %s and %s were found", endTime, startTime));
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
                new BookingEntity(randomAlphanumericString(20), randomAlphanumericString(20), randomAlphanumericString(20), nextDayStartTime.toString(), nextDayEndTime.toString()),
                new BookingEntity(randomAlphanumericString(20), randomAlphanumericString(20), randomAlphanumericString(20), nextDayStartTime.toString(), nextDayEndTime.toString()),
                new BookingEntity(randomAlphanumericString(20), randomAlphanumericString(20), randomAlphanumericString(20), prevDayStartTime.toString(), prevDayEndTime.toString()),
                new BookingEntity(randomAlphanumericString(20), randomAlphanumericString(20), randomAlphanumericString(20), prevDayStartTime.toString(), prevDayEndTime.toString())
        );

        when(mockedBookingRepository.findAllBetweenDates(startTime, endTime)).thenReturn(Arrays.asList());

        ResponseEntity result = bookingServiceController.getRange(startTime);

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(result.getBody()).isEqualTo(String.format("No records between %s and %s were found", startTime, endTime));
    }

    @Test
    void notDateTypeOnDate() {
        String startTime = "Today";
        String endTime = "Tomorrow";

        when(mockedBookingRepository.findAllBetweenDates(any(LocalDateTime.class), any(LocalDateTime.class))).thenThrow(new IllegalArgumentException());

        ResponseEntity result = bookingServiceController.getRange(LocalDate.now().atStartOfDay());

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(result.getBody()).isNull();
    }

    @Test
    void notDateTypeOnDateRange() {
        String startTime = "Today";
        String endTime = "Tomorrow";

        when(mockedBookingRepository.findAllBetweenDates(any(LocalDateTime.class), any(LocalDateTime.class))).thenThrow(new IllegalArgumentException());

        ResponseEntity result = bookingServiceController.getRange(LocalDate.now().atStartOfDay(), LocalDateTime.now().plusDays(2));

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(result.getBody()).isNull();
    }

    @Test
    void nullDate() {
        when(mockedBookingRepository.findAllBetweenDates(any(LocalDateTime.class), any(LocalDateTime.class))).thenThrow(new IllegalArgumentException());

        ResponseEntity result = bookingServiceController.getRange(null, null);

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(result.getBody()).isEqualTo("No start time was defined");
    }
}
