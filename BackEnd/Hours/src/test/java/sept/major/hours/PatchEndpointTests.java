package sept.major.hours;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sept.major.common.response.ResponseError;
import sept.major.hours.entity.HoursEntity;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
class PatchEndpointTests extends UserServiceTestHelper {

    @Test
    void valid() {
        String hoursId = randomAlphanumericString(4);

        Map<String, Object> existingMap = randomEntityMap(hoursId);
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
        String hoursId = randomAlphanumericString(4);

        Map<String, Object> existingMap = randomEntityMap(hoursId);
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
        String hoursId = randomAlphanumericString(4);

        Map<String, Object> existingMap = randomEntityMap(hoursId);
        HoursEntity existing = entityMapToEntity(existingMap);

        HashMap patchValues = new HashMap() {{
            put("customerUsername", " ");
        }};
        HoursEntity patchedEntity = patchEntity(existing, patchValues);

        runTest(hoursId, patchValues, Optional.of(existing), patchedEntity,
                new ResponseEntity(new HashSet<>(Arrays.asList(new ResponseError("customerUsername", "must not be blank"))), HttpStatus.BAD_REQUEST));
    }

    @Test
    void missingAllFields() {

        String hoursId = randomAlphanumericString(4);

        Map<String, Object> existingMap = randomEntityMap(hoursId);
        HoursEntity existing = entityMapToEntity(existingMap);

        HashMap patchValues = new HashMap() {{
            put("customerUsername", " ");
            put("workerUsername", " ");
            put("startDateTime", " ");
            put("endDateTime", " ");
        }};
        HoursEntity patchedEntity = patchEntity(existing, patchValues);

        runTest(hoursId, patchValues, Optional.of(existing), patchedEntity,
                new ResponseEntity(new HashSet<>(Arrays.asList(
                        new ResponseError("customerUsername", "must not be blank"),
                        new ResponseError("endDateTime", "must not be blank"),
                        new ResponseError("startDateTime", "must not be blank"),
                        new ResponseError("workerUsername", "must not be blank")
                )), HttpStatus.BAD_REQUEST));
    }


    @Test
    void listField() {
        String hoursId = randomAlphanumericString(4);

        Map<String, Object> existingMap = randomEntityMap(hoursId);
        HoursEntity existing = entityMapToEntity(existingMap);

        HashMap patchValues = new HashMap() {{
            put("customerUsername", Arrays.asList(randomAlphanumericString(20)));
        }};

        runTest(hoursId, patchValues, Optional.of(existing), null,
                new ResponseEntity(new HashSet(Arrays.asList(new ResponseError("customerUsername", "field expected to be class java.lang.String but received class java.util.Arrays$ArrayList"))), HttpStatus.BAD_REQUEST));
    }

    @Test
    void mapField() {
        String hoursId = randomAlphanumericString(4);

        Map<String, Object> existingMap = randomEntityMap(hoursId);
        HoursEntity existing = entityMapToEntity(existingMap);

        HashMap map = new HashMap();
        map.put("customerUsername", randomAlphanumericString(20));
        HashMap patchValues = new HashMap() {{
            put("customerUsername", map);
        }};

        runTest(hoursId, patchValues, Optional.of(existing), null,
                new ResponseEntity(new HashSet(Arrays.asList(new ResponseError("customerUsername", "field expected to be class java.lang.String but received class java.util.HashMap"))), HttpStatus.BAD_REQUEST));
    }


    private void runTest(String hoursId, Map<String, Object> patchValues, Optional<HoursEntity> existing, HoursEntity patchedEntity, ResponseEntity expected) {
        when(mockedUserRepository.findById(hoursId)).thenReturn(existing);
        when(mockedUserRepository.save(any())).thenReturn(patchedEntity);

        ResponseEntity result = hoursController.updateHours(hoursId, patchValues);

        assertThat(result).isNotNull();
        assertThat(result.getBody()).isEqualTo(expected.getBody());
        assertThat(result.getStatusCode()).isEqualTo(expected.getStatusCode());
    }

    private HoursEntity patchEntity(HoursEntity existing, HashMap<String, Object> patchValues) {

        HoursEntity newEntity = new HoursEntity(existing.getHoursId(), existing.getWorkerUsername(), existing.getCustomerUsername(), existing.getStartDateTime(), existing.getEndDateTime());

        if(patchValues.get("hoursId") != null) {
            newEntity.setHoursId((String) patchValues.get("hoursId"));
        }

        if(patchValues.get("workerUsername") != null) {
            newEntity.setHoursId((String) patchValues.get("workerUsername"));
        }

        if(patchValues.get("customerUsername") != null) {
            newEntity.setHoursId((String) patchValues.get("customerUsername"));
        }

        if(patchValues.get("startDateTime") != null) {
            newEntity.setHoursId((String) patchValues.get("startDateTime"));
        }

        if(patchValues.get("endDateTime") != null) {
            newEntity.setHoursId((String) patchValues.get("endDateTime"));
        }

        return newEntity;
    }

}
