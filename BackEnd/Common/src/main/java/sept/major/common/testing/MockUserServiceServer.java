package sept.major.common.testing;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.mockserver.client.server.MockServerClient;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.Header;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.Map;

import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

public class MockUserServiceServer {

    private static final Integer port = generateMockServerPort();

    private static ClientAndServer mockServer;


    private static int generateMockServerPort() {
        try {
            return (new ServerSocket(0)).getLocalPort();
        } catch (IOException var1) {
            throw new RuntimeException("System had no free ports", var1);
        }
    }


    public static void startUpServer() {
        mockServer = startClientAndServer(port);
        System.setProperty("AUTH_PORT", port.toString());
        createValidUserAuthorization();
        createValidAdminAuthorization();
    }

    public static void stopServer() {
        mockServer.stop();
    }

    private static void createValidUserAuthorization() {
        new MockServerClient("localhost", port)
                .when(
                        request()
                                .withMethod("GET")
                                .withPath("/users/token")
                                .withHeaders(new Header("Authorization", "foo"), new Header("username", "ValidUser"))
                ).respond(
                response()
                        .withStatusCode(HttpStatus.OK.value())
                        .withHeader(new Header("Content-Type", "application/json; charset=utf-8"))
                        .withBody(getUserResponseJSON("ValidUser", "User"))
        );

    }

    private static void createValidAdminAuthorization() {
        new MockServerClient("localhost", port)
                .when(
                        request()
                                .withMethod("GET")
                                .withPath("/users/token")
                                .withHeaders(new Header("Authorization", "foo"), new Header("username", "ValidAdmin"))
                ).respond(
                response()
                        .withStatusCode(HttpStatus.OK.value())
                        .withHeader(new Header("Content-Type", "application/json; charset=utf-8"))
                        .withBody(getUserResponseJSON("ValidAdmin", "Admin"))
        );

    }


    @SneakyThrows
    private static String getUserResponseJSON(String username, String userType) {
        Map<String, String> response = new HashMap<>();
        response.put("username", username);
        response.put("userType", userType);
        response.put("name", "mock name");
        response.put("address", "mock address");
        response.put("phone", "mock phone");
        return new ObjectMapper().writeValueAsString(response);
    }


    public static HttpEntity getAuthorizedUserHeaders() {
        return getAuthorizationHeaders(null, "ValidUser");
    }

    public static HttpEntity getAuthorizedUserHeaders(Object body) {
        return getAuthorizationHeaders(body, "ValidUser");
    }

    public static HttpEntity getAuthorizedAdminHeaders() {
        return getAuthorizationHeaders(null, "ValidAdmin");
    }

    public static HttpEntity getAuthorizedAdminHeaders(Object body) {
        return getAuthorizationHeaders(body, "ValidAdmin");
    }

    public static HttpEntity getUnAuthorizedHeaders() {
        return getAuthorizationHeaders(null, "unauthorized");
    }

    public static HttpEntity getUnAuthorizedHeaders(Object body) {
        return getAuthorizationHeaders(body, "unauthorized");
    }

    public static HttpEntity getAuthorizationHeaders(Object body, String username) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "foo");
        headers.set("username", username);
        return new HttpEntity(body, headers);
    }
}
