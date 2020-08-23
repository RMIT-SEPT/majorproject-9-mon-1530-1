package sept.major.hours.controller.unit.get;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sept.major.hours.controller.unit.HoursUnitTestHelper;
import sept.major.hours.entity.HoursEntity;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static sept.major.hours.HoursTestHelper.randomAlphanumericString;
import static sept.major.hours.HoursTestHelper.randomEntity;

@SpringBootTest
class GetEndpointTests extends HoursUnitTestHelper {

    @Test
    void valid() {
        String id = randomAlphanumericString(20);

        HoursEntity expected = randomEntity(id);

        runTest(new ResponseEntity(expected, HttpStatus.OK),
                Optional.of(expected), id);
    }

    @Test
    void missingValue() {
        String id = randomAlphanumericString(20);

        runTest(new ResponseEntity(String.format("No record with a identifier of %s was found", id), HttpStatus.NOT_FOUND),
                Optional.empty(), id);
    }

    private void runTest(ResponseEntity expected, Optional<HoursEntity> returned, String id) {
        when(mockedUserRepository.findById(id)).thenReturn(returned);
        ResponseEntity result = hoursController.getHours(id);

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(expected.getStatusCode());
        assertThat(result.getBody()).isEqualTo(expected.getBody());
    }

}
