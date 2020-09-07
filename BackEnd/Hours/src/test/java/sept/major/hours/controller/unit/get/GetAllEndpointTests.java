package sept.major.hours.controller.unit.get;

import javafx.util.Pair;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sept.major.hours.HoursTestHelper;
import sept.major.hours.controller.unit.HoursUnitTestHelper;
import sept.major.hours.entity.HoursEntity;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static sept.major.hours.HoursTestHelper.*;

@SpringBootTest
class GetAllEndpointTests extends HoursUnitTestHelper {

    @Test
    void valid() {
        List<HoursEntity> expected = Arrays.asList(
                randomEntity(randomInt(4)),
                randomEntity(randomInt(4)),
                randomEntity(randomInt(4))
        );

        runTest(new ResponseEntity(expected, HttpStatus.ACCEPTED),
                expected, null, null);
    }

    @Test
    void missingResult() {
        runTest(new ResponseEntity("No records within provided bounds were found", HttpStatus.NOT_FOUND),
                Arrays.asList(), null, null);
    }

    @Test
    void workerUsernameProvidedValid() {
        HoursEntity expected = randomEntity(randomInt(4));
        String workerUsername = randomAlphanumericString(20);
        expected.setWorkerUsername(workerUsername);

        List<HoursEntity> returned = Arrays.asList(
                randomEntity(randomInt(4)),
                expected,
                randomEntity(randomInt(4))
        );

        runTest(new ResponseEntity(Arrays.asList(expected), HttpStatus.ACCEPTED),
                returned, workerUsername, null);
    }

    @Test
    void workerUsernameProvidedMissing() {
        List<HoursEntity> returned = Arrays.asList(
                randomEntity(randomInt(4)),
                randomEntity(randomInt(4)),
                randomEntity(randomInt(4))
        );

        runTest(new ResponseEntity("No records within provided bounds were found", HttpStatus.NOT_FOUND),
                returned, randomAlphanumericString(4), null);
    }

    @Test
    void creatorUsernameProvidedValid() {
        HoursEntity expected = randomEntity(randomInt(4));
        String creatorUsername = randomAlphanumericString(20);
        expected.setCreatorUsername(creatorUsername);

        List<HoursEntity> returned = Arrays.asList(
                randomEntity(randomInt(4)),
                expected,
                randomEntity(randomInt(4))
        );

        runTest(new ResponseEntity(Arrays.asList(expected), HttpStatus.ACCEPTED),
                returned, null, creatorUsername);
    }

    @Test
    void creatorUsernameProvidedMissing() {
        List<HoursEntity> returned = Arrays.asList(
                randomEntity(randomInt(4)),
                randomEntity(randomInt(4)),
                randomEntity(randomInt(4))
        );

        runTest(new ResponseEntity("No records within provided bounds were found", HttpStatus.NOT_FOUND),
                returned, null, randomAlphanumericString(4));
    }

    @Test
    void creatorUsernameAndWorkerUsernameProvidedValid() {
        String creatorUsername = randomAlphanumericString(20);
        String workerUsername = randomAlphanumericString(20);

        HoursEntity expected = randomEntity(randomInt(4));
        expected.setCreatorUsername(creatorUsername);
        expected.setWorkerUsername(workerUsername);

        HoursEntity entityOne = randomEntity(randomInt(4));
        entityOne.setCreatorUsername(creatorUsername);

        HoursEntity entityTwo = randomEntity(randomInt(4));
        entityTwo.setWorkerUsername(workerUsername);

        List<HoursEntity> returned = Arrays.asList(
                entityOne, expected, entityTwo
        );

        runTest(new ResponseEntity(Arrays.asList(expected), HttpStatus.ACCEPTED),
                returned, workerUsername, creatorUsername);
    }

