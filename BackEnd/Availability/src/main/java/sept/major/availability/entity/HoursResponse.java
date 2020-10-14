package sept.major.availability.entity;

import lombok.*;

import java.time.LocalDateTime;

/**
 * Hours response entity class used in availability service.
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class HoursResponse {
    private Integer hoursId;
    private String workerUsername;
    private String creatorUsername;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
}

