package sept.major.users.blackbox.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sept.major.common.testing.RequestParameter;
import sept.major.users.blackbox.UserBlackBoxHelper;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static sept.major.users.UserTestHelper.randomEntityMap;

public class GenerateTokenBlackBoxTest extends UserBlackBoxHelper {

    @Test
    @DisplayName("Username not provided")
    public void usernameNotProvided() {
        List<RequestParameter> requestParameters = Arrays.asList(new RequestParameter("password", "foo"));
        ResponseEntity<HashMap> response = tokenRequest(requestParameters, new HttpEntity(new HashMap<>()));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("Password not provided")
    public void passwordNotProvided() {
        List<RequestParameter> requestParameters = Arrays.asList(new RequestParameter("username", "foo"));
        ResponseEntity<HashMap> response = tokenRequest(requestParameters, new HttpEntity(new HashMap<>()));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }


    @Test
    @DisplayName("User with provided username doesn't exist")
    public void missingUser() {
        String username = "foo";

        List<RequestParameter> requestParameters = Arrays.asList(
                new RequestParameter("username", username),
                new RequestParameter("password", "bar"));
        ResponseEntity<HashMap> response = tokenRequest(requestParameters, getAuthorizedAdminHeaders());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        HashMap<String, String> expected = new HashMap<>();
        expected.put("username", username);
        expected.put("status", "failed");
        expected.put("token", null);


        assertThat(response.getBody()).isEqualTo(expected);
    }


    @Test
    @DisplayName("Provided password doesn't match password stored")
    public void invalidPassword() {
        String username = "foo";
        Map<String, String> randomEntityMap = randomEntityMap(username);
        String password = randomEntityMap.get("password");
        successfulPost(randomEntityMap);

        List<RequestParameter> requestParameters = Arrays.asList(
                new RequestParameter("username", username),
                new RequestParameter("password", password));
        ResponseEntity<HashMap> response = tokenRequest(requestParameters, getAuthorizedAdminHeaders());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        HashMap<String, Object> expected = new HashMap<>();

        randomEntityMap.remove("password");
        expected.put("user", randomEntityMap);
        expected.put("username", username);
        expected.put("status", "successful");

        /*
            There is no predicting what the token will be so it needs to be removed  from the comparison.
            But before we remove it, we want to check we actually got one
         */
        assertThat(response.getBody().get("token")).isNotNull();
        response.getBody().remove("token");

        assertThat(response.getBody()).isEqualTo(expected);
    }


    private ResponseEntity<HashMap> tokenRequest(List<RequestParameter> requestParameters, HttpEntity httpEntity) {
        String url;
        if (requestParameters == null) {
            url = getUrl("token");
        } else {
            url = getUrl("token", requestParameters);
        }

        return testRestTemplate.exchange(url,
                HttpMethod.PUT,
                httpEntity,
                HashMap.class);
    }
}
