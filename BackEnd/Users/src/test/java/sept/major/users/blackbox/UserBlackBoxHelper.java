package sept.major.users.blackbox;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import sept.major.common.testing.BlackboxTestHelper;
import sept.major.common.testing.RequestParameter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public abstract class UserBlackBoxHelper extends BlackboxTestHelper {

    @Autowired
    protected TestRestTemplate testRestTemplate;

    @Override
    public String getInitScriptName() {
        return "user.sql";
    }

    @Override
    public String getApiExtension() {
        return "users";
    }


    protected HashMap<String, String> successfulPost(Map<String, String> entityMap) {
        ResponseEntity<String> result = postRequest(entityMap);

        System.out.println(result);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            HashMap<String, String> resultMap = objectMapper.readValue(result.getBody(), new TypeReference<HashMap<String, String>>() {
            });

            entityMap.remove("password"); // Password aren't returned

            assertThat(resultMap).isEqualTo(entityMap);

            return resultMap;
        } catch (JsonProcessingException e) {
            fail("POST response received couldn't be converted to a HashMap<String, String>");
            return null;
        }
    }

    protected void successfulPatch(Map<String, String> entityMap, Map<String, String> patchValues) throws JsonProcessingException {
        HashMap<String, String> postResult = successfulPost(entityMap);

        List<RequestParameter> requestParameters = Arrays.asList(
                new RequestParameter("username", postResult.get("username"))
        );

        // RestTemplate doesn't have postForEntity method so we need to use .exchange() to get the ResponseEntity
        ResponseEntity<String> patchResult = patchRequest(getUrl(requestParameters), patchValues);

        assertThat(patchResult.getStatusCode()).isEqualTo(HttpStatus.OK);

        for (Map.Entry<String, String> entry : patchValues.entrySet()) {
            postResult.put(entry.getKey(), entry.getValue());
        }

        ObjectMapper objectMapper = new ObjectMapper();
        HashMap<String, String> castPathResult = objectMapper.readValue(patchResult.getBody(), new TypeReference<HashMap<String, String>>() {
        });

        assertThat(castPathResult).isEqualTo(postResult);
    }


    protected void successfulGet(Map<String, String> expected, String url) {
        ResponseEntity<HashMap> getResult = getRequest(url, HashMap.class);
        HashMap<String, String> getCastedResult = new HashMap<>();
        for (Object entry : getResult.getBody().entrySet()) {
            Map.Entry<String, Object> castEntry = (Map.Entry<String, Object>) entry;
            getCastedResult.put(castEntry.getKey(), castEntry.getValue().toString());
        }

        System.out.println(getResult);
        assertThat(getResult.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getCastedResult).isEqualTo(expected);
    }

    protected void successfulGetList(List<Map<String, String>> expected, String url) throws JsonProcessingException {
        ResponseEntity<String> getResult = getRequest(url, getAuthorizedAdminHeaders(), String.class);

        System.out.println(getResult);
        assertThat(getResult.getStatusCode()).isEqualTo(HttpStatus.OK);

        ObjectMapper objectMapper = new ObjectMapper();
        List<Map<String, String>> getCastedResult = objectMapper.readValue(getResult.getBody(), new TypeReference<List<Map<String, String>>>() {
        });


        System.out.println(getCastedResult);
        assertThat(getCastedResult).containsAll(expected);
    }


    protected <T> ResponseEntity<T> getRequest(String url, Class<T> returnType) {
        return testRestTemplate.exchange(
                url,
                HttpMethod.GET,
                getAuthorizedUserHeaders(),
                returnType);

    }

    protected ResponseEntity<String> patchRequest(String url, Object body) {
        return testRestTemplate.exchange(
                url,
                HttpMethod.PATCH,
                getAuthorizedAdminHeaders(body),
                String.class);
    }

    protected ResponseEntity<String> postRequest(Object body) {
        return testRestTemplate.postForEntity(getUrl(), body, String.class);
    }

    protected ResponseEntity<String> deleteRequest(String url) {
        return testRestTemplate.exchange(
                url,
                HttpMethod.DELETE,
                getAuthorizedAdminHeaders(),
                String.class);
    }

    protected <T> ResponseEntity<T> getRequest(String url, HttpEntity httpEntity, Class<T> returnType) {
        return testRestTemplate.exchange(
                url,
                HttpMethod.GET,
                httpEntity,
                returnType);

    }

    protected ResponseEntity<String> patchRequest(String url, HttpEntity httpEntity) {
        return testRestTemplate.exchange(
                url,
                HttpMethod.PATCH,
                httpEntity,
                String.class);
    }

    protected ResponseEntity<String> postRequest(HttpEntity httpEntity) {
        return testRestTemplate.exchange(
                getUrl(),
                HttpMethod.POST,
                httpEntity,
                String.class);
    }

    protected ResponseEntity<String> deleteRequest(String url, HttpEntity httpEntity) {
        return testRestTemplate.exchange(
                url,
                HttpMethod.DELETE,
                httpEntity,
                String.class);
    }

    public HttpEntity getAuthorizedUserHeaders() {
        return formAuthorizationHeaders(null, "ValidUser");
    }

    public HttpEntity getAuthorizedUserHeaders(Object body) {
        return formAuthorizationHeaders(body, "ValidUser");
    }

    public HttpEntity getAuthorizedAdminHeaders() {
        return formAuthorizationHeaders(null, "ValidAdmin");
    }

    public HttpEntity getAuthorizedAdminHeaders(Object body) {
        return formAuthorizationHeaders(body, "ValidAdmin");
    }

    public HttpEntity getUnAuthorizedHeaders() {
        return formAuthorizationHeaders(null, "unauthorized");
    }

    public HttpEntity getUnAuthorizedHeaders(Object body) {
        return formAuthorizationHeaders(body, "unauthorized");
    }

    public HttpEntity formAuthorizationHeaders(Object body, String username) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", username + "Token");
        headers.set("username", username);
        return new HttpEntity(body, headers);
    }

}
