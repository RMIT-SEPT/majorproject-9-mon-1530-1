package sept.major.hours.entity;

import lombok.*;
import sept.major.common.annotation.ReadOnly;
import sept.major.common.entity.AbstractEntity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@Table(name = "hours")
@Entity
public class HoursEntity implements AbstractEntity<Integer> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ReadOnly
    private Integer hoursId;

    @NotBlank
    private String workerUsername;

    @NotBlank
    private String creatorUsername;

    @NotNull
    private LocalDateTime startDateTime;

    @NotNull
    private LocalDateTime endDateTime;

    public HoursEntity(String workerUsername, String creatorUsername, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        this.workerUsername = workerUsername;
        this.creatorUsername = creatorUsername;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    @Override
    public Integer getID() {
        return hoursId;
    }
}
