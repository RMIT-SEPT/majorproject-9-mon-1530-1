package sept.major.availability.service.blackbox;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sept.major.availability.service.RequestParameter;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

public class GetAllBlackBoxTests extends AvailabilityBlackBoxTest {

    @SneakyThrows
    @Test
    @DisplayName("valid GET all request with no usernames")
    public void noUsernames() {

        ResponseEntity<String> result = getRequest(getUrl("all"));

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);

        Map<String, List<Map<String, Object>>> body = new HashMap<>();
        body = new ObjectMapper().readValue(result.getBody(), body.getClass());
        assertThat(body).isNotEmpty();

        compareExpected(body, getExpected(false, false, false));

    }

    @SneakyThrows
    @Test
    @DisplayName("valid GET all request with worker username")
    public void workerUsername() {
        List<RequestParameter> requestParameters = Arrays.asList(
                new RequestParameter("workerUsername", "firstWorker")
        );


        ResponseEntity<String> result = getRequest(getUrl("all", requestParameters));

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);

        Map<String, List<Map<String, Object>>> body = new HashMap<>();
        body = new ObjectMapper().readValue(result.getBody(), body.getClass());
        assertThat(body).isNotEmpty();

        compareExpected(body, getExpected(true, false, false));

    }

    @SneakyThrows
    @Test
    @DisplayName("valid GET all request with creator username")
    public void creatorUsername() {
        List<RequestParameter> requestParameters = Arrays.asList(
                new RequestParameter("creatorUsername", "firstAdmin")
        );


        ResponseEntity<String> result = getRequest(getUrl("all", requestParameters));

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);

        Map<String, List<Map<String, Object>>> body = new HashMap<>();
        body = new ObjectMapper().readValue(result.getBody(), body.getClass());
        assertThat(body).isNotEmpty();

        compareExpected(body, getExpected(false, true, false));

    }

    @SneakyThrows
    @Test
    @DisplayName("valid GET all request with customer username")
    public void customerUsername() {
        List<RequestParameter> requestParameters = Arrays.asList(
                new RequestParameter("customerUsername", "firstCustomer")
        );


        ResponseEntity<String> result = getRequest(getUrl("all", requestParameters));

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);

        Map<String, List<Map<String, Object>>> body = new HashMap<>();
        body = new ObjectMapper().readValue(result.getBody(), body.getClass());
        assertThat(body).isNotEmpty();

        compareExpected(body, getExpected(false, false, true));

    }


    @SneakyThrows
    @Test
    @DisplayName("valid GET all request with worker and creator usernames")
    public void workerAndCreatorUsername() {
        List<RequestParameter> requestParameters = Arrays.asList(
                new RequestParameter("workerUsername", "firstWorker"),
                new RequestParameter("creatorUsername", "firstAdmin")
        );


        ResponseEntity<String> result = getRequest(getUrl("all", requestParameters));

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);

        Map<String, List<Map<String, Object>>> body = new HashMap<>();
        body = new ObjectMapper().readValue(result.getBody(), body.getClass());
        assertThat(body).isNotEmpty();

        compareExpected(body, getExpected(true, true, false));

    }

    @SneakyThrows
    @Test
    @DisplayName("valid GET all request with worker and customer usernames")
    public void workerAndCustomerUsername() {
        List<RequestParameter> requestParameters = Arrays.asList(
                new RequestParameter("workerUsername", "firstWorker"),
                new RequestParameter("customerUsername", "firstCustomer")
        );


        ResponseEntity<String> result = getRequest(getUrl("all", requestParameters));

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);

        Map<String, List<Map<String, Object>>> body = new HashMap<>();
        body = new ObjectMapper().readValue(result.getBody(), body.getClass());
        assertThat(body).isNotEmpty();

        compareExpected(body, getExpected(true, false, true));
    }

    @SneakyThrows
    @Test
    @DisplayName("valid GET all request with worker, creator and customer usernames")
    public void allUsernames() {
        List<RequestParameter> requestParameters = Arrays.asList(
                new RequestParameter("workerUsername", "firstWorker"),
                new RequestParameter("customerUsername", "firstCustomer"),
                new RequestParameter("creatorUsername", "firstAdmin")
        );


        ResponseEntity<String> result = getRequest(getUrl("all", requestParameters));

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);

        Map<String, List<Map<String, Object>>> body = new HashMap<>();
        body = new ObjectMapper().readValue(result.getBody(), body.getClass());
        assertThat(body).isNotEmpty();

        compareExpected(body, getExpected(true, true, true));
    }

    @SneakyThrows
    @Test
    @DisplayName("GET all request without valid username")
    public void invalidRequest() {
        List<RequestParameter> requestParameters = Arrays.asList(
                new RequestParameter("creatorUsername", "invalid"),
                new RequestParameter("customerUsername", "invalid")
        );


        ResponseEntity<String> result = getRequest(getUrl("all", requestParameters));

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(result.getBody()).isEqualTo("{\"service\":\"hours\",\"message\":\"400 Bad Request: [{\\\"field\\\":\\\"workerUsername\\\",\\\"message\\\":\\\"must be a valid username\\\"}]\"}");

    }

    @SneakyThrows
    @Test
    @DisplayName("GET all request without any hours or bookings")
    public void missingHoursAndBookings() {
        List<RequestParameter> requestParameters = Arrays.asList(
                new RequestParameter("customerUsername", "missing"),
                new RequestParameter("creatorUsername", "missing")
        );


        ResponseEntity<String> result = getRequest(getUrl("all", requestParameters));

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(result.getBody()).isEqualTo("{\"message\":\"found no hours in the range provided\"}");

    }

    @SneakyThrows
    @Test
    @DisplayName("GET all request without any hours")
    public void missingHours() {
        List<RequestParameter> requestParameters = Arrays.asList(
                new RequestParameter("creatorUsername", "missing")
        );


        ResponseEntity<String> result = getRequest(getUrl("all", requestParameters));

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(result.getBody()).isEqualTo("{\"message\":\"found no hours in the range provided\"}");

    }

    @SneakyThrows
    @Test
    @DisplayName("GET all request without any hours")
    public void missingBookings() {
        List<RequestParameter> requestParameters = Arrays.asList(
                new RequestParameter("customerUsername", "missing")
        );


        ResponseEntity<String> result = getRequest(getUrl("all", requestParameters));

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);


        List<Map<String, Object>> availabilities = Arrays.asList(
                getExpectedAvailability(
                        0,
                        "firstWorker",
                        "firstAdmin",
                        LocalDate.now().minusDays(1).atTime(9, 0, 0),
                        LocalDate.now().minusDays(1).atTime(17, 0, 0)),
                getExpectedAvailability(
                        0,
                        "firstWorker",
                        "secondAdmin",
                        LocalDate.now().minusDays(1).atTime(18, 0, 0),
                        LocalDate.now().minusDays(1).atTime(23, 0, 0)),
                getExpectedAvailability(
                        1,
                        "secondWorker",
                        "firstAdmin",
                        LocalDate.now().atTime(9, 0, 0),
                        LocalDate.now().atTime(17, 0, 0))
        );

        Map<String, List<Map<String, Object>>> expected = new HashMap<>();
        expected.put("availabilities", availabilities);
        expected.put("bookings", new ArrayList<>());

        Map<String, List<Map<String, Object>>> body = new HashMap<>();
        body = new ObjectMapper().readValue(result.getBody(), body.getClass());

        compareExpected(body, expected);

    }


    public Map<String, List<Map<String, Object>>> getExpected(boolean filteringWorkerUsername, boolean filteringCreatorUsername, boolean filteringCustomerUsername) {
        Map<String, List<Map<String, Object>>> expected = new HashMap<>();

        List<Map<String, Object>> availabilities = new ArrayList<>();
        availabilities.add(getExpectedAvailability(0, "firstWorker", "firstAdmin", LocalDate.now().minusDays(1).atTime(9, 0, 0), LocalDate.now().minusDays(1).atTime(11, 0, 0)));
        availabilities.add(getExpectedAvailability(0, "firstWorker", "firstAdmin", LocalDate.now().minusDays(1).atTime(13, 0, 0), LocalDate.now().minusDays(1).atTime(17, 0, 0)));

        if (!filteringCreatorUsername) {
            if (!filteringCustomerUsername) {
                availabilities.add(getExpectedAvailability(0, "firstWorker", "secondAdmin", LocalDate.now().minusDays(1).atTime(18, 0, 0), LocalDate.now().minusDays(1).atTime(19, 0, 0)));
                availabilities.add(getExpectedAvailability(0, "firstWorker", "secondAdmin", LocalDate.now().minusDays(1).atTime(20, 0, 0), LocalDate.now().minusDays(1).atTime(23, 0, 0)));
            } else {
                availabilities.add(getExpectedAvailability(0, "firstWorker", "secondAdmin", LocalDate.now().minusDays(1).atTime(18, 0, 0), LocalDate.now().minusDays(1).atTime(23, 0, 0)));
            }

        }

        if (!filteringWorkerUsername) {
            availabilities.add(getExpectedAvailability(1, "secondWorker", "firstAdmin", LocalDate.now().atTime(9, 0, 0), LocalDate.now().atTime(10, 0, 0)));
            availabilities.add(getExpectedAvailability(1, "secondWorker", "firstAdmin", LocalDate.now().atTime(11, 0, 0), LocalDate.now().atTime(17, 0, 0)));
        }


        List<Map<String, Object>> bookings = new ArrayList<>();

        bookings.add(getBookingsResponse(0, "firstWorker", "firstCustomer", LocalDate.now().minusDays(1).atTime(11, 0, 0), LocalDate.now().minusDays(1).atTime(13, 0, 0)));

        if (!filteringCustomerUsername) {
            bookings.add(getBookingsResponse(1, "firstWorker", "secondCustomer", LocalDate.now().minusDays(1).atTime(19, 0, 0), LocalDate.now().minusDays(1).atTime(20, 0, 0)));
        }

        if (!filteringWorkerUsername) {
            bookings.add(getBookingsResponse(2, "secondWorker", "firstCustomer", LocalDate.now().atTime(10, 0, 0), LocalDate.now().atTime(11, 0, 0)));
        }


        expected.put("availabilities", availabilities);
        expected.put("bookings", bookings);

        return expected;
    }
}
