package sept.major.availability.entity;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class BookingResponse {
    private Integer bookingId;
    private String workerUsername;
    private String customerUsername;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
}