package sept.major.bookings.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sept.major.common.exception.RecordNotFoundException;
import sept.major.bookings.entity.BookingEntity;
import sept.major.bookings.service.BookingService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/bookings")
public class BookingServiceController {

    BookingService bookingService;
    BookingServiceControllerHelper bookingControllerHelper;

    @Autowired
    public BookingServiceController(BookingService bookingService, BookingServiceControllerHelper bookingControllerHelper) {
        this.bookingService = bookingService;
        this.bookingControllerHelper = bookingControllerHelper;
    }

    //get bookings within range
    @GetMapping("/range")
    public ResponseEntity getRange(@RequestParam LocalDateTime startTime, @RequestParam LocalDateTime endTime) {
        try {
            List<BookingEntity> entityList = bookingService.getBookingsInRange(startTime, endTime);
            return new ResponseEntity(entityList, HttpStatus.OK);
        } catch (RecordNotFoundException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    //get bookings for date
    @GetMapping("/date")
    public ResponseEntity getRange(@RequestParam LocalDateTime startTime) {
        try {
            List<BookingEntity> entityList = bookingService.getBookingsInRange(startTime);
            return new ResponseEntity(entityList, HttpStatus.OK);
        } catch (RecordNotFoundException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    //create a new booking
    @PostMapping
    public ResponseEntity createBooking(@RequestBody Map<String, Object> requestBody) {
        return bookingControllerHelper.validateInputAndPost(BookingEntity.class, requestBody);
    }

    //delete a booking
    @DeleteMapping
    public ResponseEntity deleteBooking(@RequestParam String bookingID) {
        return bookingControllerHelper.deleteEntity(bookingID);
    }
}
