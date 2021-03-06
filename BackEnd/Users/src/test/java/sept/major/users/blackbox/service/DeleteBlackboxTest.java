package sept.major.users.blackbox.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sept.major.common.testing.RequestParameter;
import sept.major.users.blackbox.UserBlackBoxHelper;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static sept.major.users.UserTestHelper.randomAlphanumericString;
import static sept.major.users.UserTestHelper.randomEntityMap;

public class DeleteBlackboxTest extends UserBlackBoxHelper {
    @Test
    @DisplayName("Successfully delete entity")
    void successfullyDeleteEntity() {
        Map<String, String> post = successfulPost(randomEntityMap()); // Create the entity which will be deleted

        String username = post.get("username"); // Retrieve the id of the entity created
        List<RequestParameter> requestParameters = Arrays.asList(new RequestParameter("username", username));

        ResponseEntity<String> deleteResult = deleteRequest(getUrl(requestParameters));

        assertThat(deleteResult.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(deleteResult.getBody()).isEqualTo("Record successfully deleted");

        /*
            Attempt to retrieve the recently deleted entity
         */
        ResponseEntity<String> getResult = getRequest(getUrl("username", requestParameters), String.class);
        assertThat(getResult.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(getResult.getBody()).isEqualTo(String.format("{\"message\":\"No record with a identifier of %s was found\"}", username));
    }

    @Test
    @DisplayName("Attempt to delete not existing record")
    void entityMissing() {
        String username = randomAlphanumericString(20);
        List<RequestParameter> requestParameters = Arrays.asList(new RequestParameter("username", username));

        ResponseEntity<String> deleteResult = deleteRequest(getUrl(requestParameters));
        assertThat(deleteResult.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(String.format("{\"message\":\"No record with a identifier of %s was found\"}", username)).isEqualTo(deleteResult.getBody());

    }


}
