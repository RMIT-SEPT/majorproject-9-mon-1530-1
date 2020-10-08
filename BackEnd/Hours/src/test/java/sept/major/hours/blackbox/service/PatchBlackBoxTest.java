package sept.major.hours.blackbox.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import sept.major.common.response.ValidationError;
import sept.major.common.testing.RequestParameter;
import sept.major.hours.blackbox.HoursBlackBoxTests;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static sept.major.hours.HoursTestHelper.pastDateTime;
import static sept.major.hours.HoursTestHelper.randomEntityMap;

public class PatchBlackBoxTest extends HoursBlackBoxTests {

    @Test
    @DisplayName("Successfully update an entity")
    void valid() throws JsonProcessingException {
        HashMap<String, String> patchValues = new HashMap<>();
        patchValues.put("startDateTime", pastDateTime(1, 2, 3).toString());
        patchValues.put("endDateTime", pastDateTime(0, 0, 1).toString());
        successfulPatch(randomEntityMap(), patchValues);
    }

    @Test
    @DisplayName("Attempt to update not existing entity")
    void notExisting() throws JsonProcessingException {
        Map<String, String> firstPostMap = successfulPost(randomEntityMap());

        int id = Integer.parseInt(firstPostMap.get("hoursId")) + 1;

        List<RequestParameter> requestParameters = Arrays.asList(new RequestParameter("hoursId", String.valueOf(id)));

        ResponseEntity<String> patchResult = patchRequest(getUrl(requestParameters), new HashMap<>());

        assertThat(patchResult.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        ValidationError validationError = new ValidationError("Identifier field", String.format("No record with a identifier of %s was found", id));
        assertThat(patchResult.getBody()).isEqualTo(new ObjectMapper().writeValueAsString(validationError));
    }

    @Test
    @DisplayName("Update not blank field to not blank")
    void missingField() throws JsonProcessingException {
        Map<String, String> firstPostMap = successfulPost(randomEntityMap());
        List<RequestParameter> requestParameters = Arrays.asList(new RequestParameter("hoursId", firstPostMap.get("hoursId")));

        HashMap<String, String> patchValues = new HashMap<>();
        patchValues.put("creatorUsername", "");

        // RestTemplate doesn't have postForEntity method so we need to use .exchange() to get the ResponseEntity
        ResponseEntity<String> patchResult = patchRequest(getUrl(requestParameters), patchValues);

        assertThat(patchResult.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(patchResult.getBody()).isEqualTo(new ObjectMapper().writeValueAsString(Arrays.asList(new ValidationError("creatorUsername", "must not be blank"))));
    }

    @Test
    @DisplayName("Provided value for a field as a list")
    void incorrectFieldTypeList() {
        Map<String, String> firstPostMap = successfulPost(randomEntityMap());
        List<RequestParameter> requestParameters = Arrays.asList(new RequestParameter("hoursId", firstPostMap.get("hoursId")));

        HashMap<String, Object> patchValues = new HashMap<>();
        patchValues.put("creatorUsername", Arrays.asList("an incorrect value"));

        // RestTemplate doesn't have postForEntity method so we need to use .exchange() to get the ResponseEntity
        ResponseEntity<String> patchResult = patchRequest(getUrl(requestParameters), patchValues);

        System.out.println(patchResult);
        assertThat(patchResult.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("Provided value for a field as a map")
    void incorrectFieldTypeMap() {
        Map<String, String> firstPostMap = successfulPost(randomEntityMap());
        List<RequestParameter> requestParameters = Arrays.asList(new RequestParameter("hoursId", firstPostMap.get("hoursId")));

        Map<String, String> incorrectField = new HashMap<>();
        incorrectField.put("incorrect", "field");

        HashMap<String, Object> patchValues = new HashMap<>();
        patchValues.put("creatorUsername", incorrectField);

        // RestTemplate doesn't have postForEntity method so we need to use .exchange() to get the ResponseEntity
        ResponseEntity<String> patchResult = patchRequest(getUrl(requestParameters), patchValues);

        assertThat(patchResult.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("Updated end date to be before the start date")
    void endBeforeStartDate() {
        Map<String, String> firstPostMap = successfulPost(randomEntityMap());
        List<RequestParameter> requestParameters = Arrays.asList(new RequestParameter("hoursId", firstPostMap.get("hoursId")));

        HashMap<String, String> patchValues = new HashMap<>();
        patchValues.put("startDateTime", pastDateTime(0, 0, 1).toString());
        patchValues.put("endDateTime", pastDateTime(0, 0, 3).toString());

        // RestTemplate doesn't have postForEntity method so we need to use .exchange() to get the ResponseEntity
        ResponseEntity<String> patchResult = patchRequest(getUrl(requestParameters), patchValues);

        System.out.println(patchResult);
        assertThat(patchResult.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(patchResult.getBody()).isEqualTo("[{\"field\":\"endDateTime\",\"message\":\"must be after startDateTime\"}]");
    }


    /*
        TestRestTemplate does not support patch, even using .exchanged(), by default and this is needed to add support for it.
        Sets the request factory to an apache request factory implementation
     */
    @Before
    void setupRestTemplate() {
        testRestTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());
    }
}
