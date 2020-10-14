package sept.major.availability.service.blackbox.mock.list.hours;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.mockserver.client.server.MockServerClient;
import org.mockserver.model.Header;
import org.mockserver.model.Parameter;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import sept.major.availability.service.blackbox.mock.MockList;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static sept.major.availability.service.blackbox.mock.MockServices.getHoursResponse;

@AllArgsConstructor
public class HoursRangeRequestsMockList implements MockList {
    private final String path = "/hours/range";
    private final HttpMethod method = HttpMethod.GET;
    private Integer port;
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
        new MockServerClient(host, port)
                .when(
                        request()
                                .withMethod(method.name())
                                .withPath(path)
                                .withQueryStringParameters(
                                        new Parameter("startDateTime", LocalDate.now().minusDays(1).atTime(9, 0, 0).toString()),
                                        new Parameter("endDateTime", LocalDate.now().atTime(17, 0, 0).toString())
                                )
                ).respond(
                response()
                        .withStatusCode(HttpStatus.OK.value())
                        .withHeader(new Header("Content-Type", "application/json; charset=utf-8"))
                        .withBody(new ObjectMapper().writeValueAsString(Arrays.asList(
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
                        ))
        );

    }

    @SneakyThrows
    private void validRangeWithWorkerUsername() {
        new MockServerClient(host, port)
                .when(
                        request()
                                .withMethod(method.name())
                                .withPath(path)
                                .withQueryStringParameters(
                                        new Parameter("startDateTime", LocalDate.now().minusDays(1).atTime(9, 0, 0).toString()),
                                        new Parameter("endDateTime", LocalDate.now().atTime(17, 0, 0).toString()),
                                        new Parameter("workerUsername", "firstWorker")
                                )
                ).respond(
                response()
                        .withStatusCode(HttpStatus.OK.value())
                        .withHeader(new Header("Content-Type", "application/json; charset=utf-8"))
                        .withBody(new ObjectMapper().writeValueAsString(Arrays.asList(
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
                        ))
        );

    }

    @SneakyThrows
    private void validRangeWithCreatorUsername() {
        new MockServerClient(host, port)
                .when(
                        request()
                                .withMethod(method.name())
                                .withPath(path)
                                .withQueryStringParameters(
                                        new Parameter("startDateTime", LocalDate.now().minusDays(1).atTime(9, 0, 0).toString()),
                                        new Parameter("endDateTime", LocalDate.now().atTime(17, 0, 0).toString()),
                                        new Parameter("creatorUsername", "firstAdmin")
                                )
                ).respond(
                response()
                        .withStatusCode(HttpStatus.OK.value())
                        .withHeader(new Header("Content-Type", "application/json; charset=utf-8"))
                        .withBody(new ObjectMapper().writeValueAsString(Arrays.asList(
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
                        ))
        );

    }

    @SneakyThrows
    private void validRangeWithBothUsernames() {
        new MockServerClient(host, port)
                .when(
                        request()
                                .withMethod(method.name())
                                .withPath(path)
                                .withQueryStringParameters(
                                        new Parameter("startDateTime", LocalDate.now().minusDays(1).atTime(9, 0, 0).toString()),
                                        new Parameter("endDateTime", LocalDate.now().atTime(17, 0, 0).toString()),
                                        new Parameter("creatorUsername", "firstAdmin"),
                                        new Parameter("workerUsername", "firstWorker")
                                )
                ).respond(
                response()
                        .withStatusCode(HttpStatus.OK.value())
                        .withHeader(new Header("Content-Type", "application/json; charset=utf-8"))
                        .withBody(new ObjectMapper().writeValueAsString(Arrays.asList(
                                getHoursResponse(
                                        0,
                                        "firstWorker",
                                        "firstAdmin",
                                        LocalDate.now().minusDays(1).atTime(9, 0, 0),
                                        LocalDate.now().minusDays(1).atTime(17, 0, 0))
                                )
                        ))
        );

    }

    @SneakyThrows
    private void invalidRangeRequest() {
        Map<String, String> response = new HashMap<>();
        response.put("field", "date range");
        response.put("message", "start date must be above the end date");


        new MockServerClient(host, port)
                .when(
                        request()
                                .withMethod(method.name())
                                .withPath(path)
                                .withQueryStringParameters(
                                        new Parameter("startDateTime", LocalDate.now().minusDays(2).atTime(9, 0, 0).toString()),
                                        new Parameter("endDateTime", LocalDate.now().minusDays(3).atTime(17, 0, 0).toString())
                                )
                ).respond(
                response()
                        .withStatusCode(HttpStatus.BAD_REQUEST.value())
                        .withHeader(new Header("Content-Type", "application/json; charset=utf-8"))
                        .withBody(new ObjectMapper().writeValueAsString(response))
        );
    }

    @SneakyThrows
    private void missingRangeRequest() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "No records within provided bounds were found");


        new MockServerClient(host, port)
                .when(
                        request()
                                .withMethod(method.name())
                                .withPath(path)
                                .withQueryStringParameters(
                                        new Parameter("startDateTime", LocalDate.now().minusDays(1).atTime(9, 0, 0).toString()),
                                        new Parameter("endDateTime", LocalDate.now().atTime(17, 0, 0).toString()),
                                        new Parameter("creatorUsername", "missing")
                                )
                ).respond(
                response()
                        .withStatusCode(HttpStatus.NOT_FOUND.value())
                        .withHeader(new Header("Content-Type", "application/json; charset=utf-8"))
                        .withBody(new ObjectMapper().writeValueAsString(response))
        );

    }
}
