package sept.major.availability.service;

import lombok.*;

import java.time.LocalDateTime;

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
