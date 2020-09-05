package sept.major.availability.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import lombok.Getter;
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
	
	/**
	 * This method will
	 * 	get user's available hours in the range
	 *  get user's booked times
	 *  overlay the booking on the availability
	 *  calculate the result and return as a list
	 * @param username
	 * @param startDateTime
	 * @param endDateTme
	 * @return
	 */
	@GetMapping("/range")
	public void getAvailabilityInRange(@RequestParam(name = "startDateTime") String startDateString,
					@RequestParam(name = "endDateTime") String endDateString, @RequestParam(required = false) String workerUsername,
					@RequestParam(required = false) String creatorUsername) {

		log.debug("startDateTime:{}, endDateTime:{}, workerUsername:{}, creatorUsername:{}, ", startDateString, endDateString, workerUsername, creatorUsername);
		System.out.printf("username:%s, startDateTime:%s, endDateTime:%s \n", workerUsername, startDateString, endDateString);
		
		String userServiceEndpoint = env.getProperty(USER_SERVICE_ENDPOINT);
		String hoursServiceEndpoint = env.getProperty(HOURS_SERVICE_ENDPOINT);
		String bookingsServiceEndpoint = env.getProperty(BOOKINGS_SERVICE_ENDPOINT);
		
		Map<String, String> hoursVariablesMap = new HashMap<String, String>();
		hoursVariablesMap.put("startDateTime", startDateString);
		hoursVariablesMap.put("endDateTime", endDateString);
		hoursVariablesMap.put("workerUsename", workerUsername);
		hoursVariablesMap.put("creatorUsername", creatorUsername);
		
		String hoursTemplateUrl = hoursServiceEndpoint + "/range?startDateTime={startDateTime}&endDateTime={endDateTime}&workerUsename={workerUsename}&creatorUsername={creatorUsername}";
		List<HoursEntity> hoursList = restTemplate.getForObject(hoursTemplateUrl, List.class, hoursVariablesMap);

		Map<String, String> bookingsVariablesMap = new HashMap<String, String>();
		bookingsVariablesMap.put("startDateTime", startDateString);
		bookingsVariablesMap.put("endDateTime", endDateString);
		bookingsVariablesMap.put("workerUsername", workerUsername);
		
		String bookingsTemplateUrl = bookingsServiceEndpoint + "/range?startDateTime={startDateTime}&endDateTime={endDateTime}&workerUsername={workerUsername}";
		List<BookingEntity> bookingsList = restTemplate.getForObject(bookingsTemplateUrl, List.class, bookingsVariablesMap);

		log.info(hoursList.toString());  
		System.out.printf(hoursList.toString());
		
		availabilityService.checkAllAvailabilities(hoursList, bookingsList);
	 }
}
