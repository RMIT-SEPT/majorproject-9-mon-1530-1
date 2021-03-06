package sept.major.hours.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import sept.major.common.exception.RecordNotFoundException;
import sept.major.common.exception.ValidationErrorException;
import sept.major.common.response.ValidationError;
import sept.major.hours.entity.HoursEntity;
import sept.major.hours.service.HoursService;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isBlank;

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

    /**
     * @return simple "ok" response to allow health check of the service to pass
     */
    @GetMapping("/health")
    public ResponseEntity<Object> getHoursServiceHealth() {
    	MultiValueMap<String, String> s= new LinkedMultiValueMap<String, String>();
    	
    	s.add("Service:", "availability");
    	
    	try {
    		s.add("availableProcessors:", "" + Runtime.getRuntime().availableProcessors());
		} catch (Exception e) {
			s.add("Getting processor details excption:", e.getMessage());
		}
		
    	try {
			s.add("totalMemory:", "" + Runtime.getRuntime().totalMemory());
			s.add("freeMemory", "" + Runtime.getRuntime().freeMemory());
			s.add("maxMemory:", "" + Runtime.getRuntime().maxMemory());

		} catch (Exception e) {
			s.add("Getting memory details excption:", e.getMessage());
		}

    	try {
			File diskPartition = new File("/");
			s.add("getTotalSpace:", "" + diskPartition.getTotalSpace());
			s.add("getFreeSpace:", "" + diskPartition.getFreeSpace());
			s.add("getUsableSpace:", "" + diskPartition.getUsableSpace());
    	} catch (Exception e) {
    		s.add("Getting disk details excption:", e.getMessage());
    	}
    	
    	return new ResponseEntity<Object>(""+s, HttpStatus.OK);
    }
    
    @GetMapping("/range")
    public ResponseEntity getHoursInRange(@RequestParam(name = "startDateTime") String startDateString,
                                          @RequestParam(name = "endDateTime") String endDateString,
                                          @RequestParam(required = false) String workerUsername,
                                          @RequestParam(required = false) String creatorUsername) {
        if (isUsernameInvalid(workerUsername)) {
            return new ResponseEntity(new ValidationError("workerUsername", "must be a valid username"), HttpStatus.BAD_REQUEST);
        }
        if (isUsernameInvalid(creatorUsername)) {
            return new ResponseEntity(new ValidationError("creatorUsername", "must be a valid username"), HttpStatus.BAD_REQUEST);
        }

        LocalDateTime startDate;
        try {
            startDate = (startDateString == null ? null : LocalDateTime.parse(startDateString));
        } catch (DateTimeParseException e) {
            return new ResponseEntity(new ValidationError("startDate", INCORRECT_DATE_TIME_FORMAT_ERROR_MESSAGE), HttpStatus.BAD_REQUEST);
        }

        LocalDateTime endDate;
        try {
            endDate = (endDateString == null ? null : LocalDateTime.parse(endDateString));
        } catch (DateTimeParseException e) {
            return new ResponseEntity(new ValidationError("endDate", INCORRECT_DATE_TIME_FORMAT_ERROR_MESSAGE), HttpStatus.BAD_REQUEST);
        }

        if(startDate == null && endDate == null) {
            return new ResponseEntity(
                    new ValidationError("date range", "You must provide at least one date in the range"),
                    HttpStatus.BAD_REQUEST);
        }

        if (startDate != null && endDate != null && endDate.isBefore(startDate)) {
            return new ResponseEntity(new ValidationError("date range", "start date must be above the end date"), HttpStatus.BAD_REQUEST);
        }

        try {
            List<HoursEntity> hours = hoursService.getHoursBetweenDates(startDate, endDate, workerUsername, creatorUsername);
            return new ResponseEntity(hours, HttpStatus.OK);
        } catch (RecordNotFoundException e) {
            return new ResponseEntity(new AbstractMap.SimpleEntry<>("message", e.getMessage()), HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping("/date")
    public ResponseEntity getHoursInDate(@RequestParam(name = "date") String dateString,
                                         @RequestParam(required = false) String workerUsername,
                                         @RequestParam(required = false) String creatorUsername) {
        if (isUsernameInvalid(workerUsername)) {
            return new ResponseEntity(new ValidationError("workerUsername", "must be a valid username"), HttpStatus.BAD_REQUEST);
        }
        if (isUsernameInvalid(creatorUsername)) {
            return new ResponseEntity(new ValidationError("creatorUsername", "must be a valid username"), HttpStatus.BAD_REQUEST);
        }

        try {
            List<HoursEntity> hours = hoursService.getHoursInDate(LocalDate.parse(dateString), workerUsername, creatorUsername);
            return new ResponseEntity(hours, HttpStatus.OK);
        } catch (RecordNotFoundException e) {
            return new ResponseEntity(new AbstractMap.SimpleEntry<>("message", e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (DateTimeParseException e) {
            return new ResponseEntity(new ValidationError("date", INCORRECT_DATE_FORMAT_ERROR_MESSAGE), HttpStatus.BAD_REQUEST);
        } catch (ValidationErrorException e) {
            throw new RuntimeException("Received null date when the field is required by the endpoint");
        }
    }

    @GetMapping("/all")
    public ResponseEntity getAllHours(@RequestParam(required = false) String workerUsername,
                                      @RequestParam(required = false) String creatorUsername) {

        if (isUsernameInvalid(workerUsername)) {
            return new ResponseEntity(new ValidationError("workerUsername", "must be a valid username"), HttpStatus.BAD_REQUEST);
        }
        if (isUsernameInvalid(creatorUsername)) {
            return new ResponseEntity(new ValidationError("creatorUsername", "must be a valid username"), HttpStatus.BAD_REQUEST);
        }

        try {
            List<HoursEntity> hours = hoursService.getAllHours(workerUsername, creatorUsername);
            return new ResponseEntity(hours, HttpStatus.OK);
        } catch (RecordNotFoundException e) {
            return new ResponseEntity(new AbstractMap.SimpleEntry<>("message", e.getMessage()), HttpStatus.NOT_FOUND);
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

    private boolean isUsernameInvalid(String username) {
        return (username != null && ("null".equals(username) || isBlank(username)));
    }
}
