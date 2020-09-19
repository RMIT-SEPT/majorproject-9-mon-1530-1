package sept.major.hours.controller.unit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sept.major.common.response.ValidationError;
import sept.major.hours.entity.HoursEntity;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static sept.major.hours.HoursTestHelper.*;

@SpringBootTest
class PatchEndpointTests extends UnitTestHelper {

    @Test
    @DisplayName("Successfully patch an entity")
    void valid() {
        Integer hoursId = randomInt(4);

        Map<String, String> existingMap = randomEntityMap(hoursId);
        HoursEntity existing = createHoursEntity(existingMap);

        HashMap patchValues = new HashMap() {{
            put("creatorUsername", randomAlphanumericString(20));
        }};
        HoursEntity patchedEntity = patchEntity(existing, patchValues);  // Manually update the entity so we can compare the results

        runTest(hoursId, patchValues, Optional.of(existing), patchedEntity,
                new ResponseEntity(patchedEntity, HttpStatus.OK));
    }

    @Test
    @DisplayName("Successfully patch with no patch values")
    void noUpdate() {
        Integer hoursId = randomInt(4);

        Map<String, String> existingMap = randomEntityMap(hoursId);
        HoursEntity existing = createHoursEntity(existingMap);


        runTest(hoursId, new HashMap<>(), Optional.of(existing), existing,
                new ResponseEntity(existing, HttpStatus.OK));
    }


    @Test
    @DisplayName("Patch attempt on non existing entity")
    void noExisting() {
        Integer hoursId = randomInt(4);

        Map<String, String> existingMap = randomEntityMap(hoursId);
        HoursEntity existing = createHoursEntity(existingMap);

        HashMap patchValues = new HashMap() {{
            put("creatorUsername", randomAlphanumericString(20));
        }};
        HoursEntity patchedEntity = patchEntity(existing, patchValues);

        runTest(hoursId, patchValues, Optional.empty(), patchedEntity,
                new ResponseEntity(new ValidationError("Identifier field", String.format("No record with a identifier of %s was found", hoursId)), HttpStatus.NOT_FOUND));
    }

    @Test
    @DisplayName("Update not blank field to blank")
    void emptyFieldUpdate() {
        Integer hoursId = randomInt(4);

        Map<String, String> existingMap = randomEntityMap(hoursId);
        HoursEntity existing = createHoursEntity(existingMap);

        HashMap patchValues = new HashMap() {{
            put("creatorUsername", " ");
        }};
        HoursEntity patchedEntity = patchEntity(existing, patchValues);

        runTest(hoursId, patchValues, Optional.of(existing), patchedEntity,
                new ResponseEntity((Arrays.asList(new ValidationError("creatorUsername", "must not be blank"))), HttpStatus.BAD_REQUEST));
    }

    @Test
    @DisplayName("Update multiple not blank fields to blank")
    void missingFields() {

        Integer hoursId = randomInt(4);

        Map<String, String> existingMap = randomEntityMap(hoursId);
        HoursEntity existing = createHoursEntity(existingMap);

        HashMap patchValues = new HashMap() {{
            put("creatorUsername", " ");
            put("workerUsername", " ");
        }};
        HoursEntity patchedEntity = patchEntity(existing, patchValues);

        runTest(hoursId, patchValues, Optional.of(existing), patchedEntity,
                new ResponseEntity((Arrays.asList(
                        new ValidationError("creatorUsername", "must not be blank"),
                        new ValidationError("workerUsername", "must not be blank")
                )), HttpStatus.BAD_REQUEST));
    }

    @Test
    @DisplayName("Provided id isn't an integer")
    void idInvalid() {
        String hoursId = "foo";

        ResponseEntity result = hoursController.updateHours(hoursId, new HashMap());

        assertThat(result).isNotNull();
        assertThat(result.getBody()).isEqualTo(new ValidationError("id", "must be an integer (whole number)"));
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }


    private void runTest(Integer hoursId, Map<String, String> patchValues, Optional<HoursEntity> existing, HoursEntity patchedEntity, ResponseEntity expected) {
        when(mockedHoursRepository.findById(hoursId)).thenReturn(existing);
        when(mockedHoursRepository.save(any())).thenReturn(patchedEntity);

        ResponseEntity result = hoursController.updateHours(hoursId.toString(), patchValues);

        assertThat(result).isNotNull();
        if (expected.getBody() instanceof List) {
            assertThat((List) result.getBody()).containsAll((List) expected.getBody());
        } else {
            assertThat(result.getBody()).isEqualTo(expected.getBody());
        }
        assertThat(result.getStatusCode()).isEqualTo(expected.getStatusCode());
    }
}
