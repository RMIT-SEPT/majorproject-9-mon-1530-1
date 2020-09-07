package sept.major.bookings.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import sept.major.bookings.controller.BookingServiceController;
import sept.major.bookings.controller.BookingServiceControllerHelper;
import sept.major.bookings.repository.BookingsRepository;
import sept.major.bookings.service.BookingService;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest

public class UnitTestHelper {
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
}
