package sept.major.common.testing;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.mockserver.client.server.MockServerClient;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.Header;
import org.mockserver.model.Parameter;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.Map;

import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

/**
 * @author Brodey Yendall
 * @version %I%, %G%
 * @since 1.1.6
 * <p>
 * Used by the bookings and hours service to be a mock of the user service.
 * Allows for blackbox tests to simulate authentication.
 */
public class MockUserServiceServer {

    private static final Integer port = generateMockServerPort();
    private static final String host = "localhost";
    private static final ObjectMapper mapper = new ObjectMapper();

    private static ClientAndServer mockServer;


    /**
     *
     * Generates a random free port
     *
     * @return A random free port
     */
    private static int generateMockServerPort() {
        try {
            return (new ServerSocket(0)).getLocalPort();
        } catch (IOException var1) {
            throw new RuntimeException("System had no free ports", var1);
        }
    }


    /**
     * Starts the mock server and creates the mock endpoints
     */
    public static void startUpServer() {
        mockServer = startClientAndServer(port); // Start the server

        /*
            Set the system property so that services know what port to use to access this mock
         */
        System.setProperty("AUTH_PORT", port.toString());


        // Create mock endpoints
        createValidUserAuthorization();
        createValidAdminAuthorization();
    }

    public static void stopServer() {
        mockServer.stop();
    }


    /**
     * Creates a mock response giving user level security access
     */
    private static void createValidUserAuthorization() {
        new MockServerClient(host, port)
                .when(
                        request()
                                .withMethod("GET")
                                .withPath("/users/username")
                                .withHeaders(new Header("Authorization", "foo"), new Header("username", "ValidUser"))
                                .withQueryStringParameter(new Parameter("username", "ValidUser"))
                ).respond(
                response()
                        .withStatusCode(HttpStatus.OK.value())
                        .withHeader(new Header("Content-Type", "application/json; charset=utf-8"))
                        .withBody(getUserResponseJSON("ValidUser", "User"))
        );

    }

    /**
     * Creates a mock response giving admin level security access
     */
    private static void createValidAdminAuthorization() {
        new MockServerClient(host, port)
                .when(
                        request()
                                .withMethod("GET")
                                .withPath("/users/username")
                                .withHeaders(new Header("Authorization", "foo"), new Header("username", "ValidAdmin"))
                                .withQueryStringParameter(new Parameter("username", "ValidAdmin"))
                ).respond(
                response()
                        .withStatusCode(HttpStatus.OK.value())
                        .withHeader(new Header("Content-Type", "application/json; charset=utf-8"))
                        .withBody(getUserResponseJSON("ValidAdmin", "Admin"))
        );

    }


    /**
     * Generates the JSON of a user entity with provided username and userType. Intended to be used for a mock response
     * @param username The username the user entity has
     * @param userType The userType the user entity has
     * @return JSON String of a user entity with provided username and userType
     */
    @SneakyThrows
    private static String getUserResponseJSON(String username, String userType) {
        Map<String, String> response = new HashMap<>();
        response.put("username", username);
        response.put("userType", userType);
        response.put("name", "mock name");
        response.put("address", "mock address");
        response.put("phone", "mock phone");
        return mapper.writeValueAsString(response); // Convert the map to a JSON string
    }


    /**
     * Generates user security level authentication headers
     * @return A HttpEntity with user security level authentication headers
     */
    public static HttpEntity getAuthorizedUserHeaders() {
        return getAuthorizationHeaders(null, "ValidUser");
    }

    /**
     * Generates user security level authentication headers
     * @param body The body to attach to the HttpEntity containing the headers
     * @return A HttpEntity with user security level authentication headers and the body
     */
    public static HttpEntity getAuthorizedUserHeaders(Object body) {
        return getAuthorizationHeaders(body, "ValidUser");
    }

    /**
     * Generates admin security level authentication headers
     * @return A HttpEntity with admin security level authentication headers
     */
    public static HttpEntity getAuthorizedAdminHeaders() {
        return getAuthorizationHeaders(null, "ValidAdmin");
    }

    /**
     * Generates admin security level authentication headers
     * @param body The body to attach to the HttpEntity containing the headers
     * @return A HttpEntity with admin security level authentication headers and the body
     */
    public static HttpEntity getAuthorizedAdminHeaders(Object body) {
        return getAuthorizationHeaders(body, "ValidAdmin");
    }

    /**
     * Generates invalid authentication headers
     * @return A HttpEntity with invalid authentication headers
     */
    public static HttpEntity getUnAuthorizedHeaders() {
        return getAuthorizationHeaders(null, "unauthorized");
    }

    /**
     * Generates invalid authentication headers
     * @param body The body to attach to the HttpEntity containing the headers
     * @return A HttpEntity with invalid authentication headers and the body
     */
    public static HttpEntity getUnAuthorizedHeaders(Object body) {
        return getAuthorizationHeaders(body, "unauthorized");
    }


    /**
     * Generates authentication headers for a request to this mock server
     *
     * @param body     The body to attach to the HttpEntity containing the headers. Parameter is optional and a null can be provided
     *                 and no body will be attached
     * @param username The username to be used in the username header. Determines the security level if request is sent to this mock server
     * @return A HttpEntity with authentication headers and the optional body
     */
    public static HttpEntity getAuthorizationHeaders(@Nullable Object body, String username) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "foo");
        headers.set("username", username);
        return new HttpEntity(body, headers);
    }
}
