package sept.major.availability.service.connector;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sept.major.availability.entity.BookingResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BookingServiceConnector extends ServiceConnector<BookingResponse> {

    @Getter
    @Value("${bookings.service.endpoint}")
    private String serviceEndpoint;

    @Override
    protected String getSecondUsernameLabel() {
        return "customerUsername";
    }

    @Override
    protected String getServiceName() {
        return "booking";
    }

    @Override
    protected List<BookingResponse> convertResultToList(List<Map> list) {
        return list.stream().map(map -> {
            Object hoursId = map.get("bookingId");
            Object workerUsername = map.get("workerUsername");
            Object customerUsername = map.get("customerUsername");
            Object startDateTime = map.get("startDateTime");
            Object endDateTime = map.get("endDateTime");

            return new BookingResponse(
                    hoursId == null ? null : Integer.parseInt(hoursId.toString()),
                    workerUsername == null ? null : workerUsername.toString(),
                    customerUsername == null ? null : customerUsername.toString(),
                    startDateTime == null ? null : LocalDateTime.parse(startDateTime.toString()),
                    endDateTime == null ? null : LocalDateTime.parse(endDateTime.toString()));
        }).collect(Collectors.toList());
    }
}
