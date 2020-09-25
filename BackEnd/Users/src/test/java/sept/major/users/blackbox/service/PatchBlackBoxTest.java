package sept.major.users.blackbox.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import sept.major.common.response.ValidationError;
import sept.major.common.testing.RequestParameter;
import sept.major.users.blackbox.UserBlackBoxHelper;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static sept.major.users.UserTestHelper.randomAlphanumericString;
import static sept.major.users.UserTestHelper.randomEntityMap;

public class PatchBlackBoxTest extends UserBlackBoxHelper {

    @Test
    @DisplayName("Successfully update an entity")
    void valid() throws JsonProcessingException {
        HashMap<String, String> patchValues = new HashMap<>();
        patchValues.put("userType", randomAlphanumericString(20));
        patchValues.put("name", randomAlphanumericString(20));
        successfulPatch(randomEntityMap(), patchValues);
    }

    @Test
    @DisplayName("Attempt to update not existing entity")
    void notExisting() throws JsonProcessingException {
        Map<String, String> firstPostMap = successfulPost(randomEntityMap());

        String username = randomAlphanumericString(20);

        List<RequestParameter> requestParameters = Arrays.asList(new RequestParameter("username", username));

        // RestTemplate doesn't have postForEntity method so we need to use .exchange() to get the ResponseEntity
        ResponseEntity<String> patchResult = testRestTemplate.exchange(getUrl(requestParameters), HttpMethod.PATCH, new HttpEntity<>(new HashMap<String, String>()), String.class);

        assertThat(patchResult.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        ValidationError validationError = new ValidationError("Identifier field", String.format("No record with a identifier of %s was found", username));
        assertThat(patchResult.getBody()).isEqualTo(new ObjectMapper().writeValueAsString(validationError));
    }

    @Test
    @DisplayName("Update not blank field to not blank")
    void missingField() throws JsonProcessingException {
        Map<String, String> firstPostMap = successfulPost(randomEntityMap());
        List<RequestParameter> requestParameters = Arrays.asList(new RequestParameter("username", firstPostMap.get("username")));

        HashMap<String, String> patchValues = new HashMap<>();
        patchValues.put("userType", "");

        // RestTemplate doesn't have postForEntity method so we need to use .exchange() to get the ResponseEntity
        ResponseEntity<String> patchResult = testRestTemplate.exchange(getUrl(requestParameters), HttpMethod.PATCH, new HttpEntity<>(patchValues), String.class);

        assertThat(patchResult.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(patchResult.getBody()).isEqualTo(new ObjectMapper().writeValueAsString(Arrays.asList(new ValidationError("userType", "must not be blank"))));
    }

    @Test
    @DisplayName("Provided value for a field as a list")
    void incorrectFieldTypeList() {
        Map<String, String> firstPostMap = successfulPost(randomEntityMap());
        List<RequestParameter> requestParameters = Arrays.asList(new RequestParameter("username", firstPostMap.get("username")));

        HashMap<String, Object> patchValues = new HashMap<>();
        patchValues.put("userType", Arrays.asList("an incorrect value"));

        // RestTemplate doesn't have postForEntity method so we need to use .exchange() to get the ResponseEntity
        ResponseEntity<String> patchResult = testRestTemplate.exchange(getUrl(requestParameters), HttpMethod.PATCH, new HttpEntity<>(patchValues), String.class);

        System.out.println(patchResult);
        assertThat(patchResult.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("Provided value for a field as a map")
    void incorrectFieldTypeMap() {
        Map<String, String> firstPostMap = successfulPost(randomEntityMap());
        List<RequestParameter> requestParameters = Arrays.asList(new RequestParameter("username", firstPostMap.get("username")));

        Map<String, String> incorrectField = new HashMap<>();
        incorrectField.put("incorrect", "field");

        HashMap<String, Object> patchValues = new HashMap<>();
        patchValues.put("userType", incorrectField);

        // RestTemplate doesn't have postForEntity method so we need to use .exchange() to get the ResponseEntity
        ResponseEntity<String> patchResult = testRestTemplate.exchange(getUrl(requestParameters), HttpMethod.PATCH, new HttpEntity<>(patchValues), String.class);

        assertThat(patchResult.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
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
