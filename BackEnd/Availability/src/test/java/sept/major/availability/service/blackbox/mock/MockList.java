package sept.major.availability.service.blackbox.mock;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.mockserver.client.server.MockServerClient;
import org.mockserver.model.Header;
import org.mockserver.model.Parameter;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;

import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

public abstract class MockList {

    private final HttpMethod method = HttpMethod.GET;

    public abstract void createMocks();

    protected abstract String getHost();

    protected abstract Integer getPort();

    protected abstract String getPath();

    @SneakyThrows
    protected void generateValidResponseMock(List<?> response) {
        generateMock(HttpStatus.OK, new ObjectMapper().writeValueAsString(response));
    }

    @SneakyThrows
    protected void generateValidResponseMock(List<?> response, List<Parameter> parameters) {
        generateMock(HttpStatus.OK, new ObjectMapper().writeValueAsString(response), parameters);
    }

    @SneakyThrows
    protected void generateMock(HttpStatus responseCode, Map<String, String> response, Parameter parameter) {
        new MockServerClient(getHost(), getPort())
                .when(
                        request()
                                .withMethod(method.name())
                                .withPath(getPath())
                                .withQueryStringParameter(parameter)
                ).respond(
                response()
                        .withStatusCode(responseCode.value())
                        .withHeader(new Header("Content-Type", "application/json; charset=utf-8"))
                        .withBody(new ObjectMapper().writeValueAsString(response))
        );
    }

    @SneakyThrows
    protected void generateMock(HttpStatus responseCode, String response, List<Parameter> parameters) {
        new MockServerClient(getHost(), getPort())
                .when(
                        request()
                                .withMethod(method.name())
                                .withPath(getPath())
                                .withQueryStringParameters(parameters)
                ).respond(
                response()
                        .withStatusCode(responseCode.value())
                        .withHeader(new Header("Content-Type", "application/json; charset=utf-8"))
                        .withBody(response)
        );
    }

    @SneakyThrows
    protected void generateMock(HttpStatus responseCode, String response) {
        new MockServerClient(getHost(), getPort())
                .when(
                        request()
                                .withMethod(method.name())
                                .withPath(getPath())
                ).respond(
                response()
                        .withStatusCode(responseCode.value())
                        .withHeader(new Header("Content-Type", "application/json; charset=utf-8"))
                        .withBody(response)
        );
    }


}
