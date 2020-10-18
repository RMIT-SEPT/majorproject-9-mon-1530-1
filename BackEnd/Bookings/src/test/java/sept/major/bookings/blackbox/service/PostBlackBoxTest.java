package sept.major.bookings.blackbox.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sept.major.bookings.blackbox.BookingBlackBoxHelper;
import sept.major.common.response.ValidationError;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static sept.major.bookings.BookingsTestHelper.pastDateTime;
import static sept.major.bookings.BookingsTestHelper.randomEntityMap;

public class PostBlackBoxTest extends BookingBlackBoxHelper {

    @Test
    void valid() {
        successfulPost(randomEntityMap());
    }

    @Test
    void existing() {
        Map<String, String> firstPostMap = successfulPost(randomEntityMap());
        firstPostMap.remove("bookingId");
        ResponseEntity<String> result = postRequest(firstPostMap);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(result.getBody()).startsWith("{\"message\":\"Booking provided conflicts with existing booking: ");
    }

    @Test
    void missingField() throws JsonProcessingException {
        Map<String, String> randomEntityMap = randomEntityMap();
        randomEntityMap.remove("customerUsername");
        ResponseEntity<String> result = postRequest(randomEntityMap);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(result.getBody()).isEqualTo(new ObjectMapper().writeValueAsString(Arrays.asList(new ValidationError("customerUsername", "must not be blank"))));
    }

    @Test
    void incorrectFieldTypeList() {
        Map<String, Object> randomEntityMap = new HashMap<>(randomEntityMap());
        randomEntityMap.put("customerUsername", Arrays.asList("an incorrect value"));
        ResponseEntity<String> result = postRequest(randomEntityMap);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void incorrectFieldTypeMap() {
        Map<String, Object> randomEntityMap = new HashMap<>(randomEntityMap());
        Map<String, String> incorrectField = new HashMap<>();
        incorrectField.put("incorrect", "field");
        randomEntityMap.put("customerUsername", incorrectField);
        ResponseEntity<String> result = postRequest(randomEntityMap);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void endBothEndDate() throws JsonProcessingException {
        Map<String, String> randomEntityMap = randomEntityMap();
        randomEntityMap.put("startDateTime", pastDateTime(0, 0, 1).toString());
        randomEntityMap.put("endDateTime", pastDateTime(0, 0, 3).toString());
        ResponseEntity<String> result = postRequest(randomEntityMap);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(result.getBody()).isEqualTo("[{\"field\":\"endDateTime\",\"message\":\"must be after startDateTime\"}]");
    }
}
