package sept.major.availability.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import sept.major.availability.entity.AvailabilityEntity;
import sept.major.availability.entity.BookingEntity;
import sept.major.availability.entity.HoursEntity;
import sept.major.availability.service.AvailabilityService;

@RestController
@RequestMapping("/availability")
public class AvailabilityController {
	 
	private static final Logger log = LoggerFactory.getLogger(AvailabilityController.class);
	
	
	@Autowired
    private AvailabilityService availabilityService;

	
	@Autowired
	RestTemplate restTemplate;
	
	@Autowired
	public Environment env;

	private static final String USER_SERVICE_ENDPOINT = "user.service.endpoint";
	private static final String HOURS_SERVICE_ENDPOINT = "hours.service.endpoint";
	private static final String BOOKINGS_SERVICE_ENDPOINT = "bookings.service.endpoint";
	
	String userServiceEndpoint = env.getProperty(USER_SERVICE_ENDPOINT);
	String hoursServiceEndpoint = env.getProperty(HOURS_SERVICE_ENDPOINT);
	String bookingsServiceEndpoint = env.getProperty(BOOKINGS_SERVICE_ENDPOINT);

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
	public ResponseEntity getAvailabilityInRange(@RequestParam(name = "startDateTime") String startDateString,
					@RequestParam(name = "endDateTime") String endDateString, @RequestParam(required = false) String workerUsername,
					@RequestParam(required = false) String creatorUsername) {

		log.info("startDateTime:{}, endDateTime:{}, workerUsername:{}, creatorUsername:{}, ", startDateString, endDateString, workerUsername, creatorUsername);
		
		try {
		{//conversion test TODO remove
			LocalDateTime sample = LocalDateTime.now();
			log.info("sample:{}", sample);
			LocalDateTime start_LDT = LocalDateTime.parse(startDateString);
			LocalDateTime end_LDT = LocalDateTime.parse(startDateString);
			log.info("start_LDT:{}, end_LDT:{}", start_LDT, end_LDT);
		}
		
		Map<String, String> variablesMap = new HashMap<String, String>();
		variablesMap.put("startDateTime", startDateString);
		variablesMap.put("endDateTime", endDateString);
		variablesMap.put("workerUsename", workerUsername);
		variablesMap.put("creatorUsername", creatorUsername);
		
		String hoursTemplateUrl = hoursServiceEndpoint + "/range?startDateTime={startDateTime}&endDateTime={endDateTime}&workerUsename={workerUsename}&creatorUsername={creatorUsername}";
		List<HoursEntity> hoursList = restTemplate.getForObject(hoursTemplateUrl, List.class, variablesMap);

		
		
		String bookingsTemplateUrl = bookingsServiceEndpoint + "/range?startDateTime={startDateTime}&endDateTime={endDateTime}&workerUsername={workerUsername}";
		List<BookingEntity> bookingsList = restTemplate.getForObject(bookingsTemplateUrl, List.class, variablesMap);

		log.info(hoursList.toString());  
		
		List<AvailabilityEntity>  allAvailabilities = availabilityService.checkAllAvailabilities(hoursList, bookingsList);
		
		
		return new ResponseEntity(allAvailabilities, HttpStatus.ACCEPTED);
		} catch (Exception e) {
			return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST); //TODO try/catch to be improved
		}
	 }
	
	/**
	 * Return availabilities for a given worker for a given date
	 * @param dateString
	 * @param workerUsername
	 * @param creatorUsername
	 * @return
	 */
    @GetMapping("/date")
    public ResponseEntity getAvailabilityInDate(@RequestParam(name = "date") String dateString,
                                         @RequestParam(required = false) String workerUsername,
                                         @RequestParam(required = false) String creatorUsername) {
    	
    	log.info("dateString:{}, workerUsername:{}, creatorUsername:{}, ", dateString, workerUsername, creatorUsername);
    	
		try { // conversion test TODO remove
			{// conversion test TODO remove
				LocalDateTime sample = LocalDateTime.now();
				log.info("sample:{}", sample);
				LocalDateTime start_LD = LocalDateTime.parse(dateString);
				log.info("start_LD:{}", start_LD);
			}

			Map<String, String> variablesMap = new HashMap<String, String>();
			variablesMap.put("dateString", dateString);
			variablesMap.put("workerUsename", workerUsername);
			variablesMap.put("creatorUsername", creatorUsername);

			String hoursTemplateUrl = hoursServiceEndpoint + "/date?date={dateString}&workerUsename={workerUsename}&creatorUsername={creatorUsername}";
			List<HoursEntity> hoursList = restTemplate.getForObject(hoursTemplateUrl, List.class, variablesMap);

			String bookingsTemplateUrl = bookingsServiceEndpoint + "/date?date={startDateTime}&workerUsername={workerUsername}";
			List<BookingEntity> bookingsList = restTemplate.getForObject(bookingsTemplateUrl, List.class, variablesMap);

			List<AvailabilityEntity> allAvailabilities = availabilityService.checkAllAvailabilities(hoursList, bookingsList);

			return new ResponseEntity(allAvailabilities, HttpStatus.ACCEPTED);
		} catch (Exception e) {
			return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST); // TODO try/catch to be improved
		}

    }

    
    /**
     * return all available hours for the given worker and creator
     * @param workerUsername
     * @param creatorUsername
     * @return
     */
    @GetMapping("/all")
    public ResponseEntity getAllAvailabilities(@RequestParam(required = false) String workerUsername,
                                      		@RequestParam(required = false) String creatorUsername) {
    	log.info("workerUsername:{}, creatorUsername:{}, ", workerUsername, creatorUsername);

		try {
			Map<String, String> variablesMap = new HashMap<String, String>();
			variablesMap.put("workerUsename", workerUsername);
			variablesMap.put("creatorUsername", creatorUsername);

			String hoursTemplateUrl = hoursServiceEndpoint + "/all?workerUsename={workerUsename}&creatorUsername={creatorUsername}";
			List<HoursEntity> hoursList = restTemplate.getForObject(hoursTemplateUrl, List.class, variablesMap);

			String bookingsTemplateUrl = bookingsServiceEndpoint + "/all?workerUsername={workerUsername}";
			List<BookingEntity> bookingsList = restTemplate.getForObject(bookingsTemplateUrl, List.class, variablesMap);

			List<AvailabilityEntity> allAvailabilities = availabilityService.checkAllAvailabilities(hoursList, bookingsList);

			return new ResponseEntity(allAvailabilities, HttpStatus.ACCEPTED);
		} catch (Exception e) {
			return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST); // TODO try/catch to be improved
		}
    }
}

