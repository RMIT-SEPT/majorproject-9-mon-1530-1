package sept.major.availability.service.blackbox.mock.list.bookings;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;
import org.mockserver.model.Parameter;
import org.springframework.http.HttpStatus;
import sept.major.availability.service.blackbox.mock.MockList;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static sept.major.availability.service.blackbox.mock.MockServices.getBookingsResponse;

@AllArgsConstructor
public class BookingsRangeRequestsMockList extends MockList {
    @Getter
    private final String path = "/bookings/range";

    @Getter
    private Integer port;

    @Getter
    private String host;

    public void createMocks() {
        validRangeWithBothUsernames();
        validRangeWithWorkerUsername();
        validRangeWithCustomerUsername();
        invalidRangeRequest();
        missingRangeRequest();
        validRangeNoUsernames();
    }

    @SneakyThrows
    private void validRangeNoUsernames() {
        generateValidResponseMock((Arrays.asList(
                getBookingsResponse(
                        0,
                        "firstWorker",
                        "firstCustomer",
                        LocalDate.now().minusDays(1).atTime(11, 0, 0),
                        LocalDate.now().minusDays(1).atTime(13, 0, 0)),
                getBookingsResponse(
                        1,
                        "firstWorker",
                        "secondCustomer",
                        LocalDate.now().minusDays(1).atTime(19, 0, 0),
                        LocalDate.now().minusDays(1).atTime(20, 0, 0)),
                getBookingsResponse(
                        2,
                        "secondWorker",
                        "firstCustomer",
                        LocalDate.now().atTime(10, 0, 0),
                        LocalDate.now().atTime(11, 0, 0))
        )), Arrays.asList(
                new Parameter("startDateTime", LocalDate.now().minusDays(1).atTime(9, 0, 0).toString()),
                new Parameter("endDateTime", LocalDate.now().atTime(17, 0, 0).toString())
        ));
    }

    @SneakyThrows
    private void validRangeWithWorkerUsername() {
        generateValidResponseMock((Arrays.asList(
                getBookingsResponse(
                        0,
                        "firstWorker",
                        "firstCustomer",
                        LocalDate.now().minusDays(1).atTime(11, 0, 0),
                        LocalDate.now().minusDays(1).atTime(13, 0, 0)),
                getBookingsResponse(
                        1,
                        "firstWorker",
                        "secondCustomer",
                        LocalDate.now().minusDays(1).atTime(19, 0, 0),
                        LocalDate.now().minusDays(1).atTime(20, 0, 0))
        )), Arrays.asList(
                new Parameter("startDateTime", LocalDate.now().minusDays(1).atTime(9, 0, 0).toString()),
                new Parameter("endDateTime", LocalDate.now().atTime(17, 0, 0).toString()),
                new Parameter("workerUsername", "firstWorker")
        ));
    }

    @SneakyThrows
    private void validRangeWithCustomerUsername() {
        generateValidResponseMock((Arrays.asList(
                getBookingsResponse(
                        0,
                        "firstWorker",
                        "firstCustomer",
                        LocalDate.now().minusDays(1).atTime(11, 0, 0),
                        LocalDate.now().minusDays(1).atTime(13, 0, 0)),
                getBookingsResponse(
                        2,
                        "secondWorker",
                        "firstCustomer",
                        LocalDate.now().atTime(10, 0, 0),
                        LocalDate.now().atTime(11, 0, 0))
        )), Arrays.asList(
                new Parameter("startDateTime", LocalDate.now().minusDays(1).atTime(9, 0, 0).toString()),
                new Parameter("endDateTime", LocalDate.now().atTime(17, 0, 0).toString()),
                new Parameter("customerUsername", "firstCustomer")
        ));
    }

    @SneakyThrows
    private void validRangeWithBothUsernames() {
        generateValidResponseMock((Arrays.asList(
                getBookingsResponse(
                        0,
                        "firstWorker",
                        "firstCustomer",
                        LocalDate.now().minusDays(1).atTime(11, 0, 0),
                        LocalDate.now().minusDays(1).atTime(13, 0, 0))
        )), Arrays.asList(
                new Parameter("startDateTime", LocalDate.now().minusDays(1).atTime(9, 0, 0).toString()),
                new Parameter("endDateTime", LocalDate.now().atTime(17, 0, 0).toString()),
                new Parameter("customerUsername", "firstCustomer"),
                new Parameter("workerUsername", "firstWorker")
        ));
    }

    @SneakyThrows
    private void invalidRangeRequest() {
        Map<String, String> response = new HashMap<>();
        response.put("field", "date range");
        response.put("message", "start date must be above the end date");

        generateMock(HttpStatus.BAD_REQUEST, new ObjectMapper().writeValueAsString(response), Arrays.asList(
                new Parameter("startDateTime", LocalDate.now().minusDays(2).atTime(9, 0, 0).toString()),
                new Parameter("endDateTime", LocalDate.now().minusDays(3).atTime(17, 0, 0).toString())
        ));
    }

    @SneakyThrows
    private void missingRangeRequest() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "No records within provided bounds were found");

        generateMock(HttpStatus.NOT_FOUND, new ObjectMapper().writeValueAsString(response), Arrays.asList(
                new Parameter("startDateTime", LocalDate.now().minusDays(1).atTime(9, 0, 0).toString()),
                new Parameter("endDateTime", LocalDate.now().atTime(17, 0, 0).toString()),
                new Parameter("customerUsername", "missing")
        ));
    }
}
