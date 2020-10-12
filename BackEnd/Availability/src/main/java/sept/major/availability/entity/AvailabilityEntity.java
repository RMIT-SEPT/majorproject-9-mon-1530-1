package sept.major.availability.entity;

import lombok.*;

import java.time.LocalDateTime;

/**
 * Availability Entity encapsulates entity details and unlike most other entity classes, is not backed by a database table. This is because
 * it doesn't need to persist data.
 * @author Abrar
 *
 */
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
