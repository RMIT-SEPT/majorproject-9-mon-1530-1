package sept.major.hours;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sept.major.common.response.FieldIncorrectTypeError;
import sept.major.common.response.ResponseError;
import sept.major.hours.entity.HoursEntity;

import javax.swing.text.html.Option;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.in;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
class PostEndpointTests extends UserServiceTestHelper {

    @Test
    void valid() {
        Map<String, Object> input = randomEntityMap();
        HoursEntity inputEntity = entityMapToEntity(input);

        runTest(input, new ResponseEntity(inputEntity, HttpStatus.OK), Optional.empty());
    }

    @Test
    void existing() {
        Map<String, Object> input = randomEntityMap();
        HoursEntity inputEntity = entityMapToEntity(input);

        runTest(input, new ResponseEntity("Failed to create entity because an entity with it's identifier already exists", HttpStatus.CONFLICT), Optional.of(inputEntity));
    }

    @Test
    void missingField() {
        Map<String, Object> input = randomEntityMap();
        input.remove("customerUsername");
        HoursEntity inputEntity = entityMapToEntity(input);

        runTest(input, new ResponseEntity(new HashSet<>(Arrays.asList(new ResponseError("customerUsername", "must not be blank"))), HttpStatus.BAD_REQUEST), null);
    }

    @Test
    void missingAllFields() {
        Map<String, Object> input = new HashMap<>();
        runTest(input, new ResponseEntity(new HashSet(Arrays.asList(
                new ResponseError("customerUsername", "must not be blank"),
                new ResponseError("workerUsername", "must not be blank"),
                new ResponseError("startDateTime", "must not be blank"),
                new ResponseError("endDateTime", "must not be blank")
        )), HttpStatus.BAD_REQUEST), null);
    }


    @Test
    void listField() {
        Map<String, Object> input = randomEntityMap();
        input.put("customerUsername", Arrays.asList(randomAlphanumericString(20)));
        HoursEntity inputEntity = entityMapToEntity(input);

        runTest(input, new ResponseEntity(new HashSet<>(Arrays.asList(new ResponseError("customerUsername", "field expected to be class java.lang.String but received class java.util.Arrays$ArrayList"))), HttpStatus.BAD_REQUEST), Optional.of(inputEntity));
    }

    @Test
    void mapField() {
        Map<String, Object> input = randomEntityMap();

        HashMap<String, Object> unexpectedMap = new HashMap<>();
        unexpectedMap.put("customerUsername", randomAlphanumericString(20));
        input.put("customerUsername", unexpectedMap);
        HoursEntity inputEntity = entityMapToEntity(input);

        runTest(input, new ResponseEntity(new HashSet<>(Arrays.asList(new ResponseError("customerUsername", "field expected to be class java.lang.String but received class java.util.HashMap"))), HttpStatus.BAD_REQUEST), Optional.of(inputEntity));
    }


    private void runTest(Map<String, Object> input, ResponseEntity expected, Optional<HoursEntity> returned) {
        when(mockedUserRepository.findById(anyString())).thenReturn(returned);
        HoursEntity inputEntity = entityMapToEntity(input);
        when(mockedUserRepository.save(inputEntity)).thenReturn(inputEntity);

        ResponseEntity result = hoursController.createHours(input);

        assertThat(result).isNotNull();
        assertThat(result.getBody()).isEqualTo(expected.getBody());
        assertThat(result.getStatusCode()).isEqualTo(expected.getStatusCode());
    }

}
