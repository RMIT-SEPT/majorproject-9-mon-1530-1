package sept.major.bookings.entity;

import lombok.*;
import sept.major.common.entity.AbstractEntity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

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
    private String bookingId;

    private String workerId;
    private String customerId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @Override
    public String getID() {
        return bookingId;
    }
}
