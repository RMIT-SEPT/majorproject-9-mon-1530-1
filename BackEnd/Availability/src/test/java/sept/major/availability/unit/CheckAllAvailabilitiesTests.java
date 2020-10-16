package sept.major.availability.unit;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import sept.major.availability.entity.AvailabilityEntity;
import sept.major.availability.entity.BookingResponse;
import sept.major.availability.entity.HoursResponse;
import sept.major.availability.service.AvailabilityPair;
import sept.major.availability.service.AvailabilityService;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class CheckAllAvailabilitiesTests {

    @Autowired
    private AvailabilityService availabilityService;

    @Test
    @DisplayName("No hours and no bookings")
    void checkAllAvailabilities_empty() {

        List<HoursResponse> hours = new ArrayList<>();
        List<BookingResponse> bookings = new ArrayList<>();

        AvailabilityPair result = availabilityService.checkAllAvailabilities(hours, bookings);

        assertThat(result.getAvailabilities()).isNotNull();
        assertThat(result.getAvailabilities()).isEmpty();

        assertThat(result.getBookings()).isNotNull();
        assertThat(result.getBookings()).isEmpty();
    }

    @Test
    @DisplayName("No hours and just bookings")
    void checkAllAvailabilities_bookingsOnly() {
        List<HoursResponse> hours = new ArrayList<>();
        List<BookingResponse> bookings = new ArrayList<>();

        LocalDateTime now = LocalDateTime.now();

        bookings.add(new BookingResponse(null, "", "", now, now.plus(5, ChronoUnit.HOURS)));
        bookings.add(new BookingResponse(null, "", "", now.plus(10, ChronoUnit.HOURS), now.plus(15, ChronoUnit.HOURS)));

        AvailabilityPair result = availabilityService.checkAllAvailabilities(hours, bookings);

        assertThat(result.getAvailabilities()).isNotNull();
        assertThat(result.getAvailabilities()).isEmpty();

        assertThat(result.getBookings()).isEqualTo(bookings);
    }


    @Test
    @DisplayName("Full overlap of hours and bookings")
    void checkAllAvailabilities_fullOverlap() {
        List<HoursResponse> hours = new ArrayList<>();
        List<BookingResponse> bookings = new ArrayList<>();

        LocalDateTime now = LocalDateTime.now();

        hours.add(new HoursResponse(1, "", "", now, now.plus(5, ChronoUnit.HOURS)));
        hours.add(new HoursResponse(2, "", "", now.plus(10, ChronoUnit.HOURS), now.plus(15, ChronoUnit.HOURS)));

        bookings.add(new BookingResponse(1, "", "", now, now.plus(5, ChronoUnit.HOURS)));
        bookings.add(new BookingResponse(2, "", "", now.plus(9, ChronoUnit.HOURS), now.plus(16, ChronoUnit.HOURS)));

        AvailabilityPair result = availabilityService.checkAllAvailabilities(hours, bookings);

        assertThat(result.getAvailabilities()).isNotNull();
        assertThat(result.getAvailabilities()).isEmpty();

        assertThat(result.getBookings()).isEqualTo(bookings);
    }

    @Test
    @DisplayName("Only hours")
    void checkAllAvailabilities_hoursOnly() {
        List<HoursResponse> hours = new ArrayList<>();
        List<BookingResponse> bookings = new ArrayList<>();

        LocalDateTime now = LocalDateTime.now();

        hours.add(new HoursResponse(1, "", "", now, now.plus(5, ChronoUnit.HOURS)));
        hours.add(new HoursResponse(2, "", "", now.plus(5, ChronoUnit.HOURS), now.plus(15, ChronoUnit.HOURS)));

        AvailabilityPair result = availabilityService.checkAllAvailabilities(hours, bookings);

        assertThat(result.getAvailabilities()).isNotNull();
        assertThat(result.getAvailabilities()).hasSize(2);

        assertThat(result.getBookings()).isEqualTo(bookings);
    }


    @Test
    @DisplayName("No overlap")
    void checkAllAvailabilities_noOverlap() {
        List<HoursResponse> hours = new ArrayList<>();
        List<BookingResponse> bookings = new ArrayList<>();

        LocalDateTime now = LocalDateTime.now();

        hours.add(new HoursResponse(1, "", "", now, now.plus(5, ChronoUnit.HOURS)));
        hours.add(new HoursResponse(2, "", "", now.plus(10, ChronoUnit.HOURS), now.plus(15, ChronoUnit.HOURS)));

        bookings.add(new BookingResponse(1, "", "", now.minus(10, ChronoUnit.HOURS), now.minus(5, ChronoUnit.HOURS)));
        bookings.add(new BookingResponse(2, "", "", now.plus(9, ChronoUnit.HOURS), now.plus(10, ChronoUnit.HOURS)));
        bookings.add(new BookingResponse(3, "", "", now.plus(15, ChronoUnit.HOURS), now.plus(16, ChronoUnit.HOURS)));
        bookings.add(new BookingResponse(4, "", "", now.plus(20, ChronoUnit.HOURS), now.plus(25, ChronoUnit.HOURS)));

        List<Pair<LocalDateTime, LocalDateTime>> expected = Arrays.asList(
                new ImmutablePair<>(now, now.plus(5, ChronoUnit.HOURS)),
                new ImmutablePair<>(now.plus(10, ChronoUnit.HOURS), now.plus(15, ChronoUnit.HOURS))
        );

        AvailabilityPair result = availabilityService.checkAllAvailabilities(hours, bookings);

        compareAvailabilities(result.getAvailabilities(), expected);
        assertThat(result.getBookings()).isEqualTo(bookings);

    }


    @Test
    @DisplayName("Partial overlap at the start")
    void checkAllAvailabilities_partialStartOverlap() {
        List<HoursResponse> hours = new ArrayList<>();
        List<BookingResponse> bookings = new ArrayList<>();

        LocalDateTime now = LocalDateTime.now();

        hours.add(new HoursResponse(1, "", "", now, now.plus(5, ChronoUnit.HOURS)));
        bookings.add(new BookingResponse(1, "", "", now.minus(3, ChronoUnit.HOURS), now.plus(2, ChronoUnit.HOURS)));


        List<Pair<LocalDateTime, LocalDateTime>> expected = Arrays.asList(
                new ImmutablePair<>(now.plus(2, ChronoUnit.HOURS), now.plus(5, ChronoUnit.HOURS))
        );

        AvailabilityPair result = availabilityService.checkAllAvailabilities(hours, bookings);

        compareAvailabilities(result.getAvailabilities(), expected);
        assertThat(result.getBookings()).isEqualTo(bookings);
    }

    @Test
    @DisplayName("Partial overlap at the end")
    void checkAllAvailabilities_partialEndOverlap() {
        List<HoursResponse> hours = new ArrayList<>();
        List<BookingResponse> bookings = new ArrayList<>();

        LocalDateTime base = LocalDateTime.of(2021, 01, 01, 0, 0, 0);

        hours.add(new HoursResponse(1, "", "", base, base.plus(5, ChronoUnit.HOURS)));
        bookings.add(new BookingResponse(1, "", "", base.plus(3, ChronoUnit.HOURS), base.plus(6, ChronoUnit.HOURS)));

        List<Pair<LocalDateTime, LocalDateTime>> expected = Arrays.asList(
                new ImmutablePair<>(base, base.plus(3, ChronoUnit.HOURS))
        );

        AvailabilityPair result = availabilityService.checkAllAvailabilities(hours, bookings);

        compareAvailabilities(result.getAvailabilities(), expected);
        assertThat(result.getBookings()).isEqualTo(bookings);
    }


    @Test
    @DisplayName("Booking with hours")
    void checkAllAvailabilities_bookingWithingHours() {
        List<HoursResponse> hours = new ArrayList<>();
        List<BookingResponse> bookings = new ArrayList<>();

        LocalDateTime base = LocalDateTime.of(2021, 01, 01, 0, 0, 0);

        hours.add(new HoursResponse(1, "", "", base, base.plus(5, ChronoUnit.HOURS)));

        bookings.add(new BookingResponse(1, "", "", base.plus(2, ChronoUnit.HOURS), base.plus(3, ChronoUnit.HOURS)));

        List<Pair<LocalDateTime, LocalDateTime>> expected = Arrays.asList(
                new ImmutablePair<>(base, base.plus(2, ChronoUnit.HOURS)),
                new ImmutablePair<>(base.plus(3, ChronoUnit.HOURS), base.plus(5, ChronoUnit.HOURS))
        );

        AvailabilityPair result = availabilityService.checkAllAvailabilities(hours, bookings);

        compareAvailabilities(result.getAvailabilities(), expected);
        assertThat(result.getBookings()).isEqualTo(bookings);
    }


    @Test
    @DisplayName("Complex")
    void checkAllAvailabilities_complex() {
        List<HoursResponse> hours = new ArrayList<>();
        List<BookingResponse> bookings = new ArrayList<>();

        LocalDateTime base = LocalDateTime.of(2021, 01, 01, 0, 0, 0);

        hours.add(new HoursResponse(1, "", "", base.plus(00, ChronoUnit.HOURS), base.plus(05, ChronoUnit.HOURS)));
        hours.add(new HoursResponse(2, "", "", base.plus(10, ChronoUnit.HOURS), base.plus(15, ChronoUnit.HOURS)));
        hours.add(new HoursResponse(3, "", "", base.plus(20, ChronoUnit.HOURS), base.plus(25, ChronoUnit.HOURS)));
        hours.add(new HoursResponse(4, "", "", base.plus(30, ChronoUnit.HOURS), base.plus(35, ChronoUnit.HOURS)));
        hours.add(new HoursResponse(5, "", "", base.plus(40, ChronoUnit.HOURS), base.plus(45, ChronoUnit.HOURS)));
        hours.add(new HoursResponse(6, "", "", base.plus(50, ChronoUnit.HOURS), base.plus(55, ChronoUnit.HOURS)));

        bookings.add(new BookingResponse(1, "", "", base.minus(10, ChronoUnit.HOURS), base.minus(5, ChronoUnit.HOURS)));    //no overlap
        bookings.add(new BookingResponse(1, "", "", base.minus(05, ChronoUnit.HOURS), base.minus(0, ChronoUnit.HOURS)));    //no overlap
        bookings.add(new BookingResponse(2, "", "", base.plus(10, ChronoUnit.HOURS), base.plus(15, ChronoUnit.HOURS)));        // full overlap
        bookings.add(new BookingResponse(3, "", "", base.plus(20, ChronoUnit.HOURS), base.plus(23, ChronoUnit.HOURS)));    // partial start overlap
        bookings.add(new BookingResponse(3, "", "", base.plus(32, ChronoUnit.HOURS), base.plus(36, ChronoUnit.HOURS)));    // partial end overlap
        bookings.add(new BookingResponse(3, "", "", base.plus(42, ChronoUnit.HOURS), base.plus(44, ChronoUnit.HOURS)));    // middle overlap
        bookings.add(new BookingResponse(3, "", "", base.plus(55, ChronoUnit.HOURS), base.plus(60, ChronoUnit.HOURS)));    // no overlap
        bookings.add(new BookingResponse(3, "", "", base.plus(60, ChronoUnit.HOURS), base.plus(65, ChronoUnit.HOURS)));    // no overlap


        List<Pair<LocalDateTime, LocalDateTime>> expected = Arrays.asList(
                new ImmutablePair<>(base, base.plus(5, ChronoUnit.HOURS)),
                new ImmutablePair<>(base.plus(23, ChronoUnit.HOURS), base.plus(25, ChronoUnit.HOURS)),
                new ImmutablePair<>(base.plus(30, ChronoUnit.HOURS), base.plus(32, ChronoUnit.HOURS)),
                new ImmutablePair<>(base.plus(40, ChronoUnit.HOURS), base.plus(42, ChronoUnit.HOURS)),
                new ImmutablePair<>(base.plus(44, ChronoUnit.HOURS), base.plus(45, ChronoUnit.HOURS)),
                new ImmutablePair<>(base.plus(50, ChronoUnit.HOURS), base.plus(55, ChronoUnit.HOURS))
        );


        AvailabilityPair result = availabilityService.checkAllAvailabilities(hours, bookings);

        compareAvailabilities(result.getAvailabilities(), expected);
        assertThat(result.getBookings()).isEqualTo(bookings);

    }

    private void compareAvailabilities(List<AvailabilityEntity> availabilities, List<Pair<LocalDateTime, LocalDateTime>> expected) {
        for (int i = 0; i < availabilities.size(); i++) {
            AvailabilityEntity availabilityEntity = availabilities.get(i);
            Pair expectedPair = expected.get(i);

            assertThat(availabilityEntity.getStartDateTime())
                    .as(String.format("Start date didn't match expected for availability number %s, %s, expected: %s", i, availabilityEntity, expectedPair))
                    .isEqualTo(expectedPair.getLeft());
            assertThat(availabilityEntity.getEndDateTime())
                    .as(String.format("End date didn't match expected for availability number %s, %s, expected: %s", i, availabilityEntity, expectedPair))
                    .isEqualTo(expectedPair.getRight());
        }
    }
}
