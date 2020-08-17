package sept.major.hours.entity;

import lombok.*;
import sept.major.common.converter.StringToTimestampConverter;
import sept.major.common.entity.AbstractEntity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "hours")
public class HoursEntity implements AbstractEntity<String> {

    @Id
    @Setter(onMethod = @__(@Id))
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String hoursId;

    @NotBlank
    private String workerUsername;

    @NotBlank
    private String customerUsername;

    @NotBlank
    @Convert(converter = StringToTimestampConverter.class)
    private String startDateTime;

    @NotBlank
    @Convert(converter = StringToTimestampConverter.class)
    private String endDateTime;

    @Override
    public String getID() {
        return hoursId;
    }
}
