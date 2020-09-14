package sept.major.bookings.controller.unit;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sept.major.bookings.entity.BookingEntity;
import sept.major.common.response.ValidationError;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static sept.major.bookings.BookingsTestHelper.*;

@SpringBootTest
class PatchEndpointTests extends UnitTestHelper {

    @Test
    void valid() {
        Integer hoursId = randomInt(4);

        Map<String, String> existingMap = randomEntityMap(hoursId);
        BookingEntity existing = createBookingEntity(existingMap);

        HashMap patchValues = new HashMap() {{
            put("customerUsername", randomAlphanumericString(20));
        }};
        BookingEntity patchedEntity = patchEntity(existing, patchValues);

        runTest(hoursId, patchValues, Optional.of(existing), patchedEntity,
                new ResponseEntity(patchedEntity, HttpStatus.OK));
    }

    @Test
    void noExisting() {
        Integer hoursId = randomInt(4);

        Map<String, String> existingMap = randomEntityMap(hoursId);
        BookingEntity existing = createBookingEntity(existingMap);

        HashMap patchValues = new HashMap() {{
            put("customerUsername", randomAlphanumericString(20));
        }};
        BookingEntity patchedEntity = patchEntity(existing, patchValues);

        runTest(hoursId, patchValues, Optional.empty(), patchedEntity,
                new ResponseEntity(new ValidationError("Identifier field", String.format("No record with a identifier of %s was found", hoursId)), HttpStatus.NOT_FOUND));
    }

    @Test
    void emptyFieldUpdate() {
        Integer hoursId = randomInt(4);

        Map<String, String> existingMap = randomEntityMap(hoursId);
        BookingEntity existing = createBookingEntity(existingMap);

        HashMap patchValues = new HashMap() {{
            put("customerUsername", " ");
        }};
        BookingEntity patchedEntity = patchEntity(existing, patchValues);

        runTest(hoursId, patchValues, Optional.of(existing), patchedEntity,
                new ResponseEntity((Arrays.asList(new ValidationError("customerUsername", "must not be blank"))), HttpStatus.BAD_REQUEST));
    }

    @Test
    void missingFields() {

        Integer hoursId = randomInt(4);

        Map<String, String> existingMap = randomEntityMap(hoursId);
        BookingEntity existing = createBookingEntity(existingMap);

        HashMap patchValues = new HashMap() {{
            put("customerUsername", " ");
            put("workerUsername", " ");
        }};
        BookingEntity patchedEntity = patchEntity(existing, patchValues);

        runTest(hoursId, patchValues, Optional.of(existing), patchedEntity,
                new ResponseEntity((Arrays.asList(
                        new ValidationError("customerUsername", "must not be blank"),
                        new ValidationError("workerUsername", "must not be blank")
                )), HttpStatus.BAD_REQUEST));
    }

    @Test
    void idInvalid() {
        String hoursId = "foo";

        ResponseEntity result = bookingServiceController.updateHours(hoursId, new HashMap());

        assertThat(result).isNotNull();
        assertThat(result.getBody()).isEqualTo(new ValidationError("id", "must be an integer (whole number)"));
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }


    private void runTest(Integer hoursId, Map<String, String> patchValues, Optional<BookingEntity> existing, BookingEntity patchedEntity, ResponseEntity expected) {
        when(mockedBookingRepository.findById(hoursId)).thenReturn(existing);
        when(mockedBookingRepository.save(any())).thenReturn(patchedEntity);

        ResponseEntity result = bookingServiceController.updateHours(hoursId.toString(), patchValues);

        assertThat(result).isNotNull();
        assertThat(result.getBody()).isEqualTo(expected.getBody());
        assertThat(result.getStatusCode()).isEqualTo(expected.getStatusCode());
    }

    private BookingEntity patchEntity(BookingEntity existing, HashMap<String, String> patchValues) {

        BookingEntity newEntity = new BookingEntity(existing.getWorkerUsername(), existing.getCustomerUsername(), existing.getStartDateTime(), existing.getEndDateTime());

        if (patchValues.get("hoursId") != null) {
            newEntity.setBookingId(new Integer(patchValues.get("hoursId")));
        }

        if (patchValues.get("workerUsername") != null) {
            newEntity.setWorkerUsername(patchValues.get("workerUsername"));
        }

        if (patchValues.get("customerUsername") != null) {
            newEntity.setCustomerUsername(patchValues.get("customerUsername"));
        }

        if (patchValues.get("startDateTime") != null) {
            newEntity.setStartDateTime(LocalDateTime.parse(patchValues.get("startDateTime")));
        }

        if (patchValues.get("endDateTime") != null) {
            newEntity.setEndDateTime(LocalDateTime.parse(patchValues.get("endDateTime")));
        }

        return newEntity;
    }

}
