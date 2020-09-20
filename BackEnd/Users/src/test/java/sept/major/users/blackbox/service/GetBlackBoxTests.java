package sept.major.users.blackbox.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import sept.major.common.testing.RequestParameter;
import sept.major.users.blackbox.UserBlackBoxHelper;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static sept.major.users.UserTestHelper.randomAlphanumericString;
import static sept.major.users.UserTestHelper.randomEntityMap;

public class GetBlackBoxTests extends UserBlackBoxHelper {

    @Test
    @DisplayName("Successfully retrieve record by username")
    public void getById() {
        String username = randomAlphanumericString(20);
        List<Map<String, String>> postResults = Arrays.asList(successfulPost(randomEntityMap(username)), successfulPost(randomEntityMap()));

        List<RequestParameter> requestParameters = Arrays.asList(new RequestParameter("username", username));

        successfulGet(postResults.get(0), getUrl(requestParameters));
    }

    @Test
    @DisplayName("GET all endpoint successfully gets record with matching userType")
    public void getAll() throws JsonProcessingException {
        String userType = randomAlphanumericString(20);

        // Creates random entity
        HashMap<String, String> first = successfulPost(randomEntityMap(randomAlphanumericString(20), userType));

        List<RequestParameter> requestParameters = Arrays.asList(
                new RequestParameter("userType", userType)
        );

        successfulGetList(Arrays.asList(first), getUrl("bulk", requestParameters));
    }

}
