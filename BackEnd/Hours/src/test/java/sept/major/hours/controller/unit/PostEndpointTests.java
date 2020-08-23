package sept.major.hours.controller.unit;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sept.major.common.response.ResponseError;
import sept.major.hours.entity.HoursEntity;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class PostEndpointTests extends HoursUnitTestHelper {

    @Test
    void valid() {
        Map<String, String> input = randomEntityMap(null);
        HoursEntity inputEntity = entityMapToEntity(input);

        runTest(input, new ResponseEntity(inputEntity, HttpStatus.OK), Optional.empty());
    }

    @Test
    void missingField() {
        Map<String, String> input = randomEntityMap(null);
        input.remove("customerUsername");
        HoursEntity inputEntity = entityMapToEntity(input);

        runTest(input, new ResponseEntity(new HashSet<>(Arrays.asList(new ResponseError("customerUsername", "must not be blank"))), HttpStatus.BAD_REQUEST), null);
    }


    private void runTest(Map<String, String> input, ResponseEntity expected, Optional<HoursEntity> returned) {
        when(mockedUserRepository.findById(any())).thenReturn(returned);
        HoursEntity inputEntity = entityMapToEntity(input);
        when(mockedUserRepository.save(inputEntity)).thenReturn(inputEntity);

        ResponseEntity result = hoursController.createHours(input);

        assertThat(result).isNotNull();
        assertThat(result.getBody()).isEqualTo(expected.getBody());
        assertThat(result.getStatusCode()).isEqualTo(expected.getStatusCode());
    }

}
