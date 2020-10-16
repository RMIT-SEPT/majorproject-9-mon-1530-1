package sept.major.availability.service.blackbox.mock.list.bookings;

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
public class BookingsDateRequestsMockList extends MockList {
    @Getter
    private final String path = "/bookings/date";

    @Getter
    private Integer port;

    @Getter
    private String host;

    public void createMocks() {
        validDateWithBothUsernames();
        validDateWithWorkerUsername();
        validDateWithCustomerUsername();
        invalidDateRequest();
        missingDateRequest();
        validDateNoUsernames();
    }

    @SneakyThrows
    private void validDateNoUsernames() {
        generateValidResponseMock((Arrays.asList(
                getBookingsResponse(
                        0,
                        "firstWorker",
                        "firstCustomer",
                        LocalDate.now().minusDays(1).atTime(4, 0, 0),
                        LocalDate.now().minusDays(1).atTime(5, 0, 0)),
                getBookingsResponse(
                        1,
                        "secondWorker",
                        "firstCustomer",
                        LocalDate.now().minusDays(1).atTime(13, 0, 0),
                        LocalDate.now().minusDays(1).atTime(14, 0, 0)),
                getBookingsResponse(
                        2,
                        "firstWorker",
                        "secondCustomer",
                        LocalDate.now().minusDays(1).atTime(20, 0, 0),
                        LocalDate.now().minusDays(1).atTime(21, 0, 0))
        )), Arrays.asList(
                new Parameter("date", LocalDate.now().minusDays(1).toString())
        ));

    }

    @SneakyThrows
    private void validDateWithWorkerUsername() {
        generateValidResponseMock((Arrays.asList(
                getBookingsResponse(
                        0,
                        "firstWorker",
                        "firstCustomer",
                        LocalDate.now().minusDays(1).atTime(4, 0, 0),
                        LocalDate.now().minusDays(1).atTime(5, 0, 0)),
                getBookingsResponse(
                        1,
                        "firstWorker",
                        "secondCustomer",
                        LocalDate.now().minusDays(1).atTime(20, 0, 0),
                        LocalDate.now().minusDays(1).atTime(21, 0, 0))
        )), Arrays.asList(
                new Parameter("date", LocalDate.now().minusDays(1).toString()),
                new Parameter("workerUsername", "firstWorker")
        ));
    }

    @SneakyThrows
    private void validDateWithCustomerUsername() {
        generateValidResponseMock((Arrays.asList(
                getBookingsResponse(
                        0,
                        "firstWorker",
                        "firstCustomer",
                        LocalDate.now().minusDays(1).atTime(4, 0, 0),
                        LocalDate.now().minusDays(1).atTime(5, 0, 0)),
                getBookingsResponse(
                        2,
                        "secondWorker",
                        "firstCustomer",
                        LocalDate.now().minusDays(1).atTime(13, 0, 0),
                        LocalDate.now().minusDays(1).atTime(14, 0, 0))
        )), Arrays.asList(
                new Parameter("date", LocalDate.now().minusDays(1).toString()),
                new Parameter("customerUsername", "firstCustomer")
        ));
    }

    @SneakyThrows
    private void validDateWithBothUsernames() {
        generateValidResponseMock((Arrays.asList(
                getBookingsResponse(
                        0,
                        "firstWorker",
                        "firstCustomer",
                        LocalDate.now().minusDays(1).atTime(4, 0, 0),
                        LocalDate.now().minusDays(1).atTime(5, 0, 0))
        )), Arrays.asList(
                new Parameter("date", LocalDate.now().minusDays(1).toString()),
                new Parameter("customerUsername", "firstCustomer"),
                new Parameter("workerUsername", "firstWorker")
        ));
    }

    @SneakyThrows
    private void invalidDateRequest() {
        Map<String, String> response = new HashMap<>();
        response.put("field", "date");
        response.put("message", "\"Date must be formatted as yyyy-mm-dd");

        generateMock(HttpStatus.BAD_REQUEST, response, new Parameter("date", "foo"));
    }

    @SneakyThrows
    private void missingDateRequest() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "No records within provided bounds were found");

        generateMock(HttpStatus.NOT_FOUND, response, new Parameter("customerUsername", "missing"));
    }
}
