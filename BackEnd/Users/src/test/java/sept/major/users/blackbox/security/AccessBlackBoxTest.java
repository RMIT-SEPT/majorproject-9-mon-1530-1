package sept.major.users.blackbox.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sept.major.users.blackbox.UserBlackBoxHelper;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AccessBlackBoxTest extends UserBlackBoxHelper {

    @Test
    @DisplayName("Delete request with various token")
    public void deleteAuthenticationTests() {
        // Attempt delete without valid token
        ResponseEntity response = deleteRequest(getUrl(), getUnAuthorizedHeaders());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

        // Attempt delete with user token
        response = deleteRequest(getUrl(), getAuthorizedUserHeaders());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);

        // Attempt delete with admin token
        response = deleteRequest(getUrl(), getAuthorizedAdminHeaders());
        assertTrue(requestAuthenticated(response.getStatusCode()));
    }

    @Test
    @DisplayName("Patch request with various token")
    public void patchAuthenticationTests() {
        // Attempt patch without valid token
        ResponseEntity response = patchRequest(getUrl(), getUnAuthorizedHeaders(new HashMap<>()));
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

        // Attempt patch with user token
        response = patchRequest(getUrl(), getAuthorizedUserHeaders());
        assertTrue(requestAuthenticated(response.getStatusCode()));

        // Attempt patch with admin token
        response = patchRequest(getUrl(), getAuthorizedAdminHeaders());
        assertTrue(requestAuthenticated(response.getStatusCode()));
    }

    @Test
    @DisplayName("Patch password request with various token")
    public void patchPasswordAuthenticationTests() {
        // Attempt patch without valid token
        ResponseEntity response = patchRequest(getUrl("password"), getUnAuthorizedHeaders(new HashMap<>()));
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

        // Attempt patch with user token
        response = patchRequest(getUrl("password"), getAuthorizedUserHeaders());
        assertTrue(requestAuthenticated(response.getStatusCode()));

        // Attempt patch with admin token
        response = patchRequest(getUrl("password"), getAuthorizedAdminHeaders());
        assertTrue(requestAuthenticated(response.getStatusCode()));
    }

    @Test
    @DisplayName("Post request with various token")
    public void postAuthenticationTests() {
        // Attempt post without valid token
        ResponseEntity response = postRequest(getUnAuthorizedHeaders(new HashMap<>()));
        assertTrue(requestAuthenticated(response.getStatusCode()));

        // Attempt post with user token
        response = postRequest(getAuthorizedUserHeaders());
        assertTrue(requestAuthenticated(response.getStatusCode()));

        // Attempt post with admin token
        response = postRequest(getAuthorizedAdminHeaders());
        assertTrue(requestAuthenticated(response.getStatusCode()));
    }

    @Test
    @DisplayName("Get bulk request with various token")
    public void getBulkAuthenticationTests() {
        // Attempt get all without valid token
        ResponseEntity response = getRequest(getUrl("bulk"), getUnAuthorizedHeaders(), String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

        // Attempt get all with user token
        response = getRequest(getUrl("bulk"), getAuthorizedUserHeaders(), String.class);
        assertTrue(requestAuthenticated(response.getStatusCode()));

        // Attempt get all with admin token
        response = getRequest(getUrl("bulk"), getAuthorizedAdminHeaders(), String.class);
        assertTrue(requestAuthenticated(response.getStatusCode()));
    }

    @Test
    @DisplayName("Get by username with various token")
    public void getByUsernameAuthenticationTests() {
        // Attempt get by id without valid token
        ResponseEntity response = getRequest(getUrl("username"), getUnAuthorizedHeaders(), String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

        // Attempt get by id with user token
        response = getRequest(getUrl("username"), getAuthorizedUserHeaders(), String.class);
        assertTrue(requestAuthenticated(response.getStatusCode()));

        // Attempt get by id with admin token
        response = getRequest(getUrl("username"), getAuthorizedAdminHeaders(), String.class);
        assertTrue(requestAuthenticated(response.getStatusCode()));
    }

    @Test
    @DisplayName("Get by username with various token")
    public void getTokenAuthenticationTests() {
        // Attempt get by id without valid token
        ResponseEntity response = getRequest(getUrl("token"), getUnAuthorizedHeaders(), String.class);
        assertTrue(requestAuthenticated(response.getStatusCode()));

        // Attempt get by id with user token
        response = getRequest(getUrl("token"), getAuthorizedUserHeaders(), String.class);
        assertTrue(requestAuthenticated(response.getStatusCode()));

        // Attempt get by id with admin token
        response = getRequest(getUrl("token"), getAuthorizedAdminHeaders(), String.class);
        assertTrue(requestAuthenticated(response.getStatusCode()));
    }

    private boolean requestAuthenticated(HttpStatus httpStatus) {
        return !HttpStatus.UNAUTHORIZED.equals(httpStatus) && !HttpStatus.FORBIDDEN.equals(httpStatus);
    }
}
