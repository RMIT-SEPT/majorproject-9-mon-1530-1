package sept.major.hours.controller.unit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sept.major.common.response.ValidationError;
import sept.major.hours.entity.HoursEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static sept.major.hours.HoursTestHelper.*;

@SpringBootTest
public class PostEndpointTests extends UnitTestHelper {

    @Test
    @DisplayName("Successfully create an entity")
    void valid() {
        Map<String, String> input = randomEntityMap();
        HoursEntity inputEntity = createHoursEntity(input);

        runTest(new ResponseEntity(inputEntity, HttpStatus.OK), input);
    }

    @Test
    @DisplayName("No creator username provided")
    void missingCreaterUsername() {
        String workerUsername = randomAlphanumericString(5);
        String startTime = LocalDateTime.now().toString();
        String endTime = LocalDateTime.now().plusHours(4).toString();

        Map<String, String> input = new HashMap<String, String>() {{
            put("workerUsername", workerUsername);
            put("startDateTime", startTime);
            put("endDateTime", endTime);
        }};

        runTest(new ResponseEntity(Arrays.asList(new ValidationError("creatorUsername", "must not be blank")), HttpStatus.BAD_REQUEST), input);
    }

    @Test
    @DisplayName("No worker username provided")
    void missingWorkerUsername() {
        String creatorUsername = randomAlphanumericString(5);
        String startTime = LocalDateTime.now().toString();
        String endTime = LocalDateTime.now().plusHours(4).toString();

        Map<String, String> input = new HashMap<String, String>() {{
            put("creatorUsername", creatorUsername);
            put("startDateTime", startTime);
            put("endDateTime", endTime);
        }};

        runTest(new ResponseEntity(Arrays.asList(new ValidationError("workerUsername", "must not be blank")), HttpStatus.BAD_REQUEST), input);
    }

    @Test
    @DisplayName("No start date provided")
    void missingStartTime() {
        String workerUsername = randomAlphanumericString(5);
        String creatorUsername = randomAlphanumericString(5);
        String endTime = LocalDateTime.now().plusHours(4).toString();

        Map<String, String> input = new HashMap<String, String>() {{
            put("workerUsername", workerUsername);
            put("creatorUsername", creatorUsername);
            put("endDateTime", endTime);
        }};

        runTest(new ResponseEntity(Arrays.asList(new ValidationError("startDateTime", "must not be null")), HttpStatus.BAD_REQUEST), input);
    }

    @Test
    @DisplayName("No end start provided")
    void missingEndTime() {
        String workerUsername = randomAlphanumericString(5);
        String creatorUsername = randomAlphanumericString(5);
        String startTime = LocalDateTime.now().toString();

        Map<String, String> input = new HashMap<String, String>() {{
            put("workerUsername", workerUsername);
            put("creatorUsername", creatorUsername);
            put("startDateTime", startTime);
        }};

        runTest(new ResponseEntity(Arrays.asList(new ValidationError("endDateTime", "must not be null")), HttpStatus.BAD_REQUEST), input);
    }

    @Test
    @DisplayName("Hours id provided")
    void hoursIdProvided() {
        Integer hoursId = randomInt(4);

        Map<String, String> input = randomEntityMap();
        input.put("hoursId", hoursId.toString());
        HoursEntity inputEntity = createHoursEntity(input);
        inputEntity.setHoursId(hoursId);

        runTest(new ResponseEntity(Arrays.asList(new ValidationError("hoursId", "value cannot be set because field is read only")), HttpStatus.BAD_REQUEST), input);
    }

    private void runTest(ResponseEntity expected, Map<String, String> input) {
        HoursEntity inputEntity = createHoursEntity(input);

        when(mockedHoursRepository.save(inputEntity)).thenReturn(inputEntity);

        ResponseEntity result = hoursController.createHours(input);
        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(expected.getStatusCode());
        assertThat(result.getBody()).isEqualTo(expected.getBody());
    }
}
