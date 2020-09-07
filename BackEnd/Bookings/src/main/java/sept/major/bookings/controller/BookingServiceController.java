package sept.major.bookings.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sept.major.bookings.entity.BookingEntity;
import sept.major.bookings.service.BookingService;
import sept.major.common.exception.RecordNotFoundException;
import sept.major.common.response.ValidationError;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/bookings")
@CrossOrigin
public class BookingServiceController {

    BookingService bookingService;
    BookingServiceControllerHelper bookingControllerHelper;
    public static final String INCORRECT_DATE_FORMAT_ERROR_MESSAGE = "Date time must be formatted as yyyy-mm-dd";
    public static final String INCORRECT_DATE_TIME_FORMAT_ERROR_MESSAGE = "Date time must be formatted as yyyy-mm-ddThh:mm:ss[.fffffffff]";

    @Autowired
    public BookingServiceController(BookingService bookingService, BookingServiceControllerHelper bookingControllerHelper) {
        this.bookingService = bookingService;
        this.bookingControllerHelper = bookingControllerHelper;
    }

    //get bookings within range
    @GetMapping("/range")
    public ResponseEntity getRange(@RequestParam(name = "startDateTime") String startDateTimeString,
                                   @RequestParam(name = "endDateTime") String endDateTimeString,
                                   @RequestParam(required = false) String workerUsername,
                                   @RequestParam(required = false) String customerUsername
    ) {
        LocalDateTime startDateTime;
        try {
            startDateTime = LocalDateTime.parse(startDateTimeString);
        } catch (DateTimeParseException e) {
            return new ResponseEntity(new ValidationError("startDate", INCORRECT_DATE_TIME_FORMAT_ERROR_MESSAGE), HttpStatus.BAD_REQUEST);
        }

        LocalDateTime endDateTime;
        try {
            endDateTime = LocalDateTime.parse(endDateTimeString);
        } catch (DateTimeParseException e) {
            return new ResponseEntity(new ValidationError("endDate",INCORRECT_DATE_TIME_FORMAT_ERROR_MESSAGE), HttpStatus.BAD_REQUEST);
        }

        if (endDateTime.isBefore(startDateTime)) {
            return new ResponseEntity(new ValidationError("date range", "start date must be above the end date"), HttpStatus.BAD_REQUEST);
        }

        try {
            List<BookingEntity> entityList = bookingService.getBookingsInRange(startDateTime, endDateTime, workerUsername, customerUsername);
            return new ResponseEntity(entityList, HttpStatus.OK);
        } catch (RecordNotFoundException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    //get bookings for date
    @GetMapping("/date")
    public ResponseEntity getDate(@RequestParam(name = "date") String dateString,
                                   @RequestParam(required = false) String workerUsername,
                                   @RequestParam(required = false) String customerUsername) {
        LocalDate date;
        try {
            date = (dateString == null ? null : LocalDate.parse(dateString));
        } catch (DateTimeParseException e) {
            return new ResponseEntity(new ValidationError("startDate", INCORRECT_DATE_FORMAT_ERROR_MESSAGE), HttpStatus.BAD_REQUEST);
        }
        try {
            List<BookingEntity> entityList = bookingService.getBookingsOnDate(date, workerUsername, customerUsername);
            return new ResponseEntity(entityList, HttpStatus.OK);
        } catch (RecordNotFoundException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    //get all bookings
    @GetMapping("/all")
    public ResponseEntity getAllBookings(@RequestParam(required = false) String workerUsername,
                                         @RequestParam(required = false) String customerUsername) {
        try {
            List<BookingEntity> entityList = bookingService.getBookingsFor(workerUsername, customerUsername);
            return new ResponseEntity(entityList, HttpStatus.OK);
        } catch (RecordNotFoundException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    //get a specific booking
    @GetMapping
    public ResponseEntity getBooking(@RequestParam String bookingId) {
        try {
            BookingEntity entity = bookingService.getSpecificBooking(Integer.parseInt(bookingId));
            return new ResponseEntity(entity, HttpStatus.OK);
        } catch (RecordNotFoundException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    //create a new booking
    @PostMapping
    public ResponseEntity createBooking(@RequestBody Map<String, String> requestBody) {
        return bookingControllerHelper.validateInputAndPost(BookingEntity.class, requestBody);
    }

    //update a booking
    @PatchMapping
    public ResponseEntity updateHours(@RequestParam String bookingId, @RequestBody Map<String, String> requestBody) {
        return bookingControllerHelper.validateInputAndPatch(BookingEntity.class, bookingId, Integer.class, requestBody);
    }

    //delete a booking
    @DeleteMapping
    public ResponseEntity deleteBooking(@RequestParam String bookingId) {
        return bookingControllerHelper.deleteEntity(bookingId, Integer.class);
    }
}
