package sept.major.hours.blackbox;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sept.major.common.testing.BlackboxTestHelper;
import sept.major.common.testing.MockUserServiceServer;
import sept.major.common.testing.RequestParameter;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static sept.major.common.testing.MockUserServiceServer.getAuthorizedAdminHeaders;
import static sept.major.common.testing.MockUserServiceServer.getAuthorizedUserHeaders;

public abstract class HoursBlackBoxTests extends BlackboxTestHelper {

    @Autowired
    protected TestRestTemplate testRestTemplate;

    @Override
    public String getInitScriptName() {
        return "hour.sql";
    }

    @Override
    public String getApiExtension() {
        return "hours";
    }


    @BeforeAll
    public static void setupMockUserServiceServer() {
        MockUserServiceServer.startUpServer();
    }

    @AfterAll
    public static void closeMockedUserServiceServer() {
        MockUserServiceServer.stopServer();
    }

    protected HashMap<String, String> successfulPost(Map<String, String> entityMap) {
        ResponseEntity<String> result = postRequest(entityMap);

        System.out.println(result);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            HashMap<String, String> resultMap = objectMapper.readValue(result.getBody(), new TypeReference<HashMap<String, String>>() {
            });

            HashMap<String, String> entityMapCopy = new HashMap<>(entityMap);
            entityMapCopy.put("hoursId", resultMap.get("hoursId"));

            System.out.println("Comparing " + resultMap + " to " + entityMapCopy);
            assertThat(resultMap.size()).isEqualTo(entityMapCopy.size());
            for (Map.Entry<String, String> entry : resultMap.entrySet()) {
                if (entry.getKey().equals("startDateTime") || entry.getKey().equals("endDateTime")) {
                    assertThat(LocalDateTime.parse(resultMap.get(entry.getKey()))).isEqualTo(LocalDateTime.parse(entityMapCopy.get(entry.getKey())));
                } else {
                    assertThat(resultMap.get(entry.getKey())).isEqualTo(entityMapCopy.get(entry.getKey()));
                }
            }

            return resultMap;
        } catch (JsonProcessingException e) {
            fail("POST response received couldn't be converted to a HashMap<String, String>");
            return null;
        }
    }

    protected void successfulPatch(Map<String, String> entityMap, Map<String, String> patchValues) throws JsonProcessingException {
        HashMap<String, String> postResult = successfulPost(entityMap);

        List<RequestParameter> requestParameters = Arrays.asList(
                new RequestParameter("hoursId", postResult.get("hoursId"))
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

        for (Map.Entry<String, String> entry : castPathResult.entrySet()) {
            if (entry.getKey().equals("startDateTime") || entry.getKey().equals("endDateTime")) {
                assertThat(LocalDateTime.parse(castPathResult.get(entry.getKey()))).isEqualTo(LocalDateTime.parse(postResult.get(entry.getKey())));
            } else {
                assertThat(castPathResult.get(entry.getKey())).isEqualTo(postResult.get(entry.getKey()));
            }
        }
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
        ResponseEntity<String> getResult = getRequest(url, String.class);
        System.out.println(getResult);
        assertThat(getResult.getStatusCode()).isEqualTo(HttpStatus.OK);

        ObjectMapper objectMapper = new ObjectMapper();
        List<HashMap<String, String>> getCastedResult = objectMapper.readValue(getResult.getBody(), new TypeReference<List<HashMap<String, String>>>() {
        });


        System.out.println(getCastedResult);
        assertThat(getCastedResult).isEqualTo(expected);
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
        return testRestTemplate.exchange(
                getUrl(),
                HttpMethod.POST,
                getAuthorizedAdminHeaders(body),
                String.class);
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

}
