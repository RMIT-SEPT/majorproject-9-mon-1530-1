package sept.major.hours.controller.unit;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sept.major.common.response.ResponseError;
import sept.major.hours.entity.HoursEntity;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static sept.major.hours.HoursTestHelper.*;

@SpringBootTest
class PatchEndpointTests extends HoursUnitTestHelper {

    @Test
    void valid() {
        Integer hoursId = randomInt(4);

        Map<String, String> existingMap = randomEntityMap(hoursId);
        HoursEntity existing = entityMapToEntity(existingMap);

        HashMap patchValues = new HashMap() {{
            put("customerUsername", randomAlphanumericString(20));
        }};
        HoursEntity patchedEntity = patchEntity(existing, patchValues);

        runTest(hoursId, patchValues, Optional.of(existing), patchedEntity,
                new ResponseEntity(patchedEntity, HttpStatus.OK));
    }

    @Test
    void noExisting() {
        Integer hoursId = randomInt(4);

        Map<String, String> existingMap = randomEntityMap(hoursId);
        HoursEntity existing = entityMapToEntity(existingMap);

        HashMap patchValues = new HashMap() {{
            put("customerUsername", randomAlphanumericString(20));
        }};
        HoursEntity patchedEntity = patchEntity(existing, patchValues);

        runTest(hoursId, patchValues, Optional.empty(), patchedEntity,
                new ResponseEntity(new ResponseError("Identifier field", String.format("No record with a identifier of %s was found", hoursId)), HttpStatus.NOT_FOUND));
    }

    @Test
    void emptyFieldUpdate() {
        Integer hoursId = randomInt(4);

        Map<String, String> existingMap = randomEntityMap(hoursId);
        HoursEntity existing = entityMapToEntity(existingMap);

        HashMap patchValues = new HashMap() {{
            put("customerUsername", " ");
        }};
        HoursEntity patchedEntity = patchEntity(existing, patchValues);

        runTest(hoursId, patchValues, Optional.of(existing), patchedEntity,
                new ResponseEntity(new HashSet<>(Arrays.asList(new ResponseError("customerUsername", "must not be blank"))), HttpStatus.BAD_REQUEST));
    }

    @Test
    void missingFields() {

        Integer hoursId = randomInt(4);

        Map<String, String> existingMap = randomEntityMap(hoursId);
        HoursEntity existing = entityMapToEntity(existingMap);

        HashMap patchValues = new HashMap() {{
            put("customerUsername", " ");
            put("workerUsername", " ");
        }};
        HoursEntity patchedEntity = patchEntity(existing, patchValues);

        runTest(hoursId, patchValues, Optional.of(existing), patchedEntity,
                new ResponseEntity(new HashSet<>(Arrays.asList(
                        new ResponseError("customerUsername", "must not be blank"),
                        new ResponseError("workerUsername", "must not be blank")
                )), HttpStatus.BAD_REQUEST));
    }


    private void runTest(Integer hoursId, Map<String, String> patchValues, Optional<HoursEntity> existing, HoursEntity patchedEntity, ResponseEntity expected) {
        when(mockedUserRepository.findById(hoursId)).thenReturn(existing);
        when(mockedUserRepository.save(any())).thenReturn(patchedEntity);

        ResponseEntity result = hoursController.updateHours(hoursId.toString(), patchValues);

        assertThat(result).isNotNull();
        assertThat(result.getBody()).isEqualTo(expected.getBody());
        assertThat(result.getStatusCode()).isEqualTo(expected.getStatusCode());
    }

    private HoursEntity patchEntity(HoursEntity existing, HashMap<String, String> patchValues) {

        HoursEntity newEntity = new HoursEntity(existing.getWorkerUsername(), existing.getCustomerUsername(), existing.getStartDateTime(), existing.getEndDateTime());

        if(patchValues.get("hoursId") != null) {
            newEntity.setHoursId(new Integer(patchValues.get("hoursId")));
        }

        if(patchValues.get("workerUsername") != null) {
            newEntity.setWorkerUsername(patchValues.get("workerUsername"));
        }

        if(patchValues.get("customerUsername") != null) {
            newEntity.setCustomerUsername(patchValues.get("customerUsername"));
        }

        if(patchValues.get("startDateTime") != null) {
            newEntity.setStartDateTime(LocalDateTime.parse(patchValues.get("startDateTime")));
        }

        if(patchValues.get("endDateTime") != null) {
            newEntity.setEndDateTime(LocalDateTime.parse(patchValues.get("endDateTime")));
        }

        return newEntity;
    }

}
