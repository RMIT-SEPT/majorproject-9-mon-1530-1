package sept.major.users.blackbox;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sept.major.common.testing.BlackboxTestHelper;
import sept.major.common.testing.RequestParameter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public abstract class UsersBlackBoxHelper extends BlackboxTestHelper {

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
        ResponseEntity<String> result = testRestTemplate.postForEntity(getUrl(), entityMap, String.class);

        System.out.println(result);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            HashMap<String, String> resultMap = objectMapper.readValue(result.getBody(), new TypeReference<HashMap<String, String>>() {
            });

            HashMap<String, String> entityMapCopy = new HashMap<>(entityMap);
            entityMapCopy.remove("password");

            System.out.println("Comparing " + resultMap + " to " + entityMapCopy);
            assertThat(resultMap.size()).isEqualTo(entityMapCopy.size());
            assertThat(resultMap).isEqualTo(entityMapCopy);

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
        ResponseEntity<String> patchResult = testRestTemplate.exchange(getUrl(requestParameters), HttpMethod.PATCH, new HttpEntity<>(patchValues), String.class);

        System.out.println(patchResult);
        assertThat(patchResult.getStatusCode()).isEqualTo(HttpStatus.OK);

        for (Map.Entry<String, String> entry : patchValues.entrySet()) {
            postResult.put(entry.getKey(), entry.getValue());
        }

        ObjectMapper objectMapper = new ObjectMapper();
        HashMap<String, String> castPathResult = objectMapper.readValue(patchResult.getBody(), new TypeReference<HashMap<String, String>>() {
        });

        for (Map.Entry<String, String> entry : castPathResult.entrySet()) {
            assertThat(castPathResult.get(entry.getKey())).isEqualTo(postResult.get(entry.getKey()));
        }
    }


    protected void successfulGet(Map<String, String> expected, String url) {
        ResponseEntity<HashMap> getResult = testRestTemplate.getForEntity(url, HashMap.class);

        System.out.println(getResult);
        assertThat(getResult.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getResult.getBody()).isEqualTo(expected);
    }

    protected void successfulGet(List<Map<String, String>> expected, String url) throws JsonProcessingException {
        ResponseEntity<String> getResult = testRestTemplate.getForEntity(url, String.class);

        System.out.println(getResult);
        assertThat(getResult.getStatusCode()).isEqualTo(HttpStatus.OK);

        ObjectMapper objectMapper = new ObjectMapper();
        List<HashMap<String, String>> getCastedResult = objectMapper.readValue(getResult.getBody(), new TypeReference<List<HashMap<String, String>>>() {
        });


        System.out.println(getCastedResult);
        assertThat(getCastedResult).isEqualTo(expected);
    }

}
