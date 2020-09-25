package sept.major.hours.blackbox.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sept.major.common.testing.RequestParameter;
import sept.major.hours.blackbox.HoursBlackBoxTests;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static sept.major.hours.HoursTestHelper.randomEntityMap;
import static sept.major.hours.HoursTestHelper.randomInt;

public class DeleteBlackboxTest extends HoursBlackBoxTests {
    @Test
    @DisplayName("Successfully delete entity")
    void successfullyDeleteEntity() {
        Map<String, String> post = successfulPost(randomEntityMap()); // Create the entity which will be deleted

        String hoursId = post.get("hoursId"); // Retrieve the id of the entity created
        List<RequestParameter> requestParameters = Arrays.asList(new RequestParameter("hoursId", hoursId));

        /*
            testRestTemplate doesn't support DELETE with an entity returned so we use .exchange instead.
            Sends delete request with the hoursId found above
         */
        ResponseEntity<String> deleteResult = testRestTemplate.exchange(getUrl(requestParameters),
                HttpMethod.DELETE,
                new HttpEntity<>(new HashMap<String, String>()),
                String.class);

        assertThat(deleteResult.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(deleteResult.getBody()).isEqualTo("Record successfully deleted");

        /*
            Attempt to retrieve the recently deleted entity
         */
        ResponseEntity<String> getResult = testRestTemplate.getForEntity(getUrl(requestParameters), String.class);
        assertThat(getResult.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(getResult.getBody()).isEqualTo(String.format("{\"message\":\"No record with a identifier of %s was found\"}", hoursId));
    }

    @Test
    @DisplayName("Attempt to delete not existing record")
    void entityMissing() {
        String hoursId = randomInt(4).toString();
        List<RequestParameter> requestParameters = Arrays.asList(new RequestParameter("hoursId", hoursId));

        ResponseEntity<String> deleteResult = testRestTemplate.exchange(getUrl(requestParameters), HttpMethod.DELETE, new HttpEntity<>(new HashMap<String, String>()), String.class);
        assertThat(deleteResult.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(deleteResult.getBody()).isEqualTo(String.format("{\"message\":\"No record with a identifier of %s was found\"}", hoursId));
    }


}
