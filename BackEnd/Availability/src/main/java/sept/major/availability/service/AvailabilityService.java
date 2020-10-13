package sept.major.availability.service;

import org.springframework.stereotype.Service;
import sept.major.availability.entity.AvailabilityEntity;
import sept.major.availability.entity.BookingResponse;
import sept.major.availability.entity.HoursResponse;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.*;

@Service
public class AvailabilityService {

	/**
	 * Gets list of hours and bookings and derive the availabilities by overlapping the two.
	 * @param hours
	 * @param bookings
	 * @return
	 */
	public AvailabilityPair checkAllAvailabilities(List<HoursResponse> hours, List<BookingResponse> bookings) {
		List<AvailabilityEntity> allAvailabilities = new ArrayList<>();
		List<BookingResponse> overlappedBookings = new ArrayList<>();

		for (HoursResponse hour : hours) {
			List<AvailabilityEntity> availabilities = new ArrayList<>();
			AvailabilityEntity availability = new AvailabilityEntity(hour.getHoursId(), hour.getWorkerUsername(), hour.getCreatorUsername(), hour.getStartDateTime(),
					hour.getEndDateTime());
			availabilities.add(availability);
			for (BookingResponse booking : bookings) {
				if (overlap(availabilities, booking)) {
					overlappedBookings.add(booking);
				}
			}
			allAvailabilities.addAll(availabilities);
		}

		return new AvailabilityPair(allAvailabilities, bookings);
	}

	/**
	 * Overlaps the availability hours with the booking to calculate the overall availabilities. It has considerations for 5 different scenarios about how to overlap
	 * the two.
	 * @param availabilities
	 * @param booking
	 */
	private boolean overlap(List<AvailabilityEntity> availabilities, BookingResponse booking) {
		for (int i = 0; i< availabilities.size(); ++i) {
			AvailabilityEntity availability = availabilities.get(i);

			//if no overlap then no change
			if (availability.getStartDateTime().isEqual(booking.getEndDateTime()) || availability.getStartDateTime().isAfter(booking.getEndDateTime())
					|| availability.getEndDateTime().isEqual(booking.getStartDateTime()) || availability.getEndDateTime().isBefore(booking.getStartDateTime())) {

				//if partial overlap in the end, then adjust
			} else if (booking.getStartDateTime().isAfter(availability.getStartDateTime()) && booking.getStartDateTime().isBefore(availability.getEndDateTime()) &&
					(availability.getEndDateTime().isEqual(booking.getEndDateTime()) || availability.getEndDateTime().isBefore(booking.getEndDateTime()))) {
				availability.setEndDateTime(booking.getStartDateTime());
				return true;

				//if partial overlap in the start, then adjust
			} else if ((availability.getStartDateTime().isEqual(booking.getStartDateTime()) || availability.getStartDateTime().isAfter(booking.getStartDateTime()))
					&& booking.getEndDateTime().isAfter(availability.getStartDateTime()) && booking.getEndDateTime().isBefore(availability.getEndDateTime())) {
				availability.setStartDateTime(booking.getEndDateTime());
				return true;

				//if availability is within booking then remove
			} else if ((availability.getStartDateTime().isEqual(booking.getStartDateTime()) || availability.getStartDateTime().isAfter(booking.getStartDateTime()))
					&& (availability.getEndDateTime().isEqual(booking.getEndDateTime()) || availability.getEndDateTime().isBefore(booking.getEndDateTime()))) {
				availabilities.remove(availability);
				return true;

				// else, booking inside Availability, then split
			} else {
				//split
				availabilities.add(new AvailabilityEntity(availability.getHoursId(), availability.getWorkerUsername(), availability.getCreatorUsername(), booking.getEndDateTime(),
						availability.getEndDateTime()));
				availability.setEndDateTime(booking.getStartDateTime());
				return true;
			}
		}

		return false;
	}

	/**
	 * gets availability pairs and calculate times lots for the given date range.
	 *
	 * @param providedDate
	 * @param endOfWeek
	 * @param availabilityPair
	 * @return
	 */
	public Map<String, Set<TimeSlot>> getTimeSlots(LocalDate providedDate, LocalDate endOfWeek, AvailabilityPair availabilityPair) {
		LocalDate startOfWeek = findStartOfWeek(providedDate);


		Map<String, Set<TimeSlot>> timeSlotsPerDate = new HashMap<>();

		for (LocalDate date = startOfWeek; date.isBefore(endOfWeek); date = date.plusDays(1)) {
			timeSlotsPerDate.put(date.toString(), new TreeSet<>(new TimeSlotComparitor()));
		}

		for (AvailabilityEntity availabilityEntity : availabilityPair.getAvailabilities()) {
			String date = availabilityEntity.getStartDateTime().toLocalDate().toString();

			Set<TimeSlot> timeSlotsForDate = timeSlotsPerDate.get(date);

			timeSlotsForDate.addAll(timeSlotsFromRange(availabilityEntity.getStartDateTime(), availabilityEntity.getEndDateTime(), true));
			timeSlotsPerDate.put(date, timeSlotsForDate);
		}

		for (BookingResponse booking : availabilityPair.getBookings()) {
			String date = booking.getStartDateTime().toLocalDate().toString();
			Set<TimeSlot> timeSlotsForDate = timeSlotsPerDate.get(date);

			timeSlotsForDate.addAll(timeSlotsFromRange(booking.getStartDateTime(), booking.getEndDateTime(), false));
			timeSlotsPerDate.put(date, timeSlotsForDate);
		}

		return timeSlotsPerDate;
	}


	/**
	 * internal method to return all the time slots for a given range
	 * @param startDateTime
	 * @param endDateTime
	 * @param isAvailable
	 * @return all the time slots for a given range
	 */
	private List<TimeSlot> timeSlotsFromRange(LocalDateTime startDateTime, LocalDateTime endDateTime, boolean isAvailable) {

		List<TimeSlot> timeSlots = new ArrayList<>();

		LocalDateTime date = startDateTime;
		while (date.isBefore(endDateTime)) {
			TimeSlot timeSlot = new TimeSlot();
			timeSlot.setAvailable(isAvailable);

			timeSlot.setStartDateTime(date);

			date = date.plusHours(1);
			if (date.isAfter(endDateTime)) {
				timeSlot.setEndDateTime(endDateTime);
			} else {
				timeSlot.setEndDateTime(date);
			}

			if (!timeSlot.getStartDateTime().equals(timeSlot.getEndDateTime())) {
				timeSlots.add(timeSlot);
			}
		}

		return timeSlots;
	}


	/**
	 * helper method to return the first day of the week for a given date
	 * @param date
	 * @return the first day of the week for a given date
	 */
	public LocalDate findStartOfWeek(LocalDate date) {
		TemporalField temporalField = WeekFields.of(Locale.ENGLISH).dayOfWeek();
		return date.with(temporalField, 2);
	}

	/**
	 * helper method to return the last day of the week for a given date
	 * @param date
	 * @return the last day of the week for a given date
	 */
	public LocalDate findEndOfWeek(LocalDate date) {
		TemporalField temporalField = WeekFields.of(Locale.ENGLISH).dayOfWeek();
		return date.with(temporalField, 7).plusDays(1);
	}

}
