package sept.major.hours.blackbox;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import sept.major.common.response.ResponseError;

import java.util.Arrays;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static sept.major.hours.HoursTestHelper.randomEntityMap;

public class PostBlackBoxTest extends HoursBlackBoxHelper {

    @Test
    void valid() {
        successfulPost(randomEntityMap(null));
    }

    //    @Test TODO Disabled so service can be constructed. Functionality to see if hours being created conflicts with an existing
    void existing() {
        Map<String, String> firstPostMap = successfulPost(randomEntityMap(null));
        firstPostMap.remove("id");
        firstPostMap.remove("hoursId");
        ResponseEntity<String> result = testRestTemplate.postForEntity(getUrl(), firstPostMap, String.class);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(result.getBody()).isEqualTo("");
    }

    @Test
    void missingField() throws JsonProcessingException {
        Map<String, Object> randomEntityMap = randomEntityMap(null);
        randomEntityMap.remove("customerUsername");
        ResponseEntity<String> result = testRestTemplate.postForEntity(getUrl(), randomEntityMap, String.class);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(result.getBody()).isEqualTo(new ObjectMapper().writeValueAsString(Arrays.asList(new ResponseError("customerUsername", "must not be blank"))));

    }
}
