package sept.major.hours.blackbox;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.core.type.TypeReference;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import sept.major.common.testing.BlackboxTestHelper;

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


    protected Map<String, String> successfulPost(Map<String, Object> entityMap) {
        ResponseEntity<String> result = testRestTemplate.postForEntity(getUrl(), entityMap, String.class);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Map<String, String> resultMap = objectMapper.readValue(result.getBody(), new TypeReference<Map<String, String>>() {
            });
            entityMap.put("id", resultMap.get("id"));
            entityMap.put("hoursId", resultMap.get("hoursId"));

            assertThat(resultMap).isEqualTo(entityMap);

            return resultMap;
        } catch (JsonProcessingException e) {
            fail("POST response received couldn't be converted to a HashMap<String, String>");
            return null;
        }
    }
}
