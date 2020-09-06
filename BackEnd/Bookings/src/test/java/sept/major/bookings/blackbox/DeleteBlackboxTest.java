package sept.major.bookings.blackbox;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sept.major.common.testing.RequestParameter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static sept.major.bookings.BookingsTestHelper.randomEntityMap;

public class DeleteBlackboxTest extends BookingBlackBoxHelper {
    @Test
    void deleteEntity() {
        Map<String, String> post = successfulPost(randomEntityMap());

        String bookingId = post.get("bookingId");
        List<RequestParameter> requestParameters = Arrays.asList(new RequestParameter("bookingId", bookingId));

        successfulGet(post, getUrl(requestParameters));

        ResponseEntity<String> deleteResult = testRestTemplate.exchange(getUrl(requestParameters), HttpMethod.DELETE, new HttpEntity<>(new HashMap<String, String>()), String.class);
        assertThat(deleteResult.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(deleteResult.getBody()).isEqualTo("Record successfully deleted");

        ResponseEntity<String> getResult = testRestTemplate.getForEntity(getUrl(requestParameters), String.class);
        assertThat(getResult.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(getResult.getBody()).isEqualTo("No booking entity for id " + bookingId);
    }


}
