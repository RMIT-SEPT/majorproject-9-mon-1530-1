package sept.major.availability.service;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import sept.major.availability.entity.AvailabilityEntity;
import sept.major.availability.entity.BookingEntity;
import sept.major.availability.entity.HoursEntity;

@SpringBootTest
public class AvailabilityServiceTest {

	@Autowired
    private AvailabilityService availabilityService;

	@Test //no hours and no bookings
	void checkAllAvailabilities_empty() {

		List<HoursEntity> hours = new ArrayList<>();
		List<BookingEntity> bookings = new ArrayList<>();

		List<AvailabilityEntity> availabilities = availabilityService.checkAllAvailabilities(hours, bookings);

		assertTrue(availabilities != null);
		assertTrue (availabilities.isEmpty());
	}

	@Test //no hours and just bookings
	void checkAllAvailabilities_bookingsOnly() {
		List<HoursEntity> hours = new ArrayList<>();
		List<BookingEntity> bookings = new ArrayList<>();
		
		LocalDateTime now = LocalDateTime.now();
		
		bookings.add(new BookingEntity("", "", "", now, now.plus(5, ChronoUnit.DAYS)));
		bookings.add(new BookingEntity("", "", "", now.plus(10, ChronoUnit.DAYS), now.plus(15, ChronoUnit.DAYS)));
		
		List<AvailabilityEntity> availabilities = availabilityService.checkAllAvailabilities(hours, bookings);

		assertTrue(availabilities != null);
		assertTrue (availabilities.isEmpty());
	}


	@Test //full overlap of hours and bookings
	void checkAllAvailabilities_fullOverlap() {
		List<HoursEntity> hours = new ArrayList<>();
		List<BookingEntity> bookings = new ArrayList<>();

		LocalDateTime now = LocalDateTime.now();

		hours.add(new HoursEntity("", "", "", now, now.plus(5, ChronoUnit.DAYS)));
		hours.add(new HoursEntity("", "", "", now.plus(5, ChronoUnit.DAYS), now.plus(15, ChronoUnit.DAYS)));

		bookings.add(new BookingEntity("", "", "", now, now.plus(5, ChronoUnit.DAYS)));
		bookings.add(new BookingEntity("", "", "", now.plus(5, ChronoUnit.DAYS), now.plus(15, ChronoUnit.DAYS)));
		
		List<AvailabilityEntity> availabilities = availabilityService.checkAllAvailabilities(hours, bookings);

		assertTrue(availabilities != null);
		assertTrue (availabilities.isEmpty());
	}
	
	@Test // only hours 
	void checkAllAvailabilities_hoursOnly() {
		List<HoursEntity> hours = new ArrayList<>();
		List<BookingEntity> bookings = new ArrayList<>();

		LocalDateTime now = LocalDateTime.now();

		hours.add(new HoursEntity("", "", "", now, now.plus(5, ChronoUnit.DAYS)));
		hours.add(new HoursEntity("", "", "", now.plus(5, ChronoUnit.DAYS), now.plus(15, ChronoUnit.DAYS)));
		
		List<AvailabilityEntity> availabilities = availabilityService.checkAllAvailabilities(hours, bookings);
		
		assertTrue(availabilities != null);
		assertFalse(availabilities.isEmpty());
		assertEquals("", availabilities.size(), 2);
	}
	
	@Test // only hours 
	void checkAllAvailabilities_partialStartOverlap() {
		List<HoursEntity> hours = new ArrayList<>();
		List<BookingEntity> bookings = new ArrayList<>();

		LocalDateTime now = LocalDateTime.now();

		hours.add(new HoursEntity("", "", "", now, now.plus(5, ChronoUnit.DAYS)));
		bookings.add(new BookingEntity("", "", "", now.minus(3, ChronoUnit.DAYS), now.plus(2, ChronoUnit.DAYS)));
		
		List<AvailabilityEntity> availabilities = availabilityService.checkAllAvailabilities(hours, bookings);
		
		assertTrue(availabilities != null);
		assertFalse(availabilities.isEmpty());
		assertEquals("", availabilities.size(), 1);
		assertEquals("", availabilities.get(0).getStartDateTime(),now.plus(2, ChronoUnit.DAYS));
		assertEquals("", availabilities.get(0).getEndDateTime(),now.plus(5, ChronoUnit.DAYS));
		
	}
}
