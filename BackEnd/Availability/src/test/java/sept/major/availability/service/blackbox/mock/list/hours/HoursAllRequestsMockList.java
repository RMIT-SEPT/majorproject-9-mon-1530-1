package sept.major.availability.service.blackbox.mock.list.hours;

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
public class HoursAllRequestsMockList extends MockList {
    private final HttpMethod method = HttpMethod.GET;

    @Getter
    private final String path = "/hours/all";

    @Getter
    private Integer port;

    @Getter
    private String host;

    public void createMocks() {
        validAllWithBothUsernames();
        validAllWithWorkerUsername();
        validAllWithCreatorUsername();
        invalidAllRequest();
        missingAllRequest();
        validAllNoUsernames();
    }

    @SneakyThrows
    private void validAllNoUsernames() {
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
        )));
    }

    @SneakyThrows
    private void validAllWithWorkerUsername() {
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
                new Parameter("workerUsername", "firstWorker")
        ));

    }

    @SneakyThrows
    private void validAllWithCreatorUsername() {
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
                new Parameter("creatorUsername", "firstAdmin")
        ));
    }

    @SneakyThrows
    private void validAllWithBothUsernames() {
        generateValidResponseMock((Arrays.asList(
                getHoursResponse(
                        0,
                        "firstWorker",
                        "firstAdmin",
                        LocalDate.now().minusDays(1).atTime(9, 0, 0),
                        LocalDate.now().minusDays(1).atTime(17, 0, 0))

        )), Arrays.asList(
                new Parameter("creatorUsername", "firstAdmin"),
                new Parameter("workerUsername", "firstWorker")
        ));
    }

    @SneakyThrows
    private void invalidAllRequest() {
        Map<String, String> response = new HashMap<>();
        response.put("field", "workerUsername");
        response.put("message", "must be a valid username");

        generateMock(HttpStatus.BAD_REQUEST, response, new Parameter("creatorUsername", "invalid"));
    }

    @SneakyThrows
    private void missingAllRequest() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "No records within provided bounds were found");

        generateMock(HttpStatus.NOT_FOUND, response, new Parameter("creatorUsername", "missing"));
    }
}
