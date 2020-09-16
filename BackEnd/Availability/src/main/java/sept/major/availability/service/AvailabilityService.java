package sept.major.availability.service;

import org.springframework.stereotype.Service;
import sept.major.availability.entity.AvailabilityEntity;
import sept.major.availability.entity.BookingResponse;
import sept.major.availability.entity.HoursResponse;

import java.util.ArrayList;
import java.util.List;

@Service
public class AvailabilityService {

	/**
	 * Gets list of hours and bookings and derive the availabilities by overlapping the two.
	 * @param hours
	 * @param bookings
	 * @return
	 */
	public List<AvailabilityEntity> checkAllAvailabilities(List<HoursResponse> hours, List<BookingResponse> bookings) {
		List<AvailabilityEntity> allAvailabilities = new ArrayList<>();

		for (HoursResponse hour : hours) {
			List<AvailabilityEntity> availabilities = new ArrayList<>();
			AvailabilityEntity availability = new AvailabilityEntity(hour.getHoursId(), hour.getWorkerUsername(), hour.getCreatorUsername(), hour.getStartDateTime(),
					hour.getEndDateTime());
			availabilities.add(availability);
			for (BookingResponse booking : bookings) {
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
	private void overlap(List<AvailabilityEntity> availabilities, BookingResponse booking) {
		for (int i = 0; i< availabilities.size(); ++i) {
			AvailabilityEntity availability = availabilities.get(i);
			
			//if no overlap then no change
			if (availability.getStartDateTime().isEqual(booking.getEndDateTime()) || availability.getStartDateTime().isAfter(booking.getEndDateTime())
					|| availability.getEndDateTime().isEqual(booking.getStartDateTime()) || availability.getEndDateTime().isBefore(booking.getStartDateTime())) {
				
				//if partial overlap in the end, then adjust
			} else if (booking.getStartDateTime().isAfter(availability.getStartDateTime()) && booking.getStartDateTime().isBefore(availability.getEndDateTime()) &&
					(availability.getEndDateTime().isEqual(booking.getEndDateTime()) || availability.getEndDateTime().isBefore(booking.getEndDateTime()))) {
				availability.setEndDateTime(booking.getStartDateTime());
				
				//if partial overlap in the start, then adjust
			} else if ((availability.getStartDateTime().isEqual(booking.getStartDateTime()) || availability.getStartDateTime().isAfter(booking.getStartDateTime()))
					&& booking.getEndDateTime().isAfter(availability.getStartDateTime()) && booking.getEndDateTime().isBefore(availability.getEndDateTime())) {
				availability.setStartDateTime(booking.getEndDateTime());
				
				//if availability is within booking then remove
			} else if ((availability.getStartDateTime().isEqual(booking.getStartDateTime()) || availability.getStartDateTime().isAfter(booking.getStartDateTime()))
					&& (availability.getEndDateTime().isEqual(booking.getEndDateTime()) || availability.getEndDateTime().isBefore(booking.getEndDateTime()))) {
				availabilities.remove(availability);
				
				// else, booking inside Availability, then split
			} else {  //TODO not sure if the 
				//split
				availabilities.add(new AvailabilityEntity(availability.getHoursId(), availability.getWorkerUsername(), availability.getCustomerUsername(), booking.getEndDateTime(),
						availability.getEndDateTime()));
				availability.setEndDateTime(booking.getStartDateTime());
				
			}
		}
	}
	
}
