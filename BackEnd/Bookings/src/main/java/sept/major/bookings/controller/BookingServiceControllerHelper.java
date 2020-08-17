package sept.major.bookings.controller;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import sept.major.common.controller.ControllerHelper;
import sept.major.bookings.entity.BookingEntity;
import sept.major.bookings.service.BookingService;

import java.sql.Date;

@Service
public class BookingServiceControllerHelper extends ControllerHelper<BookingEntity, String> {

    @Getter
    BookingService service;

    @Autowired
    public BookingServiceControllerHelper(BookingService bookingService) {
        this.service = bookingService;
    }
}
