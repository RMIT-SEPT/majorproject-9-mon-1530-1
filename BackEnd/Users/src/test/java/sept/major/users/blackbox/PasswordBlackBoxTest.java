package sept.major.users.blackbox;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sept.major.common.testing.RequestParameter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static sept.major.users.UserServiceTestHelper.randomAlphanumericString;
import static sept.major.users.UserServiceTestHelper.randomEntityMap;

public class PasswordBlackBoxTest extends UsersBlackBoxHelper {

    @Test
    public void passwordCompare() {
        String password = randomAlphanumericString(20);
        HashMap<String, String> postResult = successfulPost(randomEntityMap(password));

        String username = postResult.get("username");

        List<RequestParameter> requestParameters = Arrays.asList(
                new RequestParameter("username", username),
                new RequestParameter("password", password));

        ResponseEntity<String> getResult = testRestTemplate.getForEntity(getUrl("password/compare", requestParameters), String.class);

        System.out.println(getResult);
        assertThat(getResult.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getResult.getBody()).isEqualTo("input,username:" + username + " password:" + password + ", password compare:true");
    }

    //    @Test TODO Currently doesn't work because we currently do not hash this input
    void passwordUpdate() {
        String password = randomAlphanumericString(20);
        HashMap<String, String> postResult = successfulPost(randomEntityMap(password));

        String username = postResult.get("username");

        List<RequestParameter> requestParameters = Arrays.asList(
                new RequestParameter("username", username),
                new RequestParameter("oldPassword", password),
                new RequestParameter("newPassword", randomAlphanumericString(20)));


        // RestTemplate doesn't have postForEntity method so we need to use .exchange() to get the ResponseEntity
        ResponseEntity<String> patchResult = testRestTemplate.exchange(getUrl("password", requestParameters), HttpMethod.PATCH, new HttpEntity<>(new HashMap<String, String>()), String.class);

        Assertions.assertThat(patchResult.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(patchResult.getBody()).isEqualTo("{\"field\":\"Identifier field\",\"message\":\"No record with a identifier of " + username + " was found\"}");
    }


}
