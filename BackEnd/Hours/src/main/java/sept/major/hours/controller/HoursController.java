package sept.major.hours.controller;

import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sept.major.common.exception.RecordNotFoundException;
import sept.major.common.response.ResponseError;
import sept.major.hours.entity.HoursEntity;
import sept.major.hours.service.HoursService;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/hours")
public class HoursController {

    private HoursControllerHelper hoursControllerHelper;
    private HoursService hoursService;

    @Autowired
    public HoursController(HoursService hoursService, HoursControllerHelper hoursControllerHelper) {
        this.hoursControllerHelper = hoursControllerHelper;
        this.hoursService = hoursService;
    }

    @GetMapping("/range")
    public ResponseEntity getHoursInRange(@RequestParam(required = false) String startDateString,
                                          @RequestParam(required = false) String endDateString,
                                          @RequestParam(required = false) String workerUsename,
                                          @RequestParam(required = false) String customerUsername) {
        LocalDate startDate;
        try {
            startDate = LocalDate.parse(startDateString);
        } catch (DateTimeParseException e) {
            return new ResponseEntity(new ResponseError("startDate", "Provided date no formatted correctly"), HttpStatus.BAD_REQUEST);
        }

        LocalDate endDate;
        try {
            endDate = LocalDate.parse(endDateString);
        } catch (DateTimeParseException e) {
            return new ResponseEntity(new ResponseError("endDate", "Provided date no formatted correctly"), HttpStatus.BAD_REQUEST);
        }

        try {
            List<HoursEntity> hours = hoursService.getHoursBetweenDates(startDate, endDate, workerUsename, customerUsername);
            return new ResponseEntity(hours, HttpStatus.ACCEPTED);
        } catch (RecordNotFoundException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping("/date")
    public ResponseEntity getHoursInDate(@RequestParam String date,
                                         @RequestParam(required = false) String workerUsername,
                                         @RequestParam(required = false) String customerUsername) {

        try {
            List<HoursEntity> hours = hoursService.getHoursInDate(LocalDate.parse(date), workerUsername, customerUsername);
            return new ResponseEntity(hours, HttpStatus.ACCEPTED);
        } catch (RecordNotFoundException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (DateTimeParseException e) {
            return new ResponseEntity(new ResponseError("date", "Provided date no formatted correctly"), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/all")
    public ResponseEntity getAllHours(@RequestParam(required = false) String workerUsername,
                                      @RequestParam(required = false) String customerUsername) {

        try {
            List<HoursEntity> hours = hoursService.getAllHours(workerUsername, customerUsername);
            return new ResponseEntity(hours, HttpStatus.ACCEPTED);
        } catch (RecordNotFoundException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping()
    public ResponseEntity getHours(@RequestParam(required = false) String hoursId) {
        return hoursControllerHelper.getEntity(hoursId);
    }

    @PostMapping
    public ResponseEntity createHours(@RequestBody Map<String, Object> requestBody) {
        return hoursControllerHelper.validateInputAndPost(HoursEntity.class, requestBody);
    }

    @PatchMapping
    public ResponseEntity updateHours(@RequestParam String hoursId, @RequestBody Map<String, Object> requestBody) {
        return hoursControllerHelper.validateInputAndPatch(HoursEntity.class, hoursId, requestBody);
    }

    @DeleteMapping
    public ResponseEntity deleteHours(@RequestParam String hoursId) {
        return hoursControllerHelper.deleteEntity(hoursId);
    }
}
