package sept.major.bookings.controller.unit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sept.major.bookings.entity.BookingEntity;
import sept.major.common.response.ValidationError;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static sept.major.bookings.BookingsTestHelper.*;

@SpringBootTest
class PatchEndpointTests extends UnitTestHelper {

    @Test
    @DisplayName("Successfully patch an entity")
    void valid() {
        Integer hoursId = randomInt(4);

        Map<String, String> existingMap = randomEntityMap(hoursId);
        BookingEntity existing = createBookingEntity(existingMap);

        HashMap patchValues = new HashMap() {{
            put("customerUsername", randomAlphanumericString(20));
        }};
        BookingEntity patchedEntity = patchEntity(existing, patchValues);  // Manually update the entity so we can compare the results

        runTest(hoursId, patchValues, Optional.of(existing), patchedEntity,
                new ResponseEntity(patchedEntity, HttpStatus.OK));
    }

    @Test
    @DisplayName("Successfully patch with no patch values")
    void noUpdate() {
        Integer hoursId = randomInt(4);

        Map<String, String> existingMap = randomEntityMap(hoursId);
        BookingEntity existing = createBookingEntity(existingMap);


        runTest(hoursId, new HashMap<>(), Optional.of(existing), existing,
                new ResponseEntity(existing, HttpStatus.OK));
    }


    @Test
    @DisplayName("Patch attempt on non existing entity")
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
    @DisplayName("Update not blank field to blank")
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
    @DisplayName("Update multiple not blank fields to blank")
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
    @DisplayName("Provided id isn't an integer")
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
        if (expected.getBody() instanceof List) {
            assertThat((List) result.getBody()).containsAll((List) expected.getBody());
        } else {
            assertThat(result.getBody()).isEqualTo(expected.getBody());
        }
        assertThat(result.getStatusCode()).isEqualTo(expected.getStatusCode());
    }
}
