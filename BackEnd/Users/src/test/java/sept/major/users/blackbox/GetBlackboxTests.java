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

        successfulGet(postResult, getUrl("username", Arrays.asList(new RequestParameter("username", username))));
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

        /*
        For authenication testing we need these two records in the database. They expected but not posted here
         */
        Map<String, String> thirdExpected = new HashMap<>();
        thirdExpected.put("address", "Test address");
        thirdExpected.put("name", "Test name");
        thirdExpected.put("phone", "Test phone");
        thirdExpected.put("userType", "User");
        thirdExpected.put("username", "ValidUser");

        Map<String, String> fourthExpected = new HashMap<>();
        fourthExpected.put("address", "Test address");
        fourthExpected.put("name", "Test name");
        fourthExpected.put("phone", "Test phone");
        fourthExpected.put("userType", "Admin");
        fourthExpected.put("username", "ValidAdmin");


        firstExpected.remove("password");
        secondExcepted.remove("password");

        successfulGetList(Arrays.asList(firstExpected, secondExcepted, thirdExpected, fourthExpected), getUrl("bulk"));
    }


}
