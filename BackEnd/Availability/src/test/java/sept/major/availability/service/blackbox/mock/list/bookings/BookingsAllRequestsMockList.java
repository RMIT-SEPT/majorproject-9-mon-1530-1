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
public class BookingsAllRequestsMockList extends MockList {

    @Getter
    private final String path = "/bookings/all";

    @Getter
    private Integer port;

    @Getter
    private String host;

    public void createMocks() {
        validAllWithBothUsernames();
        validAllWithWorkerUsername();
        validAllWithCustomerUsername();
        invalidAllRequest();
        missingAllRequest();
        validAllNoUsernames();
    }

    @SneakyThrows
    private void validAllNoUsernames() {
        generateValidResponseMock(Arrays.asList(
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
                )
        );
    }

    @SneakyThrows
    private void validAllWithWorkerUsername() {
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
                new Parameter("workerUsername", "firstWorker")
        ));
    }

    @SneakyThrows
    private void validAllWithCustomerUsername() {
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
                new Parameter("customerUsername", "firstCustomer")
        ));
    }

    @SneakyThrows
    private void validAllWithBothUsernames() {
        generateValidResponseMock((Arrays.asList(
                getBookingsResponse(
                        0,
                        "firstWorker",
                        "firstCustomer",
                        LocalDate.now().minusDays(1).atTime(11, 0, 0),
                        LocalDate.now().minusDays(1).atTime(13, 0, 0))
        )), Arrays.asList(
                new Parameter("customerUsername", "firstCustomer"),
                new Parameter("workerUsername", "firstWorker")
        ));

    }

    @SneakyThrows
    private void invalidAllRequest() {
        Map<String, String> response = new HashMap<>();
        response.put("field", "workerUsername");
        response.put("message", "must be a valid username");

        generateMock(HttpStatus.BAD_REQUEST, response, new Parameter("customerUsername", "invalid"));

    }

    @SneakyThrows
    private void missingAllRequest() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "No records within provided bounds were found");

        generateMock(HttpStatus.NOT_FOUND, response, new Parameter("customerUsername", "missing"));
    }
}
