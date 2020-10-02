package sept.major.availability.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import sept.major.availability.entity.BookingResponse;
import sept.major.availability.entity.HoursResponse;
import sept.major.availability.service.AvailabilityPair;
import sept.major.availability.service.AvailabilityService;
import sept.major.availability.service.connector.BookingServiceConnector;
import sept.major.availability.service.connector.HoursServiceConnector;
import sept.major.availability.service.connector.ServiceConnectorException;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.AbstractMap;
import java.util.List;

@RestController
@RequestMapping("/availability")
@CrossOrigin
public class AvailabilityController {

    private static final Logger log = LoggerFactory.getLogger(AvailabilityController.class);
    @Autowired
    public Environment env;
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    private AvailabilityService availabilityService;

    @Autowired
    private HoursServiceConnector hoursServiceConnector;

    @Autowired
    private BookingServiceConnector bookingServiceConnector;

    /**
     * This method will get user's available hours in the range get user's booked times overlay the booking on the availability calculate the result and return
     * as a list
     *
     * @param startDateString, example : '2020-09-05T21:54:41.173'
     * @param endDateString
     * @param workerUsername
     * @param creatorUsername
     */
    @GetMapping("/range")
    public ResponseEntity getAvailabilityInRange(@RequestHeader("Authorization") String token,
                                                 @RequestHeader("username") String requesterUsername,
                                                 @RequestParam(name = "startDateTime") String startDateString,
                                                 @RequestParam(name = "endDateTime") String endDateString,
                                                 @RequestParam(required = false) String workerUsername,
                                                 @RequestParam(required = false) String creatorUsername,
                                                 @RequestParam(required = false) String customerUsername) {

        log.info("startDateTime:{}, endDateTime:{}, workerUsername:{}, creatorUsername:{}, ", startDateString, endDateString, workerUsername, creatorUsername);

        List<HoursResponse> hoursList;
        List<BookingResponse> bookingsList;
        try {
            hoursList = hoursServiceConnector.getRange(token, requesterUsername, startDateString, endDateString, workerUsername, customerUsername);
            log.info(hoursList.toString());
            bookingsList = bookingServiceConnector.getRange(token, requesterUsername, startDateString, endDateString, workerUsername, creatorUsername);
            log.info(bookingsList.toString());
        } catch (ServiceConnectorException e) {
            return new ResponseEntity(e.getJsonFormat(), HttpStatus.BAD_REQUEST);
        }

        return evaluateAvailabilities(hoursList, bookingsList);
    }

    /**
     * Return availabilities for a given worker for a given date
     *
     * @param dateString
     * @param workerUsername
     * @param creatorUsername
     * @return
     */
    @GetMapping("/date")
    public ResponseEntity getAvailabilityInDate(@RequestHeader("Authorization") String token,
                                                @RequestHeader("username") String requesterUsername,
                                                @RequestParam(name = "date") String dateString,
                                                @RequestParam(required = false) String workerUsername,
                                                @RequestParam(required = false) String creatorUsername,
                                                @RequestParam(required = false) String customerUsername) {

        log.info("dateString:{}, workerUsername:{}, creatorUsername:{}, ", dateString, workerUsername, creatorUsername);

        List<HoursResponse> hoursList;
        List<BookingResponse> bookingsList;
        try {
            hoursList = hoursServiceConnector.getDate(token, requesterUsername, dateString, workerUsername, customerUsername);
            log.info(hoursList.toString());
            bookingsList = bookingServiceConnector.getDate(token, requesterUsername, dateString, workerUsername, creatorUsername);
            log.info(bookingsList.toString());
        } catch (ServiceConnectorException e) {
            return new ResponseEntity(e.getJsonFormat(), HttpStatus.BAD_REQUEST);
        }

        return evaluateAvailabilities(hoursList, bookingsList);

    }


