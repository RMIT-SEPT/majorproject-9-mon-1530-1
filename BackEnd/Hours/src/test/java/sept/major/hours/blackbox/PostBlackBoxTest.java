package sept.major.hours.blackbox;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import sept.major.common.response.ValidationError;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static sept.major.hours.HoursTestHelper.pastDateTime;
import static sept.major.hours.HoursTestHelper.randomEntityMap;

public class PostBlackBoxTest extends HoursBlackBoxHelper {

    @Test
    void valid() {
        successfulPost(randomEntityMap());
    }

    @Test
    void existing() {
        Map<String, String> firstPostMap = successfulPost(randomEntityMap());
        firstPostMap.remove("hoursId");
        ResponseEntity<String> result = testRestTemplate.postForEntity(getUrl(), firstPostMap, String.class);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(result.getBody()).startsWith("Hours provided conflicts with existing hours:");
    }

    @Test
    void missingField() throws JsonProcessingException {
        Map<String, String> randomEntityMap = randomEntityMap();
        randomEntityMap.remove("creatorUsername");
        ResponseEntity<String> result = testRestTemplate.postForEntity(getUrl(), randomEntityMap, String.class);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(result.getBody()).isEqualTo(new ObjectMapper().writeValueAsString(Arrays.asList(new ValidationError("creatorUsername", "must not be blank"))));
    }

    @Test
    void incorrectFieldTypeList() {
        Map<String, Object> randomEntityMap = new HashMap<>(randomEntityMap());
        randomEntityMap.put("creatorUsername", Arrays.asList("an incorrect value"));
        ResponseEntity<String> result = testRestTemplate.postForEntity(getUrl(), randomEntityMap, String.class);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void incorrectFieldTypeMap() {
        Map<String, Object> randomEntityMap = new HashMap<>(randomEntityMap());
        Map<String, String> incorrectField = new HashMap<>();
        incorrectField.put("incorrect", "field");
        randomEntityMap.put("creatorUsername", incorrectField);
        ResponseEntity<String> result = testRestTemplate.postForEntity(getUrl(), randomEntityMap, String.class);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void endBothEndDate() throws JsonProcessingException {
        Map<String, String> randomEntityMap = randomEntityMap();
        randomEntityMap.put("startDateTime", pastDateTime(0, 0, 1).toString());
        randomEntityMap.put("endDateTime", pastDateTime(0, 0, 3).toString());
        ResponseEntity<String> result = testRestTemplate.postForEntity(getUrl(), randomEntityMap, String.class);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(result.getBody()).isEqualTo("[{\"field\":\"endDateTime\",\"message\":\"must be after startDateTime\"}]");
    }
}
