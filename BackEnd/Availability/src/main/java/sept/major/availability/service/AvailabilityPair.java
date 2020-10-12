package sept.major.availability.service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import sept.major.availability.entity.AvailabilityEntity;
import sept.major.availability.entity.BookingResponse;

import java.util.List;


/**
 * Encapsulates the availability entities and the booking responses in one class for convenience. 
 *
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class AvailabilityPair {
    private List<AvailabilityEntity> availabilities;
    private List<BookingResponse> bookings;

    @JsonIgnore
    public boolean isEmpty() {
        return availabilities.isEmpty();
    }
}
