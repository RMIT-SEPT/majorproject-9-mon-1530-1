package sept.major.users.blackbox;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sept.major.common.response.ValidationError;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static sept.major.users.UserServiceTestHelper.randomEntityMap;

public class PostBlackBoxTest extends UsersBlackBoxHelper {

    @Test
    void valid() {
        successfulPost(randomEntityMap());
    }


    @Test
    void missingField() throws JsonProcessingException {
        Map<String, String> randomEntityMap = randomEntityMap();
        randomEntityMap.remove("password");
        ResponseEntity<String> result = testRestTemplate.postForEntity(getUrl(), randomEntityMap, String.class);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(result.getBody()).isEqualTo(new ObjectMapper().writeValueAsString(Arrays.asList(new ValidationError("password", "must not be blank"))));
    }

    @Test
    void incorrectFieldTypeList() {
        Map<String, Object> randomEntityMap = new HashMap<>(randomEntityMap());
        randomEntityMap.put("password", Arrays.asList("an incorrect value"));
        ResponseEntity<String> result = testRestTemplate.postForEntity(getUrl(), randomEntityMap, String.class);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void incorrectFieldTypeMap() {
        Map<String, Object> randomEntityMap = new HashMap<>(randomEntityMap());
        Map<String, String> incorrectField = new HashMap<>();
        incorrectField.put("incorrect", "field");
        randomEntityMap.put("password", incorrectField);
        ResponseEntity<String> result = testRestTemplate.postForEntity(getUrl(), randomEntityMap, String.class);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}
