package sept.major.bookings;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import sept.major.bookings.controller.BookingServiceController;
import sept.major.bookings.controller.BookingServiceControllerHelper;
import sept.major.bookings.entity.BookingEntity;
import sept.major.bookings.repository.BookingsRepository;
import sept.major.bookings.service.BookingService;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest

public class BookingServiceTestHelper {
    BookingServiceController bookingServiceController;
    BookingServiceControllerHelper bookingServiceControllerHelper;
    BookingService bookingService;

    @Mock
    BookingsRepository mockedBookingRepository;

    @BeforeEach
    public void setUp() {
        bookingService = new BookingService(mockedBookingRepository);
        bookingServiceControllerHelper = new BookingServiceControllerHelper(bookingService);
        bookingServiceController = new BookingServiceController(bookingService, bookingServiceControllerHelper);
    }

    @Test
    void contextLoads() { assertThat(bookingServiceController).isNotNull(); }

    protected String randomAlphanumericString(int length) {
        final int[] uppercaseRange = {65, 91};
        final int[] lowerCaseRange = {97, 123};
        final int[] numbersRange = {48, 58};

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int rangeToUse = (int) ((Math.random() * (4 - 1) + 1));
            int charToUse;
            if (rangeToUse == 1) {
                charToUse = (int) ((Math.random() * (uppercaseRange[0] - uppercaseRange[1]) + uppercaseRange[1]));
            } else if (rangeToUse == 2) {
                charToUse = (int) ((Math.random() * (lowerCaseRange[0] - lowerCaseRange[1]) + lowerCaseRange[1]));
            } else {
                charToUse = (int) ((Math.random() * (numbersRange[0] - numbersRange[1]) + numbersRange[1]));
            }
            stringBuilder.append((char) charToUse);
        }

        return stringBuilder.toString();
    }

    protected Map<String, Object> randomEntityMap() {
        return new HashMap<String, Object>() {{
            put("bookingId", randomAlphanumericString(4));
            put("workerId", randomAlphanumericString(20));
            put("customerId", randomAlphanumericString(20));
            put("startTime", randomAlphanumericString(20));
            put("endTime", randomAlphanumericString(20));
        }};
    }

    protected BookingEntity createBookingEntity(Map<String, Object> entityMap) {
        return new BookingEntity(
                (String) entityMap.get("bookingId"),
                (String) entityMap.get("workerId"),
                (String) entityMap.get("customerId"),
                (String) entityMap.get("startTime"),
                (String) entityMap.get("endTime")
        );
    }
}
