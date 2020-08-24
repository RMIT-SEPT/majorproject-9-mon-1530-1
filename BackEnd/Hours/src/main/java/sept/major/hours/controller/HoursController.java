package sept.major.hours.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sept.major.common.exception.RecordNotFoundException;
import sept.major.common.exception.ResponseErrorException;
import sept.major.common.response.ResponseError;
import sept.major.hours.entity.HoursEntity;
import sept.major.hours.service.HoursService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/hours")
@CrossOrigin
public class HoursController {

    private HoursControllerHelper hoursControllerHelper;
    private HoursService hoursService;

    public static final String INCORRECT_DATE_FORMAT_ERROR_MESSAGE = "Date must be formatted as yyyy-mm-dd";
    public static final String INCORRECT_DATE_TIME_FORMAT_ERROR_MESSAGE = "Date time must be formatted as yyyy-mm-dd hh:mm:ss[.fffffffff]";


    @Autowired
    public HoursController(HoursService hoursService, HoursControllerHelper hoursControllerHelper) {
        this.hoursControllerHelper = hoursControllerHelper;
        this.hoursService = hoursService;
    }

    @GetMapping("/range")
    public ResponseEntity getHoursInRange(@RequestParam(name = "startDateTime") String startDateString,
                                          @RequestParam(name = "endDateTime") String endDateString,
                                          @RequestParam(required = false) String workerUsername,
                                          @RequestParam(required = false) String creatorUsername) {
        LocalDateTime startDate;
        try {
            startDate = (startDateString == null ? null : LocalDateTime.parse(startDateString));
        } catch (DateTimeParseException e) {
            return new ResponseEntity(new ResponseError("startDate", INCORRECT_DATE_TIME_FORMAT_ERROR_MESSAGE), HttpStatus.BAD_REQUEST);
        }

        LocalDateTime endDate;
        try {
            endDate = (endDateString == null ? null : LocalDateTime.parse(endDateString));
        } catch (DateTimeParseException e) {
            return new ResponseEntity(new ResponseError("endDate",INCORRECT_DATE_TIME_FORMAT_ERROR_MESSAGE), HttpStatus.BAD_REQUEST);
        }

        if(startDate == null && endDate == null) {
            return new ResponseEntity(
                    new ResponseError("date range", "You must provide at least one date in the range"),
                    HttpStatus.BAD_REQUEST);
        }

        if (startDate != null && endDate != null && endDate.isBefore(startDate)) {
            return new ResponseEntity(new ResponseError("date range", "start date must be above the end date"), HttpStatus.BAD_REQUEST);
        }

        try {
            List<HoursEntity> hours = hoursService.getHoursBetweenDates(startDate, endDate, workerUsername, creatorUsername);
            return new ResponseEntity(hours, HttpStatus.ACCEPTED);
        } catch (RecordNotFoundException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping("/date")
    public ResponseEntity getHoursInDate(@RequestParam(name = "date") String dateString,
                                         @RequestParam(required = false) String workerUsername,
                                         @RequestParam(required = false) String creatorUsername) {
        try {
            List<HoursEntity> hours = hoursService.getHoursInDate(LocalDate.parse(dateString), workerUsername, creatorUsername);
            return new ResponseEntity(hours, HttpStatus.ACCEPTED);
        } catch (RecordNotFoundException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (DateTimeParseException e) {
            return new ResponseEntity(new ResponseError("date", INCORRECT_DATE_FORMAT_ERROR_MESSAGE), HttpStatus.BAD_REQUEST);
        } catch (ResponseErrorException e) {
            throw new RuntimeException("Received null date when the field is required by the endpoint");
        }
    }

    @GetMapping("/all")
    public ResponseEntity getAllHours(@RequestParam(required = false) String workerUsername,
                                      @RequestParam(required = false) String creatorUsername) {

        try {
            List<HoursEntity> hours = hoursService.getAllHours(workerUsername, creatorUsername);
            return new ResponseEntity(hours, HttpStatus.OK);
        } catch (RecordNotFoundException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping()
    public ResponseEntity getHours(@RequestParam(name = "hoursId") String hoursId) {
        return hoursControllerHelper.getEntity(hoursId, Integer.class);
    }

    @PostMapping
    public ResponseEntity createHours(@RequestBody Map<String, String> requestBody) {
        return hoursControllerHelper.validateInputAndPost(HoursEntity.class, requestBody);
    }

    @PatchMapping
    public ResponseEntity updateHours(@RequestParam String hoursId, @RequestBody Map<String, String> requestBody) {
        return hoursControllerHelper.validateInputAndPatch(HoursEntity.class, hoursId, Integer.class, requestBody);
    }

    @DeleteMapping
    public ResponseEntity deleteHours(@RequestParam String hoursId) {
        return hoursControllerHelper.deleteEntity(hoursId, Integer.class);
    }
}
