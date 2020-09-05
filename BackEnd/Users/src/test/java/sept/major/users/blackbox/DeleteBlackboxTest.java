package sept.major.users.blackbox;

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
import static sept.major.users.UserServiceTestHelper.randomEntityMap;

public class DeleteBlackboxTest extends UsersBlackBoxHelper {
    @Test
    void deleteEntity() {
        Map<String, String> post = successfulPost(randomEntityMap());

        String username = post.get("username");
        List<RequestParameter> requestParameters = Arrays.asList(new RequestParameter("username", username));

        successfulGet(post, getUrl(requestParameters));

        ResponseEntity<String> deleteResult = testRestTemplate.exchange(getUrl(requestParameters), HttpMethod.DELETE, new HttpEntity<>(new HashMap<String, String>()), String.class);
        assertThat(deleteResult.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(deleteResult.getBody()).isEqualTo("Record successfully deleted");

        ResponseEntity<String> getResult = testRestTemplate.getForEntity(getUrl(requestParameters), String.class);
        assertThat(getResult.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(getResult.getBody()).isEqualTo("No record with a identifier of " + username + " was found");
    }


}
