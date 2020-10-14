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
public class HoursDateRequestsMockList implements MockList {
    private final String path = "/hours/date";
    private final HttpMethod method = HttpMethod.GET;
    private Integer port;
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
        new MockServerClient(host, port)
                .when(
                        request()
                                .withMethod(method.name())
                                .withPath(path)
                                .withQueryStringParameter(new Parameter("date", LocalDate.now().minusDays(1).toString()))
                ).respond(
                response()
                        .withStatusCode(HttpStatus.OK.value())
                        .withHeader(new Header("Content-Type", "application/json; charset=utf-8"))
                        .withBody(new ObjectMapper().writeValueAsString(Arrays.asList(
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
                        )))
        );

    }

    @SneakyThrows
    private void validDateWithWorkerUsername() {
        new MockServerClient(host, port)
                .when(
                        request()
                                .withMethod(method.name())
                                .withPath(path)
                                .withQueryStringParameters(
                                        new Parameter("date", LocalDate.now().minusDays(1).toString()),
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
                                        LocalDate.now().minusDays(1).atTime(1, 0, 0),
                                        LocalDate.now().minusDays(1).atTime(8, 0, 0)),
                                getHoursResponse(
                                        1,
                                        "firstWorker",
                                        "secondAdmin",
                                        LocalDate.now().minusDays(1).atTime(18, 0, 0),
                                        LocalDate.now().minusDays(1).atTime(23, 0, 0))
                        )))
        );

    }

    @SneakyThrows
    private void validDateWithCreatorUsername() {
        new MockServerClient(host, port)
                .when(
                        request()
                                .withMethod(method.name())
                                .withPath(path)
                                .withQueryStringParameters(
                                        new Parameter("date", LocalDate.now().minusDays(1).toString()),
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
                                        LocalDate.now().minusDays(1).atTime(1, 0, 0),
                                        LocalDate.now().minusDays(1).atTime(8, 0, 0)),
                                getHoursResponse(
                                        2,
                                        "secondWorker",
                                        "firstAdmin",
                                        LocalDate.now().minusDays(1).atTime(9, 0, 0),
                                        LocalDate.now().minusDays(1).atTime(17, 0, 0))
                        )))
        );

    }

    @SneakyThrows
    private void validDateWithBothUsernames() {
        new MockServerClient(host, port)
                .when(
                        request()
                                .withMethod(method.name())
                                .withPath(path)
                                .withQueryStringParameters(
                                        new Parameter("date", LocalDate.now().minusDays(1).toString()),
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
                                        LocalDate.now().minusDays(1).atTime(1, 0, 0),
                                        LocalDate.now().minusDays(1).atTime(8, 0, 0))
                        )))
        );

    }

    @SneakyThrows
    private void invalidDateRequest() {
        Map<String, String> response = new HashMap<>();
        response.put("field", "date");
        response.put("message", "\"Date must be formatted as yyyy-mm-dd");


        new MockServerClient(host, port)
                .when(
                        request()
                                .withMethod(method.name())
                                .withPath(path)
                                .withQueryStringParameters(
                                        new Parameter("date", "foo")
                                )
                ).respond(
                response()
                        .withStatusCode(HttpStatus.BAD_REQUEST.value())
                        .withHeader(new Header("Content-Type", "application/json; charset=utf-8"))
                        .withBody(new ObjectMapper().writeValueAsString(response))
        );
    }

    @SneakyThrows
    private void missingDateRequest() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "No records within provided bounds were found");


        new MockServerClient(host, port)
                .when(
                        request()
                                .withMethod(method.name())
                                .withPath(path)
                                .withQueryStringParameters(
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
