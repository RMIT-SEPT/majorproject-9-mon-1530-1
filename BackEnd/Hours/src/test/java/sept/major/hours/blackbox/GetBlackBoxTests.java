package sept.major.hours.blackbox;

import org.junit.jupiter.api.Test;
import sept.major.common.testing.RequestParameter;
import sept.major.hours.HoursTestHelper;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static sept.major.hours.HoursTestHelper.randomAlphanumericString;
import static sept.major.hours.HoursTestHelper.randomEntityMap;

public class GetBlackBoxTests extends HoursBlackBoxHelper {

    @Test
    public void getById() {
        List<Map<String, String>> postResults = Arrays.asList(successfulPost(randomEntityMap()), successfulPost(randomEntityMap()));

        List<RequestParameter> requestParameters = Arrays.asList(new RequestParameter("hoursId", "1"));

        successfulGet(postResults.get(0), getUrl(requestParameters));
    }

    @Test
    public void getAll() {
        String workerUsername = randomAlphanumericString(20);
        String creatorUsername = randomAlphanumericString(20);

        Map<String, String> justWorkerUsername = randomEntityMap();
        justWorkerUsername.put("workerUsername", workerUsername);

        Map<String, String> justCreatorUsername = randomEntityMap();
        justCreatorUsername.put("creatorUsername", creatorUsername);

        Map<String, String> creatorAndWorkerUsername = randomEntityMap();
        creatorAndWorkerUsername.put("workerUsername", workerUsername);
        creatorAndWorkerUsername.put("creatorUsername", creatorUsername);

        successfulPost(justWorkerUsername);
        successfulPost(justCreatorUsername);
        HashMap<String, String> expected = successfulPost(creatorAndWorkerUsername);
        successfulPost(randomEntityMap());

        List<RequestParameter> requestParameters = Arrays.asList(
                new RequestParameter("workerUsername", workerUsername),
                new RequestParameter("creatorUsername", creatorUsername)

        );

        successfulGetList(Arrays.asList(expected), getUrl("all", requestParameters));
    }

    @Test
    public void getDate() {
        Map<String, String> pastDate = randomEntityMap();
        pastDate.put("startDateTime", HoursTestHelper.pastDateTime(0, 0, 1).toString());
        pastDate.put("endDateTime", HoursTestHelper.pastDateTime(0, 0, 1).toString());

        Map<String, String> futureDate = randomEntityMap();
        futureDate.put("startDateTime", HoursTestHelper.futureDateTime(0, 0, 1).toString());
        futureDate.put("endDateTime", HoursTestHelper.futureDateTime(0, 0, 1).toString());

        Map<String, String> onDate = randomEntityMap();

        successfulPost(pastDate);
        successfulPost(futureDate);
        HashMap<String, String> expected = successfulPost(onDate);

        List<RequestParameter> requestParameters = Arrays.asList(
                new RequestParameter("date", LocalDate.now().toString())
        );

        successfulGetList(Arrays.asList(expected), getUrl("date", requestParameters));
    }



}
