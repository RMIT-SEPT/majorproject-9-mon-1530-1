package sept.major.users.blackbox.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sept.major.common.response.ValidationError;
import sept.major.users.blackbox.UserBlackBoxHelper;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static sept.major.users.UserTestHelper.randomEntityMap;

public class PostBlackBoxTest extends UserBlackBoxHelper {

    @Test
    void valid() {
        successfulPost(randomEntityMap());
    }

    @Test
    void existing() {
        Map<String, String> firstPostMap = successfulPost(randomEntityMap());
        ResponseEntity<String> result = postRequest(firstPostMap);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(result.getBody()).isEqualTo("{\"message\":\"Cannot create entity because an entity with given identifier already exists\"}");
    }

    @Test
    void missingField() throws JsonProcessingException {
        Map<String, String> randomEntityMap = randomEntityMap();
        randomEntityMap.remove("userType");
        ResponseEntity<String> result = postRequest(randomEntityMap);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(result.getBody()).isEqualTo(new ObjectMapper().writeValueAsString(Arrays.asList(new ValidationError("userType", "must not be blank"))));
    }

    @Test
    void incorrectFieldTypeList() {
        Map<String, Object> randomEntityMap = new HashMap<>(randomEntityMap());
        randomEntityMap.put("userType", Arrays.asList("an incorrect value"));
        ResponseEntity<String> result = postRequest(randomEntityMap);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void incorrectFieldTypeMap() {
        Map<String, Object> randomEntityMap = new HashMap<>(randomEntityMap());
        Map<String, String> incorrectField = new HashMap<>();
        incorrectField.put("incorrect", "field");
        randomEntityMap.put("userType", incorrectField);
        ResponseEntity<String> result = postRequest(randomEntityMap);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

}
