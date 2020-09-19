package sept.major.bookings.blackbox;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sept.major.common.testing.RequestParameter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static sept.major.bookings.BookingsTestHelper.*;

public class GetBlackBoxTests extends BookingBlackBoxHelper {

    @Test
    public void getById() {
        List<Map<String, String>> postResults = Arrays.asList(successfulPost(randomEntityMap()), successfulPost(randomEntityMap()));

        List<RequestParameter> requestParameters = Arrays.asList(new RequestParameter("bookingId", "1"));

        successfulGet(postResults.get(0), getUrl(requestParameters));
    }

    @Test
    public void getAll() throws JsonProcessingException {
        String workerUsername = randomAlphanumericString(20);
        String customerUsername = randomAlphanumericString(20);

        HashMap<String, String> first = postWithUsernames(randomEntityMap(), workerUsername, customerUsername);
        HashMap<String, String> second = postWithUsernames(randomEntityMap(pastDateTime(0, 0, 1)), workerUsername, customerUsername);

        List<RequestParameter> requestParameters = Arrays.asList(
                new RequestParameter("workerUsername", workerUsername),
                new RequestParameter("customerUsername", customerUsername)

        );

        successfulGetList(Arrays.asList(first, second), getUrl("all", requestParameters));
    }

    @Test
    public void getDate() throws JsonProcessingException {

        String workerUsername = randomAlphanumericString(20);
        String customerUsername = randomAlphanumericString(20);

        Map<String, String> pastDate = randomEntityMap();
        pastDate.put("startDateTime", pastDateTime(0, 0, 1).toString());
        pastDate.put("endDateTime", pastDateTime(0, 0, 1).toString());

        Map<String, String> futureDate = randomEntityMap();
        futureDate.put("startDateTime", futureDateTime(0, 0, 1).toString());
        futureDate.put("endDateTime", futureDateTime(0, 0, 1).toString());

        Map<String, String> onDate = randomEntityMap();

        postWithUsernames(pastDate, workerUsername, customerUsername);
        postWithUsernames(futureDate, workerUsername, customerUsername);
        HashMap<String, String> expected = postWithUsernames(onDate, workerUsername, customerUsername);

        List<RequestParameter> requestParameters = Arrays.asList(
                new RequestParameter("date", LocalDate.now().toString()),
                new RequestParameter("workerUsername", workerUsername),
                new RequestParameter("customerUsername", customerUsername)
        );

        successfulGetList(Arrays.asList(expected), getUrl("date", requestParameters));
    }

    @Test
    public void getRange() throws JsonProcessingException {

        String workerUsername = randomAlphanumericString(20);
        String customerUsername = randomAlphanumericString(20);

        Map<String, String> pastDate = randomEntityMap();
        pastDate.put("startDateTime", pastDateTime(0, 0, 3).toString());
        pastDate.put("endDateTime", pastDateTime(0, 0, 3).toString());

        Map<String, String> futureDate = randomEntityMap();
        futureDate.put("startDateTime", futureDateTime(0, 0, 1).toString());
        futureDate.put("endDateTime", futureDateTime(0, 0, 1).toString());

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

        postWithUsernames(pastDate, workerUsername, customerUsername);
        postWithUsernames(futureDate, workerUsername, customerUsername);
        postWithUsernames(correctDayPastTime, workerUsername, customerUsername);
        postWithUsernames(correctDayFutureTime, workerUsername, customerUsername);
        HashMap<String, String> firstExpected = postWithUsernames(firstDateInRange, workerUsername, customerUsername);
        HashMap<String, String> secondExpected = postWithUsernames(secondDateInRange, workerUsername, customerUsername);
        HashMap<String, String> thirdExpected = postWithUsernames(thirdDateInRange, workerUsername, customerUsername);

        List<RequestParameter> requestParameters = Arrays.asList(
                new RequestParameter("startDateTime", LocalDateTime.of(pastDate(0, 0, 2), LocalTime.of(9, 30, 0)).toString()),
                new RequestParameter("endDateTime", LocalDateTime.of(pastDate(0, 0, 1), LocalTime.of(17, 30, 0)).toString()),
                new RequestParameter("workerUsername", workerUsername),
                new RequestParameter("customerUsername", customerUsername)
        );

        successfulGetList(Arrays.asList(firstExpected, secondExpected, thirdExpected), getUrl("range", requestParameters));
    }

    @Test
    public void endDateBeforeStartDate() throws JsonProcessingException {
        List<RequestParameter> requestParameters = Arrays.asList(
                new RequestParameter("startDateTime", pastDateTime(0, 0, 1).toString()),
                new RequestParameter("endDateTime", pastDateTime(0, 0, 3).toString())
        );

        ResponseEntity<String> getResult = testRestTemplate.getForEntity(getUrl("range", requestParameters), String.class);

        assertThat(getResult.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(getResult.getBody()).isEqualTo("{\"field\":\"date range\",\"message\":\"start date must be above the end date\"}");
    }

    private HashMap<String, String> postWithUsernames(Map<String, String> post, String workerUsername, String customerUsername) {
        successfulPost(post);

        HashMap<String, String> bothUsername = new HashMap<>(post);
        bothUsername.put("workerUsername", workerUsername);
        bothUsername.put("customerUsername", customerUsername);
        return successfulPost(bothUsername);

    }


}
