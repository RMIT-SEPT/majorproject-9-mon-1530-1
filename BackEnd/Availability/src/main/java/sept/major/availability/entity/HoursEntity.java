package sept.major.availability.entity;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import sept.major.common.converter.StringToTimestampConverter;
import sept.major.common.entity.AbstractEntity;

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
