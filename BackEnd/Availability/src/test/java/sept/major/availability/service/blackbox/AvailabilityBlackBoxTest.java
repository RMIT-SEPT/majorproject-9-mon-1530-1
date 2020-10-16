package sept.major.availability.service.blackbox;

import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import sept.major.availability.service.RequestParameter;
import sept.major.availability.service.blackbox.mock.MockServices;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Component
public abstract class AvailabilityBlackBoxTest {

    @Autowired
    protected TestRestTemplate testRestTemplate;
    @LocalServerPort
    private int servicePort;

    @BeforeAll
    public static void setUpMocks() {
        MockServices.startUpServer();
    }

    @AfterAll
    public static void closeMocks() {
        MockServices.stopServer();
        ;
    }


    public String getUrl() {
        return String.format("http://localhost:%s/availability", this.servicePort);
    }

    public String getUrl(List<RequestParameter> requestParameters) {
        return this.addRequestParameters(getUrl(), requestParameters);
    }

    public String getUrl(String urlExtension, List<RequestParameter> requestParameters) {
        return this.addRequestParameters(getUrl(urlExtension), requestParameters);
    }

    public String addRequestParameters(String url, List<RequestParameter> requestParameters) {
        if (requestParameters != null && !requestParameters.isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder(url);

            for (int i = 0; i < requestParameters.size(); ++i) {
                if (i == 0) {
                    stringBuilder.append("?");
                } else {
                    stringBuilder.append("&");
                }

                stringBuilder.append(requestParameters.get(i));
            }

            return stringBuilder.toString();
        } else {
            return url;
        }
    }

    public String getUrl(String urlExtension) {
        return String.format("%s/%s", getUrl(), urlExtension);
    }


    public ResponseEntity<String> getRequest(String url) {
        return testRestTemplate.exchange(
                url,
                HttpMethod.GET,
                getHeaders(),
                String.class
        );
    }

    public HttpEntity getHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "foo");
        httpHeaders.set("username", "bar");
        return new HttpEntity(httpHeaders);
    }

    protected void compareExpected(Map<String, List<Map<String, Object>>> body, Map<String, List<Map<String, Object>>> expected) {
        List<Map<String, Object>> availabilities = body.get("availabilities");
        assertThat(availabilities).isNotEmpty();

        List<Map<String, Object>> bookings = body.get("bookings");
        assertThat(availabilities).isNotEmpty();

        List<Map<String, Object>> expectedAvailabilities = expected.get("availabilities");
        List<Map<String, Object>> expectedBookings = expected.get("bookings");

        assertThat(availabilities).hasSize(expectedAvailabilities.size());
        assertThat(bookings).hasSize(expectedBookings.size());

        for (Map<String, Object> availability : availabilities) {
            boolean foundMatch = false;
            for (Map<String, Object> expectedMap : expectedAvailabilities) {
                if (compareMap(availability, expectedMap)) {
                    foundMatch = true;
                    break;
                }
            }
            if (!foundMatch) {
                System.out.printf("Could find any records to match %s\n", availability.toString());
            }
            assertTrue(foundMatch);
        }

        for (Map<String, Object> booking : bookings) {
            boolean foundMatch = false;
            for (Map<String, Object> expectedMap : expectedBookings) {
                if (compareMap(booking, expectedMap)) {
                    foundMatch = true;
                    break;
                }
            }
            if (!foundMatch) {
                System.out.printf("Could find any records to match %s\n", booking.toString());
            }
            assertTrue(foundMatch);
        }
    }

    protected boolean compareMap(Map<String, Object> actualValues, Map<String, Object> expectedValues) {
        for (String key : actualValues.keySet()) {
            String expectedValue = expectedValues.get(key).toString();
            String actualValue = actualValues.get(key).toString();

            assertThat(actualValue).isNotNull();
            if (key.equalsIgnoreCase("startDateTime") || key.equalsIgnoreCase("endDateTime")) {
                if (!LocalDateTime.parse(actualValue).equals(LocalDateTime.parse(expectedValue))) {
                    return false;
                }
            } else {
                if (!expectedValue.equals(expectedValue)) {
                    return false;
                }
            }
        }

        return true;
    }

    @SneakyThrows
    protected Map<String, Object> getBookingsResponse(Integer bookingId, String workerUsername, String customerUsername, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        Map<String, Object> response = new HashMap<>();
        response.put("bookingId", bookingId);
        response.put("workerUsername", workerUsername);
        response.put("customerUsername", customerUsername);
        response.put("startDateTime", startDateTime);
        response.put("endDateTime", endDateTime);
        return response;
    }

    @SneakyThrows
    protected Map<String, Object> getExpectedAvailability(Integer hoursId, String workerUsername, String creatorUsername, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        Map<String, Object> response = new HashMap<>();
        response.put("hoursId", hoursId);
        response.put("workerUsername", workerUsername);
        response.put("creatorUsername", creatorUsername);
        response.put("startDateTime", startDateTime);
        response.put("endDateTime", endDateTime);
        return response;
    }
}
