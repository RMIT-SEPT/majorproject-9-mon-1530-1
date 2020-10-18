package sept.major.availability.service.blackbox.mock;

import lombok.SneakyThrows;
import org.mockserver.integration.ClientAndServer;
import sept.major.availability.service.blackbox.mock.list.bookings.BookingsAllRequestsMockList;
import sept.major.availability.service.blackbox.mock.list.bookings.BookingsDateRequestsMockList;
import sept.major.availability.service.blackbox.mock.list.bookings.BookingsRangeRequestsMockList;
import sept.major.availability.service.blackbox.mock.list.hours.HoursAllRequestsMockList;
import sept.major.availability.service.blackbox.mock.list.hours.HoursDateRequestsMockList;
import sept.major.availability.service.blackbox.mock.list.hours.HoursRangeRequestsMockList;

import java.io.IOException;
import java.net.ServerSocket;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockserver.integration.ClientAndServer.startClientAndServer;

public abstract class MockServices {

    private static final Integer hoursPort = generateMockServerPort();
    private static final Integer bookingsPort = generateMockServerPort();

    private static ClientAndServer mockServer;


    private static int generateMockServerPort() {
        try {
            return (new ServerSocket(0)).getLocalPort();
        } catch (IOException var1) {
            throw new RuntimeException("System had no free ports", var1);
        }
    }


    public static void startUpServer() {
        mockServer = startClientAndServer(hoursPort, bookingsPort);
        System.setProperty("HOURS_URL", "http://localhost:" + hoursPort + "/hours");
        System.setProperty("BOOKINGS_URL", "http://localhost:" + bookingsPort + "/bookings");


        List<MockList> mockLists = Arrays.asList(
                new HoursRangeRequestsMockList(hoursPort, "localhost"),
                new HoursAllRequestsMockList(hoursPort, "localhost"),
                new HoursDateRequestsMockList(hoursPort, "localhost"),

                new BookingsRangeRequestsMockList(bookingsPort, "localhost"),
                new BookingsAllRequestsMockList(bookingsPort, "localhost"),
                new BookingsDateRequestsMockList(bookingsPort, "localhost")
        );

        for (MockList mockList : mockLists) {
            mockList.createMocks();
        }
    }

    public static void stopServer() {
        mockServer.stop();
    }


    @SneakyThrows
    public static Map<String, String> getHoursResponse(Integer hoursId, String workerUsername, String creatorUsername, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        Map<String, String> response = new HashMap<>();
        response.put("hoursId", (hoursId == null ? null : hoursId.toString()));
        response.put("workerUsername", workerUsername);
        response.put("creatorUsername", creatorUsername);
        response.put("startDateTime", startDateTime.toString());
        response.put("endDateTime", endDateTime.toString());
        return response;
    }

    @SneakyThrows
    public static Map<String, String> getBookingsResponse(Integer bookingsId, String workerUsername, String customerUsername, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        Map<String, String> response = new HashMap<>();
        response.put("bookingId", (bookingsId == null ? null : bookingsId.toString()));
        response.put("workerUsername", workerUsername);
        response.put("customerUsername", customerUsername);
        response.put("startDateTime", startDateTime.toString());
        response.put("endDateTime", endDateTime.toString());
        return response;
    }
}
