package sept.major.hours.controller.unit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sept.major.common.response.ValidationError;

import static org.assertj.core.api.Assertions.assertThat;
import static sept.major.hours.HoursTestHelper.randomAlphanumericString;
import static sept.major.hours.HoursTestHelper.randomInt;

public class DeleteEndpointTests extends UnitTestHelper {

    @Test
    @DisplayName("A entity is correctly deleted")
    void valid() {
        int hoursId = randomInt(4);

        Mockito.doNothing().when(mockedHoursRepository).deleteById(hoursId);

        ResponseEntity result = hoursController.deleteHours(Integer.toString(hoursId));

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo("Record successfully deleted");
    }

    @Test
    @DisplayName("No entity to delete")
    void missing() {
        int hoursId = randomInt(4);

        Mockito.doThrow(new EmptyResultDataAccessException(1)).when(mockedHoursRepository).deleteById(hoursId);

        ResponseEntity result = hoursController.deleteHours(Integer.toString(hoursId));

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(result.getBody()).isEqualTo(String.format("No record with a identifier of %s was found", hoursId));
    }

    @Test
    @DisplayName("Provided id was not an integer")
    void IdInvalid() {
        ResponseEntity result = hoursController.deleteHours(randomAlphanumericString(4));

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(result.getBody()).isEqualTo(new ValidationError("id", "must be an integer (whole number)"));
    }
}
