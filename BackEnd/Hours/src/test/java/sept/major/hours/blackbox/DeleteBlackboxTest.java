package sept.major.hours.blackbox;

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
import static sept.major.hours.HoursTestHelper.randomEntityMap;

public class DeleteBlackboxTest extends HoursBlackBoxHelper {
    @Test
    void deleteEntity() {
        Map<String, String> post = successfulPost(randomEntityMap());

        String hoursId = post.get("hoursId");
        List<RequestParameter> requestParameters = Arrays.asList(new RequestParameter("hoursId", hoursId));

        successfulGet(post, getUrl(requestParameters));

        ResponseEntity<String> deleteResult = testRestTemplate.exchange(getUrl(requestParameters), HttpMethod.DELETE, new HttpEntity<>(new HashMap<String, String>()), String.class);
        assertThat(deleteResult.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(deleteResult.getBody()).isEqualTo("Record successfully deleted");

        ResponseEntity<String> getResult = testRestTemplate.getForEntity(getUrl(requestParameters), String.class);
        assertThat(getResult.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(getResult.getBody()).isEqualTo("No record with a identifier of " + hoursId + " was found");
    }


}
