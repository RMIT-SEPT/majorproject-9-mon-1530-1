package sept.major.availability.entity;

import lombok.*;

import java.time.LocalDateTime;

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

    public HoursResponse(Object hoursId, Object workerUsername, Object creatorUsername, Object startDateTime, Object endDateTime) {
        this.hoursId = hoursId == null ? null : Integer.parseInt(hoursId.toString());
        this.workerUsername = workerUsername == null ? null : workerUsername.toString();
        this.creatorUsername = creatorUsername == null ? null : creatorUsername.toString();
        this.startDateTime = startDateTime == null ? null : LocalDateTime.parse(startDateTime.toString());
        this.endDateTime = endDateTime == null ? null : LocalDateTime.parse(endDateTime.toString());
    }
}

