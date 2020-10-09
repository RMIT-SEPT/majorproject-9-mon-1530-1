package sept.major.availability.service;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AvailabilityServiceTest {

//	@Autowired
//    private AvailabilityService availabilityService;
//
//	@Test //no hours and no bookings
//	void checkAllAvailabilities_empty() {
//
//		List<HoursResponse> hours = new ArrayList<>();
//		List<BookingResponse> bookings = new ArrayList<>();
//
//		List<AvailabilityEntity> availabilities = availabilityService.checkAllAvailabilities(hours, bookings);
//
//		assertTrue(availabilities != null);
//		assertTrue (availabilities.isEmpty());
//	}
//
//	@Test //no hours and just bookings
//	void checkAllAvailabilities_bookingsOnly() {
//		List<HoursResponse> hours = new ArrayList<>();
//		List<BookingResponse> bookings = new ArrayList<>();
//
//		LocalDateTime now = LocalDateTime.now();
//
//		bookings.add(new BookingResponse(null, "", "", now, now.plus(5, ChronoUnit.HOURS)));
//		bookings.add(new BookingResponse(null, "", "", now.plus(10, ChronoUnit.HOURS), now.plus(15, ChronoUnit.HOURS)));
//
//		List<AvailabilityEntity> availabilities = availabilityService.checkAllAvailabilities(hours, bookings);
//
//		assertTrue(availabilities != null);
//		assertTrue (availabilities.isEmpty());
//	}
//
//
//	@Test //full overlap of hours and bookings
//	void checkAllAvailabilities_fullOverlap() {
//		List<HoursResponse> hours = new ArrayList<>();
//		List<BookingResponse> bookings = new ArrayList<>();
//
//		LocalDateTime now = LocalDateTime.now();
//
//		hours.add(new HoursResponse(1, "", "", now, now.plus(5, ChronoUnit.HOURS)));
//		hours.add(new HoursResponse(2, "", "", now.plus(10, ChronoUnit.HOURS), now.plus(15, ChronoUnit.HOURS)));
//
//		bookings.add(new BookingResponse(1, "", "", now, now.plus(5, ChronoUnit.HOURS)));
//		bookings.add(new BookingResponse(2, "", "", now.plus(9, ChronoUnit.HOURS), now.plus(16, ChronoUnit.HOURS)));
//
//		List<AvailabilityEntity> availabilities = availabilityService.checkAllAvailabilities(hours, bookings);
//
//		assertTrue(availabilities != null);
//		assertTrue (availabilities.isEmpty());
//	}
//
//	@Test // only hours
//	void checkAllAvailabilities_hoursOnly() {
//		List<HoursResponse> hours = new ArrayList<>();
//		List<BookingResponse> bookings = new ArrayList<>();
//
//		LocalDateTime now = LocalDateTime.now();
//
//		hours.add(new HoursResponse(1, "", "", now, now.plus(5, ChronoUnit.HOURS)));
//		hours.add(new HoursResponse(2, "", "", now.plus(5, ChronoUnit.HOURS), now.plus(15, ChronoUnit.HOURS)));
//
//		List<AvailabilityEntity> availabilities = availabilityService.checkAllAvailabilities(hours, bookings);
//
//		assertTrue(availabilities != null);
//		assertFalse(availabilities.isEmpty());
//		assertEquals("number of availabilities,", 2, availabilities.size());
//	}
//
//
//	@Test // no overlap
//	void checkAllAvailabilities_noOverlap() {
//		List<HoursResponse> hours = new ArrayList<>();
//		List<BookingResponse> bookings = new ArrayList<>();
//
//		LocalDateTime now = LocalDateTime.now();
//
//		hours.add(new HoursResponse(1, "", "", now, now.plus(5, ChronoUnit.HOURS)));
//		hours.add(new HoursResponse(2, "", "", now.plus(10, ChronoUnit.HOURS), now.plus(15, ChronoUnit.HOURS)));
//
//		bookings.add(new BookingResponse(1, "", "", now.minus(10, ChronoUnit.HOURS), now.minus(5, ChronoUnit.HOURS)));
//		bookings.add(new BookingResponse(2, "", "", now.plus(9, ChronoUnit.HOURS), now.plus(10, ChronoUnit.HOURS)));
//		bookings.add(new BookingResponse(3, "", "", now.plus(15, ChronoUnit.HOURS), now.plus(16, ChronoUnit.HOURS)));
//		bookings.add(new BookingResponse(4, "", "", now.plus(20, ChronoUnit.HOURS), now.plus(25, ChronoUnit.HOURS)));
//
//		List<AvailabilityEntity> availabilities = availabilityService.checkAllAvailabilities(hours, bookings);
//
//		assertTrue(availabilities != null);
//		assertFalse(availabilities.isEmpty());
//		assertEquals("number of availabilities,", 2, availabilities.size());
//		assertEquals("availability start time,", now, availabilities.get(0).getStartDateTime());
//		assertEquals("availability end time,", now.plus(5, ChronoUnit.HOURS), availabilities.get(0).getEndDateTime());
//		assertEquals("availability start time,", now.plus(10, ChronoUnit.HOURS), availabilities.get(1).getStartDateTime());
//		assertEquals("availability end time,", now.plus(15, ChronoUnit.HOURS), availabilities.get(1).getEndDateTime());
//
//	}
//
//	@Test // partial overlap at the start
//	void checkAllAvailabilities_partialStartOverlap() {
//		List<HoursResponse> hours = new ArrayList<>();
//		List<BookingResponse> bookings = new ArrayList<>();
//
//		LocalDateTime now = LocalDateTime.now();
//
//		hours.add(new HoursResponse(1, "", "", now, now.plus(5, ChronoUnit.HOURS)));
//		bookings.add(new BookingResponse(1, "", "", now.minus(3, ChronoUnit.HOURS), now.plus(2, ChronoUnit.HOURS)));
//
//		List<AvailabilityEntity> availabilities = availabilityService.checkAllAvailabilities(hours, bookings);
//
//		assertTrue(availabilities != null);
//		assertFalse(availabilities.isEmpty());
//		assertEquals("number of availabilities,", availabilities.size(), 1);
//		assertEquals("availability start time,", now.plus(2, ChronoUnit.HOURS), availabilities.get(0).getStartDateTime());
//		assertEquals("availability end time,", now.plus(5, ChronoUnit.HOURS), availabilities.get(0).getEndDateTime());
//	}
//
//	@Test // partial overlap at the end
//	void checkAllAvailabilities_partialEndOverlap() {
//		List<HoursResponse> hours = new ArrayList<>();
//		List<BookingResponse> bookings = new ArrayList<>();
//
//		LocalDateTime base = LocalDateTime.of(2021, 01, 01, 0, 0, 0);
//
//		hours.add(new HoursResponse(1, "", "", base, base.plus(5, ChronoUnit.HOURS)));
//		bookings.add(new BookingResponse(1, "", "", base.plus(3, ChronoUnit.HOURS), base.plus(6, ChronoUnit.HOURS)));
//
//		List<AvailabilityEntity> availabilities = availabilityService.checkAllAvailabilities(hours, bookings);
//
//		assertTrue(availabilities != null);
//		assertFalse(availabilities.isEmpty());
//		assertEquals("number of availabilities,", 1, availabilities.size());
//		assertEquals("availability start time,", base , availabilities.get(0).getStartDateTime());
//		assertEquals("availability end time,", base.plus(3, ChronoUnit.HOURS), availabilities.get(0).getEndDateTime());
//	}
//
//
//	@Test // partial overlap at the start
//	void checkAllAvailabilities_bookingWithingHours() {
//		List<HoursResponse> hours = new ArrayList<>();
//		List<BookingResponse> bookings = new ArrayList<>();
//
//		LocalDateTime base = LocalDateTime.of(2021, 01, 01, 0, 0, 0);
//
//		hours.add(new HoursResponse(1, "", "", base, base.plus(5, ChronoUnit.HOURS)));
//
//		bookings.add(new BookingResponse(1, "", "", base.plus(2, ChronoUnit.HOURS), base.plus(3, ChronoUnit.HOURS)));
//
//		List<AvailabilityEntity> availabilities = availabilityService.checkAllAvailabilities(hours, bookings);
//
//		assertTrue(availabilities != null);
//		assertFalse(availabilities.isEmpty());
//		assertEquals("number of availabilities,", availabilities.size(), 2);
//		assertEquals("availability start time,", base, availabilities.get(0).getStartDateTime());
//		assertEquals("availability end time," , base.plus(2, ChronoUnit.HOURS), availabilities.get(0).getEndDateTime());
//		assertEquals("availability start time,", base.plus(3, ChronoUnit.HOURS), availabilities.get(1).getStartDateTime());
//		assertEquals("availability end time,", base.plus(5, ChronoUnit.HOURS), availabilities.get(1).getEndDateTime());
//	}
//
//
//	@Test // partial overlap at the start
//	void checkAllAvailabilities_complex() {
//		List<HoursResponse> hours = new ArrayList<>();
//		List<BookingResponse> bookings = new ArrayList<>();
//
//		LocalDateTime base = LocalDateTime.of(2021, 01, 01, 0, 0, 0);
//
//		hours.add(new HoursResponse(1, "", "", base.plus(00, ChronoUnit.HOURS), base.plus(05, ChronoUnit.HOURS)));
//		hours.add(new HoursResponse(2, "", "", base.plus(10, ChronoUnit.HOURS), base.plus(15, ChronoUnit.HOURS)));
//		hours.add(new HoursResponse(3, "", "", base.plus(20, ChronoUnit.HOURS), base.plus(25, ChronoUnit.HOURS)));
//		hours.add(new HoursResponse(4, "", "", base.plus(30, ChronoUnit.HOURS), base.plus(35, ChronoUnit.HOURS)));
//		hours.add(new HoursResponse(5, "", "", base.plus(40, ChronoUnit.HOURS), base.plus(45, ChronoUnit.HOURS)));
//		hours.add(new HoursResponse(6, "", "", base.plus(50, ChronoUnit.HOURS), base.plus(55, ChronoUnit.HOURS)));
//
//		bookings.add(new BookingResponse(1, "", "", base.minus(10, ChronoUnit.HOURS), base.minus(5, ChronoUnit.HOURS)));    //no overlap
//		bookings.add(new BookingResponse(1, "", "", base.minus(05, ChronoUnit.HOURS), base.minus(0, ChronoUnit.HOURS)));    //no overlap
//		bookings.add(new BookingResponse(2, "", "", base.plus(10, ChronoUnit.HOURS), base.plus(15, ChronoUnit.HOURS)));        // full overlap
//		bookings.add(new BookingResponse(3, "", "", base.plus(20, ChronoUnit.HOURS), base.plus(23, ChronoUnit.HOURS)));    // partial start overlap
//		bookings.add(new BookingResponse(3, "", "", base.plus(32, ChronoUnit.HOURS), base.plus(36, ChronoUnit.HOURS)));    // partial end overlap
//		bookings.add(new BookingResponse(3, "", "", base.plus(42, ChronoUnit.HOURS), base.plus(44, ChronoUnit.HOURS)));    // middle overlap
//		bookings.add(new BookingResponse(3, "", "", base.plus(55, ChronoUnit.HOURS), base.plus(60, ChronoUnit.HOURS)));    // no overlap
//		bookings.add(new BookingResponse(3, "", "", base.plus(60, ChronoUnit.HOURS), base.plus(65, ChronoUnit.HOURS)));    // no overlap
//
//		List<AvailabilityEntity> availabilities = availabilityService.checkAllAvailabilities(hours, bookings);
//
//		assertTrue(availabilities != null);
//		assertFalse(availabilities.isEmpty());
//		assertEquals("number of availabilities,", availabilities.size(), 6);
//		assertEquals("availability1 start time,", base, availabilities.get(0).getStartDateTime());
//		assertEquals("availability1 end time," , base.plus(5, ChronoUnit.HOURS), availabilities.get(0).getEndDateTime());
//		assertEquals("availability2 start time,", base.plus(23, ChronoUnit.HOURS), availabilities.get(1).getStartDateTime());
//		assertEquals("availability2 end time," , base.plus(25, ChronoUnit.HOURS), availabilities.get(1).getEndDateTime());
//		assertEquals("availability3 start time,", base.plus(30, ChronoUnit.HOURS), availabilities.get(2).getStartDateTime());
//		assertEquals("availability3 end time," , base.plus(32, ChronoUnit.HOURS), availabilities.get(2).getEndDateTime());
//		assertEquals("availability4 start time,", base.plus(40, ChronoUnit.HOURS), availabilities.get(3).getStartDateTime());
//		assertEquals("availability4 end time," , base.plus(42, ChronoUnit.HOURS), availabilities.get(3).getEndDateTime());
//		assertEquals("availability5 start time,", base.plus(44, ChronoUnit.HOURS), availabilities.get(4).getStartDateTime());
//		assertEquals("availability5 end time," , base.plus(45, ChronoUnit.HOURS), availabilities.get(4).getEndDateTime());
//		assertEquals("availability6 start time,", base.plus(50, ChronoUnit.HOURS), availabilities.get(5).getStartDateTime());
//		assertEquals("availability6 end time," , base.plus(55, ChronoUnit.HOURS), availabilities.get(5).getEndDateTime());
//
//	}
}
