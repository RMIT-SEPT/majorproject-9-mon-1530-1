package sept.major.hours.blackbox;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import sept.major.common.testing.RequestParameter;
import sept.major.hours.HoursTestHelper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static sept.major.hours.HoursTestHelper.*;

public class GetBlackBoxTests extends HoursBlackBoxHelper {

    @Test
    public void getById() {
        List<Map<String, String>> postResults = Arrays.asList(successfulPost(randomEntityMap()), successfulPost(randomEntityMap()));

        List<RequestParameter> requestParameters = Arrays.asList(new RequestParameter("hoursId", "1"));

        successfulGet(postResults.get(0), getUrl(requestParameters));
    }

    @Test
    public void getAll() throws JsonProcessingException {
        String workerUsername = randomAlphanumericString(20);
        String creatorUsername = randomAlphanumericString(20);

        HashMap<String, String> first = postWithUsernames(randomEntityMap(), workerUsername, creatorUsername);
        HashMap<String, String> second = postWithUsernames(randomEntityMap(pastDateTime(0, 0, 1)), workerUsername, creatorUsername);

        List<RequestParameter> requestParameters = Arrays.asList(
                new RequestParameter("workerUsername", workerUsername),
                new RequestParameter("creatorUsername", creatorUsername)

        );

        successfulGetList(Arrays.asList(first, second), getUrl("all", requestParameters));
    }

    @Test
    public void getDate() throws JsonProcessingException {

        String workerUsername = randomAlphanumericString(20);
        String creatorUsername = randomAlphanumericString(20);

        Map<String, String> pastDate = randomEntityMap();
        pastDate.put("startDateTime", HoursTestHelper.pastDateTime(0, 0, 1).toString());
        pastDate.put("endDateTime", HoursTestHelper.pastDateTime(0, 0, 1).toString());

        Map<String, String> futureDate = randomEntityMap();
        futureDate.put("startDateTime", HoursTestHelper.futureDateTime(0, 0, 1).toString());
        futureDate.put("endDateTime", HoursTestHelper.futureDateTime(0, 0, 1).toString());

        Map<String, String> onDate = randomEntityMap();

        postWithUsernames(pastDate, workerUsername, creatorUsername);
        postWithUsernames(futureDate, workerUsername, creatorUsername);
        HashMap<String, String> expected = postWithUsernames(onDate, workerUsername, creatorUsername);

        List<RequestParameter> requestParameters = Arrays.asList(
                new RequestParameter("date", LocalDate.now().toString()),
                new RequestParameter("workerUsername", workerUsername),
                new RequestParameter("creatorUsername", creatorUsername)
        );

        successfulGetList(Arrays.asList(expected), getUrl("date", requestParameters));
    }

    @Test
    public void getRange() throws JsonProcessingException {

        String workerUsername = randomAlphanumericString(20);
        String creatorUsername = randomAlphanumericString(20);

        Map<String, String> pastDate = randomEntityMap();
        pastDate.put("startDateTime", HoursTestHelper.pastDateTime(0, 0, 3).toString());
        pastDate.put("endDateTime", HoursTestHelper.pastDateTime(0, 0, 3).toString());

        Map<String, String> futureDate = randomEntityMap();
        futureDate.put("startDateTime", HoursTestHelper.futureDateTime(0, 0, 1).toString());
        futureDate.put("endDateTime", HoursTestHelper.futureDateTime(0, 0, 1).toString());

        Map<String, String> correctDayPastTime = randomEntityMap();
        correctDayPastTime.put("startDateTime", LocalDateTime.of(pastDate(0, 0, 2), LocalTime.of(7, 30, 0)).toString());
        correctDayPastTime.put("endDateTime", LocalDateTime.of(pastDate(0, 0, 2), LocalTime.of(8, 30, 0)).toString());

        Map<String, String> correctDayFutureTime = randomEntityMap();
        correctDayFutureTime.put("startDateTime", LocalDateTime.of(pastDate(0, 0, 1), LocalTime.of(18, 30, 0)).toString());
        correctDayFutureTime.put("endDateTime", LocalDateTime.of(pastDate(0, 0, 1), LocalTime.of(19, 30, 0)).toString());

        Map<String, String> firstDateInRange = randomEntityMap();
        firstDateInRange.put("startDateTime", LocalDateTime.of(pastDate(0, 0, 2), LocalTime.of(9, 30, 0)).toString());
        firstDateInRange.put("endDateTime", LocalDateTime.of(pastDate(0, 0, 2), LocalTime.of(10, 15, 0)).toString());

        Map<String, String> secondDateInRange = randomEntityMap();
        secondDateInRange.put("startDateTime", LocalDateTime.of(pastDate(0, 0, 1), LocalTime.of(17, 0, 0)).toString());
        secondDateInRange.put("endDateTime", LocalDateTime.of(pastDate(0, 0, 1), LocalTime.of(18, 0, 0)).toString());

        Map<String, String> thirdDateInRange = randomEntityMap();
        thirdDateInRange.put("startDateTime", LocalDateTime.of(pastDate(0, 0, 1), LocalTime.of(1, 0)).toString());
        thirdDateInRange.put("endDateTime", LocalDateTime.of(pastDate(0, 0, 1), LocalTime.of(2, 0, 0)).toString());

        postWithUsernames(pastDate, workerUsername, creatorUsername);
        postWithUsernames(futureDate, workerUsername, creatorUsername);
        postWithUsernames(correctDayPastTime, workerUsername, creatorUsername);
        postWithUsernames(correctDayFutureTime, workerUsername, creatorUsername);
        HashMap<String, String> firstExpected = postWithUsernames(firstDateInRange, workerUsername, creatorUsername);
        HashMap<String, String> secondExpected = postWithUsernames(secondDateInRange, workerUsername, creatorUsername);
        HashMap<String, String> thirdExpected = postWithUsernames(thirdDateInRange, workerUsername, creatorUsername);

        List<RequestParameter> requestParameters = Arrays.asList(
                new RequestParameter("startDateTime", LocalDateTime.of(pastDate(0, 0, 2), LocalTime.of(9, 30, 0)).toString()),
                new RequestParameter("endDateTime", LocalDateTime.of(pastDate(0, 0, 1), LocalTime.of(17, 30, 0)).toString()),
                new RequestParameter("workerUsername", workerUsername),
                new RequestParameter("creatorUsername", creatorUsername)
        );

        successfulGetList(Arrays.asList(firstExpected, secondExpected, thirdExpected), getUrl("range", requestParameters));
    }

    @Test
    public void endDateBeforeEndDate() throws JsonProcessingException {
        List<RequestParameter> requestParameters = Arrays.asList(
                new RequestParameter("startDateTime", pastDateTime(0, 0, 1).toString()),
                new RequestParameter("endDateTime", pastDateTime(0, 0, 3).toString())
        );

        ResponseEntity<String> getResult = testRestTemplate.getForEntity(getUrl("range", requestParameters), String.class);

        assertThat(getResult.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(getResult.getBody()).isEqualTo("{\"field\":\"date range\",\"message\":\"start date must be above the end date\"}");
    }

    private HashMap<String, String> postWithUsernames(Map<String, String> post, String workerUsername, String creatorUsername) {
        successfulPost(post);

        HashMap<String, String> bothUsername = new HashMap<>(post);
        bothUsername.put("workerUsername", workerUsername);
        bothUsername.put("creatorUsername", creatorUsername);
        return successfulPost(bothUsername);

    }



}
