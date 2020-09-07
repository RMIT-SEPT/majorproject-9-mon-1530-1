package sept.major.users.blackbox;

import org.junit.jupiter.api.Test;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import sept.major.common.testing.RequestParameter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static sept.major.users.UserServiceTestHelper.randomAlphanumericString;
import static sept.major.users.UserServiceTestHelper.randomEntityMap;

public class GetBlackboxTests extends UsersBlackBoxHelper {

    @Test
    public void getById() {
        HashMap<String, String> postResult = successfulPost(randomEntityMap());

        String username = postResult.get("username");

        successfulGet(postResult, getUrl(Arrays.asList(new RequestParameter("username", username))));
    }

    @Test
    public void getBulk() throws JsonProcessingException {
        String userType = randomAlphanumericString(20);
        successfulPost(randomEntityMap());
        successfulPost(randomEntityMap());

        Map<String, String> firstExpected = randomEntityMap();
        firstExpected.put("userType", userType);
        successfulPost(firstExpected);

        Map<String, String> secondExcepted = randomEntityMap();
        secondExcepted.put("userType", userType);
        successfulPost(secondExcepted);

        firstExpected.remove("password");
        secondExcepted.remove("password");

        successfulGet(Arrays.asList(firstExpected, secondExcepted), getUrl("bulk", Arrays.asList(new RequestParameter("userType", userType))));
    }

    @Test
    public void getBulkNoUserType() throws JsonProcessingException {
        Map<String, String> firstExpected = successfulPost(randomEntityMap());
        Map<String, String> secondExcepted = successfulPost(randomEntityMap());

        firstExpected.remove("password");
        secondExcepted.remove("password");

        successfulGet(Arrays.asList(firstExpected, secondExcepted), getUrl("bulk"));
    }


}
