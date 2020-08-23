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

import sept.major.availability.entity.BookingEntity;
import sept.major.availability.entity.HoursEntity;

@RestController
@RequestMapping("/availability")
public class AvailabilityController {
	 
	private static final Logger log = LoggerFactory.getLogger(AvailabilityController.class);
	
	

	
	@Autowired
	RestTemplate rt;
	
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
	 public void getAvailabilityInRange(@RequestParam(required = false) String username , /*LocalDateTime*/String startDateTime, /*LocalDateTime*/String endDateTime ) {

		log.debug("username:{}, startDateTime:{}, endDateTime:{}", username, startDateTime, endDateTime);
		System.out.printf("username:%s, startDateTime:%s, endDateTime:%s \n", username, startDateTime, endDateTime);
		
		
		String userServiceEndpoint = env.getProperty(USER_SERVICE_ENDPOINT);
		String hoursServiceEndpoint = env.getProperty(HOURS_SERVICE_ENDPOINT);
		String bookingsServiceEndpoint = env.getProperty(BOOKINGS_SERVICE_ENDPOINT);
		
		
		Map<String, String> hoursVariablesMap = new HashMap<String, String>();
		hoursVariablesMap.put("startDateString", "123");
		hoursVariablesMap.put("endDateString", "234");
		hoursVariablesMap.put("workerUsename", "345");
		hoursVariablesMap.put("customerUsername", "456");
		
		String hoursTemplateUrl = hoursServiceEndpoint + "/range?startDateString={startDateString}&endDateString={endDateString}&workerUsename={workerUsename}&customerUsername={customerUsername}";
		List<HoursEntity> hoursList = rt.getForObject(hoursTemplateUrl, List.class, hoursVariablesMap);

		
		Map<String, String> bookingsVariablesMap = new HashMap<String, String>();
		bookingsVariablesMap.put("startTime", "123");
		bookingsVariablesMap.put("endTime", "234");
		
		String bookingsTemplateUrl = bookingsServiceEndpoint + "/range?startTime={startTime}&endTime={endTime}";
		List<BookingEntity> bookingsList = rt.getForObject(bookingsTemplateUrl, List.class, bookingsVariablesMap);

		log.info(hoursList.toString());  
		System.out.printf(hoursList.toString());
	 }
}
