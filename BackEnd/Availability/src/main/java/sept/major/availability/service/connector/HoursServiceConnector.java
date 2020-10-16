package sept.major.availability.service.connector;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sept.major.availability.entity.HoursResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Connector class for external Hours micro service to make it easier to use it and call it's methods. 
 *
 */
@Service
public class HoursServiceConnector extends ServiceConnector<HoursResponse> {

    @Getter
    @Value("${hours.service.endpoint}")
    private String serviceEndpoint;

    @Override
    protected String getSecondUsernameLabel() {
        return "creatorUsername";
    }

    @Override
    protected String getServiceName() {
        return "hours";
    }

    @Override
    protected List<HoursResponse> convertResultToList(List<Map> list) {
        return list.stream().map(map -> {
            Object hoursId = map.get("hoursId");
            Object workerUsername = map.get("workerUsername");
            Object creatorUsername = map.get("creatorUsername");
            Object startDateTime = map.get("startDateTime");
            Object endDateTime = map.get("endDateTime");

            return new HoursResponse(
                    hoursId == null ? null : Integer.parseInt(hoursId.toString()),
                    workerUsername == null ? null : workerUsername.toString(),
                    creatorUsername == null ? null : creatorUsername.toString(),
                    startDateTime == null ? null : LocalDateTime.parse(startDateTime.toString()),
                    endDateTime == null ? null : LocalDateTime.parse(endDateTime.toString()));
        }).collect(Collectors.toList());
    }
}
