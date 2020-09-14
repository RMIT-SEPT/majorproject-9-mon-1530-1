package sept.major.bookings.blackbox.service;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import sept.major.bookings.blackbox.BookingBlackBoxHelper;
import sept.major.common.response.ValidationError;
import sept.major.common.testing.RequestParameter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static sept.major.bookings.BookingsTestHelper.pastDateTime;
import static sept.major.bookings.BookingsTestHelper.randomEntityMap;

public class PatchBlackBoxTest extends BookingBlackBoxHelper {

    @Test
    void valid() throws JsonProcessingException {
        HashMap<String, String> patchValues = new HashMap<>();
        patchValues.put("startDateTime", pastDateTime(1, 2, 3).toString());
        patchValues.put("endDateTime", pastDateTime(0, 0, 1).toString());
        successfulPatch(randomEntityMap(), patchValues);
    }

    @Test
    void notExisting() {
        Map<String, String> firstPostMap = successfulPost(randomEntityMap());

        int id = Integer.parseInt(firstPostMap.get("bookingId")) + 1;

        List<RequestParameter> requestParameters = Arrays.asList(new RequestParameter("bookingId", String.valueOf(id)));

        // RestTemplate doesn't have postForEntity method so we need to use .exchange() to get the ResponseEntity
        ResponseEntity<String> patchResult = testRestTemplate.exchange(getUrl(requestParameters), HttpMethod.PATCH, new HttpEntity<>(new HashMap<String, String>()), String.class);

        assertThat(patchResult.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(patchResult.getBody()).isEqualTo("{\"field\":\"Identifier field\",\"message\":\"No record with a identifier of " + id + " was found\"}");
    }

    @Test
    void missingField() throws JsonProcessingException {
        Map<String, String> firstPostMap = successfulPost(randomEntityMap());
        List<RequestParameter> requestParameters = Arrays.asList(new RequestParameter("bookingId", firstPostMap.get("bookingId")));

        HashMap<String, String> patchValues = new HashMap<>();
        patchValues.put("customerUsername", "");

        // RestTemplate doesn't have postForEntity method so we need to use .exchange() to get the ResponseEntity
        ResponseEntity<String> patchResult = testRestTemplate.exchange(getUrl(requestParameters), HttpMethod.PATCH, new HttpEntity<>(patchValues), String.class);

        System.out.println(patchResult);
        assertThat(patchResult.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(patchResult.getBody()).isEqualTo(new ObjectMapper().writeValueAsString(Arrays.asList(new ValidationError("customerUsername", "must not be blank"))));
    }

    @Test
    void incorrectFieldTypeList() {
        Map<String, String> firstPostMap = successfulPost(randomEntityMap());
        List<RequestParameter> requestParameters = Arrays.asList(new RequestParameter("bookingId", firstPostMap.get("bookingId")));

        HashMap<String, Object> patchValues = new HashMap<>();
        patchValues.put("customerUsername", Arrays.asList("an incorrect value"));

        // RestTemplate doesn't have postForEntity method so we need to use .exchange() to get the ResponseEntity
        ResponseEntity<String> patchResult = testRestTemplate.exchange(getUrl(requestParameters), HttpMethod.PATCH, new HttpEntity<>(patchValues), String.class);

        System.out.println(patchResult);
        assertThat(patchResult.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void incorrectFieldTypeMap() {
        Map<String, String> firstPostMap = successfulPost(randomEntityMap());
        List<RequestParameter> requestParameters = Arrays.asList(new RequestParameter("bookingId", firstPostMap.get("bookingId")));

        Map<String, String> incorrectField = new HashMap<>();
        incorrectField.put("incorrect", "field");

        HashMap<String, Object> patchValues = new HashMap<>();
        patchValues.put("customerUsername", incorrectField);

        // RestTemplate doesn't have postForEntity method so we need to use .exchange() to get the ResponseEntity
        ResponseEntity<String> patchResult = testRestTemplate.exchange(getUrl(requestParameters), HttpMethod.PATCH, new HttpEntity<>(patchValues), String.class);

        assertThat(patchResult.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void endBeforeStartDate() {
        Map<String, String> firstPostMap = successfulPost(randomEntityMap());
        List<RequestParameter> requestParameters = Arrays.asList(new RequestParameter("bookingId", firstPostMap.get("bookingId")));

        HashMap<String, String> patchValues = new HashMap<>();
        patchValues.put("startDateTime", pastDateTime(0, 0, 1).toString());
        patchValues.put("endDateTime", pastDateTime(0, 0, 3).toString());

        // RestTemplate doesn't have postForEntity method so we need to use .exchange() to get the ResponseEntity
        ResponseEntity<String> patchResult = testRestTemplate.exchange(getUrl(requestParameters), HttpMethod.PATCH, new HttpEntity<>(patchValues), String.class);

        System.out.println(patchResult);
        assertThat(patchResult.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(patchResult.getBody()).isEqualTo("[{\"field\":\"endDateTime\",\"message\":\"must be after startDateTime\"}]");
    }


//    Default restTemplate doesn't support PATCH endpoints. The following adds that  support

    @Before
    void setupRestTemplate() {
        testRestTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());
    }
}
