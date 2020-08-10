package sept.major.bookings.entity;

import lombok.*;
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
public class BookingEntity implements AbstractEntity<String> {

    @Id
    @Setter(onMethod = @__(@Id))
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String bookingId;

    @NotBlank
    private String workerId;
    @NotBlank
    private String customerId;
    @NotBlank
    private String startTime;
    @NotBlank
    private String endTime;

    @Override
    public String getID() {
        return bookingId;
    }
}
