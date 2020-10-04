package sept.major.hours.blackbox.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sept.major.hours.blackbox.HoursBlackBoxTests;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static sept.major.hours.blackbox.UserServiceMockServer.*;

public class SecurityBlackBoxTests extends HoursBlackBoxTests {

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
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);

        // Attempt patch with admin token
        response = patchRequest(getUrl(), getAuthorizedAdminHeaders());
        assertTrue(requestAuthenticated(response.getStatusCode()));
    }

    @Test
    @DisplayName("Post request with various token")
    public void postAuthenticationTests() {
        // Attempt post without valid token
        ResponseEntity response = postRequest(getUnAuthorizedHeaders(new HashMap<>()));
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

        // Attempt post with user token
        response = postRequest(getAuthorizedUserHeaders());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);

        // Attempt post with admin token
        response = postRequest(getAuthorizedAdminHeaders());
        assertTrue(requestAuthenticated(response.getStatusCode()));
    }

    @Test
    @DisplayName("Get all request with various token")
    public void getAllAuthenticationTests() {
        // Attempt get all without valid token
        ResponseEntity response = getRequest(getUrl("all"), getUnAuthorizedHeaders(), String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

        // Attempt get all with user token
        response = getRequest(getUrl("all"), getAuthorizedUserHeaders(), String.class);
        assertTrue(requestAuthenticated(response.getStatusCode()));

        // Attempt get all with admin token
        response = getRequest(getUrl("all"), getAuthorizedAdminHeaders(), String.class);
        assertTrue(requestAuthenticated(response.getStatusCode()));
    }

    @Test
    @DisplayName("Get range with various token")
    public void getRangeAuthenticationTests() {
        // Attempt get range without valid token
        ResponseEntity response = getRequest(getUrl("range"), getUnAuthorizedHeaders(), String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

        // Attempt get range with user token
        response = getRequest(getUrl("range"), getAuthorizedUserHeaders(), String.class);
        assertTrue(requestAuthenticated(response.getStatusCode()));

        // Attempt get range with admin token
        response = getRequest(getUrl("range"), getAuthorizedAdminHeaders(), String.class);
        assertTrue(requestAuthenticated(response.getStatusCode()));
    }

    @Test
    @DisplayName("Get date with various token")
    public void getDateAuthenticationTests() {
        // Attempt get date without valid token
        ResponseEntity response = getRequest(getUrl("date"), getUnAuthorizedHeaders(), String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

        // Attempt get date with user token
        response = getRequest(getUrl("date"), getAuthorizedUserHeaders(), String.class);
        assertTrue(requestAuthenticated(response.getStatusCode()));

        // Attempt get date with admin token
        response = getRequest(getUrl("date"), getAuthorizedAdminHeaders(), String.class);
        assertTrue(requestAuthenticated(response.getStatusCode()));
    }

    @Test
    @DisplayName("Get by id with various token")
    public void getByIdAuthenticationTests() {
        // Attempt get by id without valid token
        ResponseEntity response = getRequest(getUrl(), getUnAuthorizedHeaders(), String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

        // Attempt get by id with user token
        response = getRequest(getUrl(), getAuthorizedUserHeaders(), String.class);
        assertTrue(requestAuthenticated(response.getStatusCode()));

        // Attempt get by id with admin token
        response = getRequest(getUrl(), getAuthorizedAdminHeaders(), String.class);
        assertTrue(requestAuthenticated(response.getStatusCode()));
    }

    private boolean requestAuthenticated(HttpStatus httpStatus) {
        return !HttpStatus.UNAUTHORIZED.equals(httpStatus) && !HttpStatus.FORBIDDEN.equals(httpStatus);
    }

}