    /**
     * return all available hours for the given worker and creator
     *
     * @param workerUsername
     * @param creatorUsername
     * @return
     */
    @GetMapping("/all")
    public ResponseEntity getAllAvailabilities(@RequestHeader("Authorization") String token,
                                               @RequestHeader("username") String requesterUsername,
                                               @RequestParam(required = false) String workerUsername,
                                               @RequestParam(required = false) String creatorUsername,
                                               @RequestParam(required = false) String customerUsername) {
        log.info("workerUsername:{}, creatorUsername:{}, ", workerUsername, creatorUsername);

        List<HoursResponse> hoursList;
        List<BookingResponse> bookingsList;
        try {
            hoursList = hoursServiceConnector.getAll(token, requesterUsername, workerUsername, customerUsername);
            log.info(hoursList.toString());
            bookingsList = bookingServiceConnector.getAll(token, requesterUsername, workerUsername, creatorUsername);
            log.info(bookingsList.toString());
        } catch (ServiceConnectorException e) {
            return new ResponseEntity(e.getJsonFormat(), HttpStatus.BAD_REQUEST);
        }

        return evaluateAvailabilities(hoursList, bookingsList);
    }

    /**
     * There doesn't seem a need for implementing Delete for availability since there is no persistence. The availability is calculated from hours and bookings.
     *
     * @return
     */
    @DeleteMapping("/delete")
    public ResponseEntity<String> delete() {
        log.info("unimplemented Delete method called");
        return new ResponseEntity<String>("Availability Delete method is not implemnted ", HttpStatus.NOT_IMPLEMENTED);
    }


    @GetMapping("slot/date")
    public ResponseEntity getSlotsOnDate(@RequestHeader("Authorization") String token,
                                         @RequestHeader("username") String requesterUsername,
                                         @RequestParam(name = "date") String dateString) {
        LocalDate date;
        try {
            date = LocalDate.parse(dateString);
        } catch (DateTimeParseException e) {
            return new ResponseEntity(new AbstractMap.SimpleEntry<>("message", "Provided date must have YYYY-MM-DD format"), HttpStatus.BAD_REQUEST);
        }

        List<HoursResponse> hoursList;
        List<BookingResponse> bookingsList;

        LocalDate endDate = availabilityService.findEndOfWeek(date);

        try {
            hoursList = hoursServiceConnector.getRange(token, requesterUsername, date.atStartOfDay().toString(), endDate.atTime(23, 59, 59).toString(), null, null);
            log.info(hoursList.toString());
            bookingsList = bookingServiceConnector.getRange(token, requesterUsername, date.atStartOfDay().toString(), endDate.atTime(23, 59, 59).toString(), null, null);
            log.info(bookingsList.toString());
        } catch (ServiceConnectorException e) {
            return new ResponseEntity(e.getJsonFormat(), HttpStatus.BAD_REQUEST);
        }


        return new ResponseEntity(availabilityService.getTimeSlots(date, endDate, availabilityService.checkAllAvailabilities(hoursList, bookingsList)), HttpStatus.OK);
    }

    @GetMapping("slot/now")
    public ResponseEntity getTimeSlotsNow(@RequestHeader("Authorization") String token,
                                          @RequestHeader("username") String requesterUsername,
                                          @RequestParam(name = "from", required = false) Integer increment) {
        LocalDate date = LocalDate.now();
        if (increment != null) {
            date = availabilityService.findStartOfWeek(date).plusWeeks(increment);
        }
        return getSlotsOnDate(token, requesterUsername, date.toString());
    }

    private ResponseEntity evaluateAvailabilities(List<HoursResponse> hoursResponses, List<BookingResponse> bookingResponses) {
        if (hoursResponses.isEmpty()) {
            return new ResponseEntity(new AbstractMap.SimpleEntry("message", "found no hours in the range provided"), HttpStatus.NOT_FOUND);
        }

        AvailabilityPair allAvailabilities = availabilityService.checkAllAvailabilities(hoursResponses, bookingResponses);

        if (allAvailabilities.isEmpty()) {
            return new ResponseEntity(new AbstractMap.SimpleEntry("message", "found no availabilities in the range provided"), HttpStatus.NOT_FOUND);
        }


        return new ResponseEntity(allAvailabilities, HttpStatus.OK);
    }


}
