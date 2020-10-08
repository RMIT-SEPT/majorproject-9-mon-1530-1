package sept.major.availability.entity;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class AvailabilityEntity {
    private Integer hoursId;
    private String workerUsername;
    private String customerUsername;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
}
