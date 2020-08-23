package sept.major.availability.entity;

import lombok.*;
import sept.major.common.converter.StringToTimestampConverter;
import sept.major.common.entity.AbstractEntity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Table(name = "bookings")
@Entity
public class BookingEntity implements AbstractEntity<String> { //TODO This class is a duplicate and should move to the common project instead 

    @Id
    @Setter(onMethod = @__(@Id))
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String bookingId;

    @NotBlank
    private String workerId;
    @NotBlank
    private String customerId;

    @NotBlank
    @Convert(converter = StringToTimestampConverter.class)
    private String startTime;

    @NotBlank
    @Convert(converter = StringToTimestampConverter.class)
    private String endTime;

    @Override
    public String getID() {
        return bookingId;
    }
}
