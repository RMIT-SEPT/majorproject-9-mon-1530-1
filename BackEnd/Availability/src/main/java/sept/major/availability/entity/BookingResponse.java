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

    public BookingResponse(Object bookingId, Object workerUsername, Object customerUsername, Object startDateTime, Object endDateTime) {
        this.bookingId = bookingId == null ? null : Integer.parseInt(bookingId.toString());
        this.workerUsername = workerUsername == null ? null : workerUsername.toString();
        this.customerUsername = customerUsername == null ? null : customerUsername.toString();
        this.startDateTime = startDateTime == null ? null : LocalDateTime.parse(startDateTime.toString());
        this.endDateTime = endDateTime == null ? null : LocalDateTime.parse(endDateTime.toString());
    }
}