package sept.major.hours.controller.unit.get;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sept.major.common.response.ResponseError;
import sept.major.hours.controller.HoursController;
import sept.major.hours.controller.unit.HoursUnitTestHelper;
import sept.major.hours.entity.HoursEntity;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@SpringBootTest
class GetOnDateEndpointTests extends HoursUnitTestHelper {

    @Test
    void noDateException() {
        List<HoursEntity> expected = Arrays.asList(
                randomEntity(randomInt(4)),
                randomEntity(randomInt(4)),
                randomEntity(randomInt(4))
        );

        assertThatThrownBy(() -> hoursController.getHoursInDate(null, null, null))
                .isInstanceOf(RuntimeException.class).hasMessage("Received null date when the field is required by the endpoint");
    }

    @Test
    void invalidDate() {
        runTest(new ResponseEntity(new ResponseError("date", HoursController.INCORRECT_DATE_FORMAT_ERROR_MESSAGE), HttpStatus.BAD_REQUEST),
                null, "foo", null, null);
    }

    @Test
    void dateValid() {
        LocalDate date = pastDate(1, 2, 3);

        HoursEntity expected = randomEntityWithDate(randomInt(4), date);

        runTestsWithUsernameFilters(new ResponseEntity(Arrays.asList(expected), HttpStatus.ACCEPTED),
                Arrays.asList(expected), date.toString());
    }

    @Test
    void dateMissingResult() {
        runTestsWithUsernameFilters(new ResponseEntity("No records within provided bounds were found", HttpStatus.NOT_FOUND),
                Arrays.asList(), LocalDate.now().toString());
    }


    private void runTestsWithUsernameFilters(ResponseEntity expected, List<HoursEntity> returned, String date) {
        testWorkerUsernameFilter(expected, returned, date);
        testCustomerUsernameFilter(expected, returned, date);
        testCustomerAndWorkerUsernameFilter(expected, returned, date);
    }

    private void testWorkerUsernameFilter(ResponseEntity expected, List<HoursEntity> returned, String date) {
        String workerUsername = randomAlphanumericString(20);

        List<HoursEntity> workerUsernameEntities = deepCopy(returned);
        workerUsernameEntities.forEach(hoursEntity -> hoursEntity.setWorkerUsername(workerUsername));

        workerUsernameEntities.add(randomEntityWithDate(randomInt(4), LocalDate.parse(date)));

        runTest(updateExpectedWithUsername(expected, workerUsername, null), workerUsernameEntities, date, workerUsername, null);
    }

    private void testCustomerUsernameFilter(ResponseEntity expected, List<HoursEntity> returned, String date) {
        String customerUsername = randomAlphanumericString(20);

        List<HoursEntity> customerUsernameEntities = deepCopy(returned);
        customerUsernameEntities.forEach(hoursEntity -> hoursEntity.setCustomerUsername(customerUsername));

        customerUsernameEntities.add(randomEntityWithDate(randomInt(4), LocalDate.parse(date)));

        runTest(updateExpectedWithUsername(expected, null, customerUsername), customerUsernameEntities, date, null, customerUsername);
    }

    private void testCustomerAndWorkerUsernameFilter(ResponseEntity expected, List<HoursEntity> returned, String date) {
        String customerUsername = randomAlphanumericString(20);
        String workerUsername = randomAlphanumericString(20);

        List<HoursEntity> usernameEntities = deepCopy(returned);
        usernameEntities.forEach(hoursEntity -> hoursEntity.setCustomerUsername(customerUsername));
        usernameEntities.forEach(hoursEntity -> hoursEntity.setWorkerUsername(workerUsername));

        HoursEntity customerUsernameEntity = randomEntityWithDate(randomInt(4), LocalDate.parse(date));
        customerUsernameEntity.setCustomerUsername(customerUsername);
        usernameEntities.add(customerUsernameEntity);

        HoursEntity workerUsernameEntity = randomEntityWithDate(randomInt(4), LocalDate.parse(date));
        workerUsernameEntity.setWorkerUsername(workerUsername);
        usernameEntities.add(workerUsernameEntity);

        usernameEntities.add(randomEntityWithDate(randomInt(4), LocalDate.parse(date)));

        runTest(updateExpectedWithUsername(expected, workerUsername, customerUsername), usernameEntities, date, workerUsername, customerUsername);
    }


    private void runTest(ResponseEntity expected, List<HoursEntity> returned, String date, String workerUsername, String customerUsername) {
        try {
            if (date != null) {
                LocalDate parsedDate = LocalDate.parse(date);
                when(mockedUserRepository.findAllBetweenDates(parsedDate.atStartOfDay(), parsedDate.plusDays(1).atStartOfDay())).thenReturn(returned);
            }
        } catch (DateTimeParseException e) {

        }
        ResponseEntity result = hoursController.getHoursInDate(date, workerUsername, customerUsername);

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(expected.getStatusCode());
        assertThat(expected.getBody()).isEqualTo(result.getBody());
    }

    private ResponseEntity updateExpectedWithUsername(ResponseEntity expected, String workerUsername, String customerUsername) {
        Object expectedBody = expected.getBody();
        if(expectedBody instanceof HoursEntity) {
            HoursEntity hoursEntity = (HoursEntity) expectedBody;
            if(workerUsername != null) {
                hoursEntity.setWorkerUsername(workerUsername);
            }
            if(customerUsername != null) {
                hoursEntity.setCustomerUsername(customerUsername);
            }
            return new ResponseEntity(hoursEntity, expected.getStatusCode());
        }
        if(expectedBody instanceof List) {
            List<HoursEntity> entityList = (List<HoursEntity>) expectedBody;
            entityList.forEach(hoursEntity -> {
                if(workerUsername != null) {
                    hoursEntity.setWorkerUsername(workerUsername);
                }
                if(customerUsername != null) {
                    hoursEntity.setCustomerUsername(customerUsername);
                }
            });

            return new ResponseEntity(entityList, expected.getStatusCode());
        }

        return expected;
    }


}
