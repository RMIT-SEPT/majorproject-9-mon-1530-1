package sept.major.users.blackbox;

import static sept.major.users.UserTestHelper.randomAlphanumericString;
import static sept.major.users.UserTestHelper.randomEntityMap;


import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;

import sept.major.common.testing.RequestParameter;

public class GetBlackboxTests extends UserBlackBoxHelper {

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

        successfulGetList(Arrays.asList(firstExpected, secondExcepted), getUrl("bulk", Arrays.asList(new RequestParameter("userType", userType))));
    }

    @Test
    public void getBulkNoUserType() throws JsonProcessingException {
        Map<String, String> firstExpected = successfulPost(randomEntityMap());
        Map<String, String> secondExcepted = successfulPost(randomEntityMap());

        firstExpected.remove("password");
        secondExcepted.remove("password");

        successfulGetList(Arrays.asList(firstExpected, secondExcepted), getUrl("bulk"));
    }


}
