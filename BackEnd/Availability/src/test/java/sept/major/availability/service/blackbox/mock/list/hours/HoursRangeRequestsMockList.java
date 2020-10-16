package sept.major.availability.service.blackbox.mock.list.hours;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;
import org.mockserver.model.Parameter;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import sept.major.availability.service.blackbox.mock.MockList;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static sept.major.availability.service.blackbox.mock.MockServices.getHoursResponse;

@AllArgsConstructor
public class HoursRangeRequestsMockList extends MockList {

    private final HttpMethod method = HttpMethod.GET;

    @Getter
    private final String path = "/hours/range";

    @Getter
    private Integer port;

    @Getter
    private String host;

    public void createMocks() {
        validRangeWithBothUsernames();
        validRangeWithWorkerUsername();
        validRangeWithCreatorUsername();
        invalidRangeRequest();
        missingRangeRequest();
        validRangeNoUsernames();
    }

    @SneakyThrows
    private void validRangeNoUsernames() {
        generateValidResponseMock((Arrays.asList(
                getHoursResponse(
                        0,
                        "firstWorker",
                        "firstAdmin",
                        LocalDate.now().minusDays(1).atTime(9, 0, 0),
                        LocalDate.now().minusDays(1).atTime(17, 0, 0)),
                getHoursResponse(
                        0,
                        "firstWorker",
                        "secondAdmin",
                        LocalDate.now().minusDays(1).atTime(18, 0, 0),
                        LocalDate.now().minusDays(1).atTime(23, 0, 0)),
                getHoursResponse(
                        1,
                        "secondWorker",
                        "firstAdmin",
                        LocalDate.now().atTime(9, 0, 0),
                        LocalDate.now().atTime(17, 0, 0))
        )
        ), Arrays.asList(
                new Parameter("startDateTime", LocalDate.now().minusDays(1).atTime(9, 0, 0).toString()),
                new Parameter("endDateTime", LocalDate.now().atTime(17, 0, 0).toString())
        ));
    }

    @SneakyThrows
    private void validRangeWithWorkerUsername() {
        generateValidResponseMock((Arrays.asList(
                getHoursResponse(
                        0,
                        "firstWorker",
                        "firstAdmin",
                        LocalDate.now().minusDays(1).atTime(9, 0, 0),
                        LocalDate.now().minusDays(1).atTime(17, 0, 0)),
                getHoursResponse(
                        0,
                        "firstWorker",
                        "secondAdmin",
                        LocalDate.now().minusDays(1).atTime(18, 0, 0),
                        LocalDate.now().minusDays(1).atTime(23, 0, 0))
        )
        ), Arrays.asList(
                new Parameter("startDateTime", LocalDate.now().minusDays(1).atTime(9, 0, 0).toString()),
                new Parameter("endDateTime", LocalDate.now().atTime(17, 0, 0).toString()),
                new Parameter("workerUsername", "firstWorker")
        ));
    }

    @SneakyThrows
    private void validRangeWithCreatorUsername() {
        generateValidResponseMock((Arrays.asList(
                getHoursResponse(
                        0,
                        "firstWorker",
                        "firstAdmin",
                        LocalDate.now().minusDays(1).atTime(9, 0, 0),
                        LocalDate.now().minusDays(1).atTime(17, 0, 0)),
                getHoursResponse(
                        1,
                        "secondWorker",
                        "firstAdmin",
                        LocalDate.now().atTime(9, 0, 0),
                        LocalDate.now().atTime(17, 0, 0))
        )
        ), Arrays.asList(
                new Parameter("startDateTime", LocalDate.now().minusDays(1).atTime(9, 0, 0).toString()),
                new Parameter("endDateTime", LocalDate.now().atTime(17, 0, 0).toString()),
                new Parameter("creatorUsername", "firstAdmin")
        ));
    }

    @SneakyThrows
    private void validRangeWithBothUsernames() {
        generateValidResponseMock((Arrays.asList(
                getHoursResponse(
                        0,
                        "firstWorker",
                        "firstAdmin",
                        LocalDate.now().minusDays(1).atTime(9, 0, 0),
                        LocalDate.now().minusDays(1).atTime(17, 0, 0))
        )
        ), Arrays.asList(
                new Parameter("startDateTime", LocalDate.now().minusDays(1).atTime(9, 0, 0).toString()),
                new Parameter("endDateTime", LocalDate.now().atTime(17, 0, 0).toString()),
                new Parameter("creatorUsername", "firstAdmin"),
                new Parameter("workerUsername", "firstWorker")
        ));
    }

    @SneakyThrows
    private void invalidRangeRequest() {
        Map<String, String> response = new HashMap<>();
        response.put("field", "date range");
        response.put("message", "start date must be above the end date");

        generateMock(HttpStatus.BAD_REQUEST, new ObjectMapper().writeValueAsString(response), Arrays.asList(
                new Parameter("startDateTime", LocalDate.now().minusDays(2).atTime(9, 0, 0).toString()),
                new Parameter("endDateTime", LocalDate.now().minusDays(3).atTime(17, 0, 0).toString())
        ));
    }

    @SneakyThrows
    private void missingRangeRequest() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "No records within provided bounds were found");

        generateMock(HttpStatus.NOT_FOUND, new ObjectMapper().writeValueAsString(response), Arrays.asList(
                new Parameter("startDateTime", LocalDate.now().minusDays(1).atTime(9, 0, 0).toString()),
                new Parameter("endDateTime", LocalDate.now().atTime(17, 0, 0).toString()),
                new Parameter("creatorUsername", "missing")
        ));
    }
}
