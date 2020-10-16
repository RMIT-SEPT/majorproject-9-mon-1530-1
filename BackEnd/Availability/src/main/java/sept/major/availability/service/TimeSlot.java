package sept.major.availability.service;

import lombok.*;

import java.time.LocalDateTime;

/**
 * a class for a single time slot whether the time is available or not.
 *
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class TimeSlot {
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private boolean available;
}
