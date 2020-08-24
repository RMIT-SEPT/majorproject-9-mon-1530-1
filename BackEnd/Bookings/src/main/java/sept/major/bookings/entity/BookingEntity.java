package sept.major.bookings.entity;

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
@NoArgsConstructor
@EqualsAndHashCode
@Table(name = "bookings")
@Entity
public class BookingEntity implements AbstractEntity<Integer> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ReadOnly
    private Integer bookingId;

    @NotBlank
    private String workerUsername;
    @NotBlank
    private String customerUsername;

    @NotNull
    private LocalDateTime startDateTime;

    @NotNull
    private LocalDateTime endDateTime;

    public BookingEntity(String workerUsername, String customerUsername, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        this.workerUsername = workerUsername;
        this.customerUsername = customerUsername;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    @Override
    public Integer getID() {
        return bookingId;
    }
}
