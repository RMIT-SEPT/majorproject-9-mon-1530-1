package sept.major.bookings.blackbox.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sept.major.bookings.blackbox.BookingBlackBoxHelper;
import sept.major.common.testing.RequestParameter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static sept.major.bookings.BookingsTestHelper.randomEntityMap;
import static sept.major.bookings.BookingsTestHelper.randomInt;

public class DeleteBlackboxTest extends BookingBlackBoxHelper {
    @Test
    @DisplayName("Successfully delete entity")
    void successfullyDeleteEntity() {
        Map<String, String> post = successfulPost(randomEntityMap()); // Create the entity which will be deleted

        String bookingId = post.get("bookingId"); // Retrieve the id of the entity created
        List<RequestParameter> requestParameters = Arrays.asList(new RequestParameter("bookingId", bookingId));

        /*
            testRestTemplate doesn't support DELETE with an entity returned so we use .exchange instead.
            Sends delete request with the bookingId found above
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
        assertThat(getResult.getBody()).isEqualTo(String.format("No record with a identifier of %s was found", bookingId));
    }

    @Test
    @DisplayName("Attempt to delete not existing record")
    void entityMissing() {
        String bookingId = randomInt(4).toString();
        List<RequestParameter> requestParameters = Arrays.asList(new RequestParameter("bookingId", bookingId));

        ResponseEntity<String> deleteResult = testRestTemplate.exchange(getUrl(requestParameters), HttpMethod.DELETE, new HttpEntity<>(new HashMap<String, String>()), String.class);
        assertThat(deleteResult.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(String.format("No record with a identifier of %s was found", bookingId)).isEqualTo(deleteResult.getBody());

    }


}