    @Test
    void creatorUsernameAndWorkerUsernameProvidedMissing() {
        String creatorUsername = randomAlphanumericString(20);
        String workerUsername = randomAlphanumericString(20);

        HoursEntity entityOne = randomEntity(randomInt(4));
        entityOne.setCreatorUsername(creatorUsername);

        HoursEntity entityTwo = randomEntity(randomInt(4));
        entityTwo.setWorkerUsername(workerUsername);

        List<HoursEntity> returned = Arrays.asList(
                entityOne, randomEntity(randomInt(4)), entityTwo
        );

        runTest(new ResponseEntity("No records within provided bounds were found", HttpStatus.NOT_FOUND),
                returned, workerUsername, creatorUsername);
    }

    // TOOD DELETE THIS
    @Test
    void quickTest() {

        HashMap<String, Pair<LocalDateTime, LocalDateTime>> dates = new HashMap<>();

        Pair<LocalDateTime, LocalDateTime> pastDate = new Pair<>(HoursTestHelper.pastDateTime(0, 0, 3),
                HoursTestHelper.pastDateTime(0, 0, 3));
        dates.put("pastDate", pastDate);

        Pair<LocalDateTime, LocalDateTime> futureDate = new Pair<>(HoursTestHelper.futureDateTime(0, 0, 1),
                HoursTestHelper.futureDateTime(0, 0, 1));
        dates.put("futureDate", futureDate);

        Pair<LocalDateTime, LocalDateTime> correctDayPastTime = new Pair<>(LocalDateTime.of(pastDate(0, 0, 2), LocalTime.of(7, 30, 0)),
                LocalDateTime.of(pastDate(0, 0, 2), LocalTime.of(8, 30, 0)));
        dates.put("correctDayPastTime", correctDayPastTime);

        Pair<LocalDateTime, LocalDateTime> correctDayFutureTime = new Pair<>(LocalDateTime.of(pastDate(0, 0, 1), LocalTime.of(18, 30, 0)),
                LocalDateTime.of(pastDate(0, 0, 1), LocalTime.of(19, 30, 0)));
        dates.put("correctDayFutureTime", correctDayFutureTime);

        Pair<LocalDateTime, LocalDateTime> firstDateInRange = new Pair<>(LocalDateTime.of(pastDate(0, 0, 2), LocalTime.of(9, 30, 0)),
                LocalDateTime.of(pastDate(0, 0, 2), LocalTime.of(10, 15, 0)));
        dates.put("firstDateInRange", firstDateInRange);

        Pair<LocalDateTime, LocalDateTime> secondDateInRange = new Pair<>(LocalDateTime.of(pastDate(0, 0, 1), LocalTime.of(17, 0, 0)),
                LocalDateTime.of(pastDate(0, 0, 1), LocalTime.of(18, 0, 0)));
        dates.put("secondDateInRange", secondDateInRange);

        Pair<LocalDateTime, LocalDateTime> thirdDateInRange = new Pair<>(LocalDateTime.of(pastDate(0, 0, 1), LocalTime.of(1, 0)),
                LocalDateTime.of(pastDate(0, 0, 1), LocalTime.of(2, 0, 0)));
        dates.put("thirdDateInRange", thirdDateInRange);


        LocalDateTime lower = LocalDateTime.of(pastDate(0, 0, 2), LocalTime.of(9, 30, 0));
        LocalDateTime upper = LocalDateTime.of(pastDate(0, 0, 1), LocalTime.of(17, 30, 0));

        for (Map.Entry<String, Pair<LocalDateTime, LocalDateTime>> entry : dates.entrySet()) {
            LocalDateTime entryLower = entry.getValue().getKey();
            LocalDateTime entryHigher = entry.getValue().getValue();
            if (((lower.isBefore(entryLower) || lower.isEqual(entryLower)) && (upper.isBefore(entryLower) || upper.isEqual(entryLower)))
                    || (lower.isAfter(entryHigher))) {
                System.out.println(entry.getKey() + " Not in range");
            } else {
                System.out.println(entry.getKey() + " In range");
            }
        }
    }

    private void runTest(ResponseEntity expected, List<HoursEntity> returned, String workerUsername, String creatorUsername) {
        when(mockedUserRepository.findAll()).thenReturn(returned);
        ResponseEntity result = hoursController.getAllHours(workerUsername, creatorUsername);

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(expected.getStatusCode());
        assertThat(result.getBody()).isEqualTo(expected.getBody());
    }


}
