package sept.major.users.blackbox;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import sept.major.common.response.ValidationError;
import sept.major.common.testing.RequestParameter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static sept.major.users.UserServiceTestHelper.randomAlphanumericString;
import static sept.major.users.UserServiceTestHelper.randomEntityMap;

public class PatchBlackBoxTest extends UsersBlackBoxHelper {

    @Test
    void valid() throws JsonProcessingException {
        HashMap<String, String> patchValues = new HashMap<>();
        patchValues.put("name", randomAlphanumericString(20));
        successfulPatch(randomEntityMap(), patchValues);
    }

    @Test
    void notExisting() {
        String username = randomAlphanumericString(20);
        List<RequestParameter> requestParameters = Arrays.asList(new RequestParameter("username", username));

        HashMap<String, String> patchValues = new HashMap<>();
        patchValues.put("name", randomAlphanumericString(20));

        // RestTemplate doesn't have postForEntity method so we need to use .exchange() to get the ResponseEntity
        ResponseEntity<String> patchResult = testRestTemplate.exchange(getUrl(requestParameters), HttpMethod.PATCH, new HttpEntity<>(patchValues), String.class);

        assertThat(patchResult.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(patchResult.getBody()).isEqualTo("{\"field\":\"Identifier field\",\"message\":\"No record with a identifier of " + username + " was found\"}");
    }

    @Test
    void missingField() throws JsonProcessingException {
        Map<String, String> firstPostMap = successfulPost(randomEntityMap());
        List<RequestParameter> requestParameters = Arrays.asList(new RequestParameter("username", firstPostMap.get("username")));

        HashMap<String, String> patchValues = new HashMap<>();
        patchValues.put("name", "");

        // RestTemplate doesn't have postForEntity method so we need to use .exchange() to get the ResponseEntity
        ResponseEntity<String> patchResult = testRestTemplate.exchange(getUrl(requestParameters), HttpMethod.PATCH, new HttpEntity<>(patchValues), String.class);

        System.out.println(patchResult);
        assertThat(patchResult.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(patchResult.getBody()).isEqualTo(new ObjectMapper().writeValueAsString(Arrays.asList(new ValidationError("name", "must not be blank"))));
    }

    @Test
    void incorrectFieldTypeList() {
        Map<String, String> firstPostMap = successfulPost(randomEntityMap());
        List<RequestParameter> requestParameters = Arrays.asList(new RequestParameter("username", firstPostMap.get("username")));

        HashMap<String, Object> patchValues = new HashMap<>();
        patchValues.put("name", Arrays.asList("an incorrect value"));

        // RestTemplate doesn't have postForEntity method so we need to use .exchange() to get the ResponseEntity
        ResponseEntity<String> patchResult = testRestTemplate.exchange(getUrl(requestParameters), HttpMethod.PATCH, new HttpEntity<>(patchValues), String.class);

        System.out.println(patchResult);
        assertThat(patchResult.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void incorrectFieldTypeMap() {
        Map<String, String> firstPostMap = successfulPost(randomEntityMap());
        List<RequestParameter> requestParameters = Arrays.asList(new RequestParameter("username", firstPostMap.get("username")));

        Map<String, String> incorrectField = new HashMap<>();
        incorrectField.put("incorrect", "field");

        HashMap<String, Object> patchValues = new HashMap<>();
        patchValues.put("name", incorrectField);

        // RestTemplate doesn't have postForEntity method so we need to use .exchange() to get the ResponseEntity
        ResponseEntity<String> patchResult = testRestTemplate.exchange(getUrl(requestParameters), HttpMethod.PATCH, new HttpEntity<>(patchValues), String.class);

        assertThat(patchResult.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }


    //    Default restTemplate doesn't support PATCH endpoints. The following adds that  support
    @Before
    void setupRestTemplate() {
        testRestTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());
    }
}
