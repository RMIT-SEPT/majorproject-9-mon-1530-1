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
public class HoursDateRequestsMockList extends MockList {

    private final HttpMethod method = HttpMethod.GET;

    @Getter
    private final String path = "/hours/date";

    @Getter
    private Integer port;

    @Getter
    private String host;

    public void createMocks() {
        validDateWithBothUsernames();
        validDateWithWorkerUsername();
        validDateWithCreatorUsername();
        invalidDateRequest();
        missingDateRequest();
        validDateNoUsernames();
    }

    @SneakyThrows
    private void validDateNoUsernames() {
        generateValidResponseMock((Arrays.asList(
                getHoursResponse(
                        0,
                        "firstWorker",
                        "firstAdmin",
                        LocalDate.now().minusDays(1).atTime(1, 0, 0),
                        LocalDate.now().minusDays(1).atTime(8, 0, 0)),
                getHoursResponse(
                        1,
                        "secondWorker",
                        "firstAdmin",
                        LocalDate.now().minusDays(1).atTime(9, 0, 0),
                        LocalDate.now().minusDays(1).atTime(17, 0, 0)),
                getHoursResponse(
                        2,
                        "firstWorker",
                        "secondAdmin",
                        LocalDate.now().minusDays(1).atTime(18, 0, 0),
                        LocalDate.now().minusDays(1).atTime(23, 0, 0))
        )
        ), Arrays.asList(
                new Parameter("date", LocalDate.now().minusDays(1).toString())
        ));
    }

    @SneakyThrows
    private void validDateWithWorkerUsername() {
        generateValidResponseMock((Arrays.asList(
                getHoursResponse(
                        0,
                        "firstWorker",
                        "firstAdmin",
                        LocalDate.now().minusDays(1).atTime(1, 0, 0),
                        LocalDate.now().minusDays(1).atTime(8, 0, 0)),
                getHoursResponse(
                        1,
                        "firstWorker",
                        "secondAdmin",
                        LocalDate.now().minusDays(1).atTime(18, 0, 0),
                        LocalDate.now().minusDays(1).atTime(23, 0, 0))
        )
        ), Arrays.asList(
                new Parameter("date", LocalDate.now().minusDays(1).toString()),
                new Parameter("workerUsername", "firstWorker")
        ));
    }

    @SneakyThrows
    private void validDateWithCreatorUsername() {
        generateValidResponseMock((Arrays.asList(
                getHoursResponse(
                        0,
                        "firstWorker",
                        "firstAdmin",
                        LocalDate.now().minusDays(1).atTime(1, 0, 0),
                        LocalDate.now().minusDays(1).atTime(8, 0, 0)),
                getHoursResponse(
                        2,
                        "secondWorker",
                        "firstAdmin",
                        LocalDate.now().minusDays(1).atTime(9, 0, 0),
                        LocalDate.now().minusDays(1).atTime(17, 0, 0))
        )
        ), Arrays.asList(
                new Parameter("date", LocalDate.now().minusDays(1).toString()),
                new Parameter("creatorUsername", "firstAdmin")
        ));
    }

    @SneakyThrows
    private void validDateWithBothUsernames() {
        generateValidResponseMock((Arrays.asList(
                getHoursResponse(
                        0,
                        "firstWorker",
                        "firstAdmin",
                        LocalDate.now().minusDays(1).atTime(1, 0, 0),
                        LocalDate.now().minusDays(1).atTime(8, 0, 0))
        )
        ), Arrays.asList(
                new Parameter("date", LocalDate.now().minusDays(1).toString()),
                new Parameter("creatorUsername", "firstAdmin"),
                new Parameter("workerUsername", "firstWorker")
        ));
    }

    @SneakyThrows
    private void invalidDateRequest() {
        Map<String, String> response = new HashMap<>();
        response.put("field", "date");
        response.put("message", "\"Date must be formatted as yyyy-mm-dd");

        generateMock(HttpStatus.BAD_REQUEST, response, new Parameter("date", "foo"));
    }

    @SneakyThrows
    private void missingDateRequest() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "No records within provided bounds were found");

        generateMock(HttpStatus.NOT_FOUND, response, new Parameter("creatorUsername", "missing"));
    }
}
