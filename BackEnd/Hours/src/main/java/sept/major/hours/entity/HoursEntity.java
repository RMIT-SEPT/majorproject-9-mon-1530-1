package sept.major.hours.entity;

import lombok.*;
import sept.major.common.entity.AbstractEntity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "hours", schema = "hours")
public class HoursEntity implements AbstractEntity<String> {

    @Id
    @Setter(onMethod = @__(@Id))
    private String hoursId;

    private String workerUsername;
    private String createdBy;
    private String date;
    private String startTime;
    private String endTime;

    @Override
    public String getID() {
        return hoursId;
    }
}
