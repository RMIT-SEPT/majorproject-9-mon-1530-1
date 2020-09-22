package sept.major.hours.controller.unit.get;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sept.major.common.response.ValidationError;
import sept.major.hours.entity.HoursEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static sept.major.hours.HoursTestHelper.randomEntity;

@SpringBootTest
public class GetAllEndpointTests extends GetEndpointUnitTestHelper {

    @Test
    @DisplayName("Successfully retrieve one record")
    void valid() {
        List<HoursEntity> expected = Arrays.asList(
                randomEntity(),
                randomEntity(),
                randomEntity()
        );

        testWithUsernameFilters(new ResponseEntity(expected, HttpStatus.OK),
                expected);
    }


    @Test
    @DisplayName("No record found")
    void missingResult() {
        testWithUsernameFilters(new ResponseEntity("No records within provided bounds were found", HttpStatus.NOT_FOUND),
                Arrays.asList());
    }


    @Test
    @DisplayName("Successfully retrieve many records")
    void retrieveMany() {
        List<HoursEntity> hoursEntities = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            hoursEntities.add(randomEntity());
        }
        testWithUsernameFilters(new ResponseEntity(hoursEntities, HttpStatus.OK), hoursEntities);
    }

    protected void testWithUsernameFilters(ResponseEntity expected, List<HoursEntity> returned) {
        super.testWithUsernameFilters(expected, returned, null, null);
    }

    @Test
    @DisplayName("Invalid usernames provided")
    void invalidUsernameProvided() {
        ResponseEntity expected = new ResponseEntity(new ValidationError("workerUsername", "must be a valid username"), HttpStatus.BAD_REQUEST);

        String startDate = LocalDateTime.now().toString();
        String endDate = LocalDateTime.now().toString();

        runTest(expected, Collections.emptyList(), startDate, endDate, "null", null);
        runTest(expected, Collections.emptyList(), startDate, endDate, "", null);
        runTest(expected, Collections.emptyList(), startDate, endDate, "  ", null);
        runTest(expected, Collections.emptyList(), startDate, endDate, "\t", null);
        runTest(expected, Collections.emptyList(), startDate, endDate, "   \t   ", null);

        expected = new ResponseEntity(new ValidationError("creatorUsername", "must be a valid username"), HttpStatus.BAD_REQUEST);

        runTest(expected, Collections.emptyList(), startDate, endDate, null, "null");
        runTest(expected, Collections.emptyList(), startDate, endDate, null, "");
        runTest(expected, Collections.emptyList(), startDate, endDate, null, "  ");
        runTest(expected, Collections.emptyList(), startDate, endDate, null, "\t");
        runTest(expected, Collections.emptyList(), startDate, endDate, null, "   \t  ");
    }


    @Override
    protected void runTest(ResponseEntity expected, List<HoursEntity> returned, String startDate, String endDate, String workerUsername, String creatorUsername) {
        when(mockedHoursRepository.findAll()).thenReturn(returned);
        ResponseEntity result = hoursController.getAllHours(workerUsername, creatorUsername);

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(expected.getStatusCode());
        assertThat(result.getBody()).isEqualTo(expected.getBody());
    }


}
