package sept.major.availability.entity;

import java.time.LocalDateTime;

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
import sept.major.common.entity.AbstractEntity;

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
    private LocalDateTime startTime;

    @NotBlank
    private LocalDateTime endTime;

    @Override
    public String getID() {
        return bookingId;
    }
}
