package sept.major.hours.blackbox;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.core.type.TypeReference;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import sept.major.common.testing.BlackboxTestHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public abstract class HoursBlackBoxHelper extends BlackboxTestHelper {

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


    protected HashMap<String, String> successfulPost(Map<String, String> entityMap) {
        ResponseEntity<String> result = testRestTemplate.postForEntity(getUrl(), entityMap, String.class);

        System.out.println(result);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            HashMap<String, String> resultMap = objectMapper.readValue(result.getBody(), new TypeReference<HashMap<String, String>>() {
            });
            entityMap.put("hoursId", resultMap.get("hoursId"));


            assertThat(resultMap).isEqualTo(entityMap);

            return resultMap;
        } catch (JsonProcessingException e) {
            fail("POST response received couldn't be converted to a HashMap<String, String>");
            return null;
        }
    }

    protected void successfulGet(Map<String, String> expected, String url) {
        ResponseEntity<HashMap> getResult = testRestTemplate.getForEntity(url, HashMap.class);
        HashMap<String, String> getCastedResult = new HashMap<>();
        for (Object entry : getResult.getBody().entrySet()) {
            Map.Entry<String, Object> castEntry = (Map.Entry<String, Object>) entry;
            getCastedResult.put(castEntry.getKey(), castEntry.getValue().toString());
        }

        System.out.println(getResult);
        assertThat(getResult.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getCastedResult).isEqualTo(expected);
    }

    protected void successfulGetList(List<Map<String, String>> expected, String url) {
        ResponseEntity<List> getResult = testRestTemplate.getForEntity(url, List.class);
        List<HashMap<String, String>> getCastedResult = new ArrayList<>();


        for (Object result : getResult.getBody()) {
            HashMap<String, Object> resultCast = (HashMap<String, Object>) result;

            HashMap<String, String> castMap = new HashMap<>();
            for (Map.Entry<String, Object> entry : resultCast.entrySet()) {
                castMap.put(entry.getKey(), entry.getValue().toString());
            }

            getCastedResult.add(castMap);

        }

        System.out.println(getResult);
        assertThat(getResult.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getCastedResult).isEqualTo(expected);
    }
}
