package sept.major.availability.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import sept.major.availability.entity.AvailabilityEntity;
import sept.major.availability.entity.BookingEntity;
import sept.major.availability.entity.HoursEntity;

@Service
public class AvailabilityService {

	/**
	 * Gets list of hours and bookings and derive the availabilities by overlapping the two.
	 * @param hours
	 * @param bookings
	 * @return
	 */
	public List<AvailabilityEntity> checkAllAvailabilities(List<HoursEntity> hours, List<BookingEntity> bookings) {
		List<AvailabilityEntity> allAvailabilities = new ArrayList<>();

		for (HoursEntity hour : hours) {
			List<AvailabilityEntity> availabilities = new ArrayList<>();
			AvailabilityEntity availability = new AvailabilityEntity("", hour.getWorkerUsername(), hour.getCustomerUsername(), hour.getStartDateTime(),
					hour.getEndDateTime());
			availabilities.add(availability);
			for (BookingEntity booking : bookings) {
				overlap(availabilities, booking);
			}
			allAvailabilities.addAll(availabilities);
		}

		return allAvailabilities;
	}
	
	/**
	 * Overlaps the availabilities with the booking 
	 * @param availabilities
	 * @param booking
	 */
	private void overlap(List<AvailabilityEntity> availabilities, BookingEntity booking) {
		for (int i = 0; i< availabilities.size(); ++i) {
			AvailabilityEntity availability = availabilities.get(i);
			
			//if no overlap then no change
			if (availability.getStartDateTime().isAfter(booking.getEndTime()) || availability.getEndDateTime().isBefore(booking.getStartTime())) {
				
				//if partial overlap then adjust
			} else if (availability.getStartDateTime().isAfter(booking.getStartTime()) && availability.getEndDateTime().isAfter(booking.getStartTime())) {
				availability.setStartDateTime(booking.getEndTime());
				
				//if partial overlap then adjust
			} else if (availability.getStartDateTime().isBefore(booking.getStartTime()) && availability.getEndDateTime().isBefore(booking.getStartTime())) {
				availability.setEndDateTime(booking.getStartTime());
				
				//if availability is within booking then remove
			} else if ((availability.getStartDateTime().isEqual(booking.getStartTime()) || availability.getStartDateTime().isAfter(booking.getStartTime()))
					&& (availability.getEndDateTime().isEqual(booking.getEndTime()) || availability.getStartDateTime().isBefore(booking.getStartTime()))) {
				availabilities.remove(availability);
				
				// else, booking inside Availability, then split
			} else {  //TODO not sure if the 
				//split
				availability.setEndDateTime(booking.getStartTime());
				availabilities.add(new AvailabilityEntity("", availability.getWorkerUsername(), availability.getCustomerUsername(), booking.getEndTime(), availability.getEndDateTime()));
			}
		}
	}
	
}
