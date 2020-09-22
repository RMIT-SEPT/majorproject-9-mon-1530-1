package sept.major.availability.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import sept.major.availability.entity.AvailabilityEntity;
import sept.major.availability.entity.BookingResponse;
import sept.major.availability.entity.HoursResponse;
import sept.major.availability.service.AvailabilityService;
import sept.major.common.testing.RequestParameter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/availability")
@CrossOrigin
public class AvailabilityController {

    private static final Logger log = LoggerFactory.getLogger(AvailabilityController.class);
    private static final String USER_SERVICE_ENDPOINT = "user.service.endpoint";
    private static final String HOURS_SERVICE_ENDPOINT = "hours.service.endpoint";
    private static final String BOOKINGS_SERVICE_ENDPOINT = "bookings.service.endpoint";
    @Autowired
    public Environment env;
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    private AvailabilityService availabilityService;

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
                                                 @RequestParam(name = "endDateTime") String endDateString,
                                                 @RequestParam(required = false) String workerUsername,
                                                 @RequestParam(required = false) String creatorUsername,
                                                 @RequestParam(required = false) String customerUsername) {

        log.info("startDateTime:{}, endDateTime:{}, workerUsername:{}, creatorUsername:{}, ", startDateString, endDateString, workerUsername, creatorUsername);
        String userServiceEndpoint = env.getProperty(USER_SERVICE_ENDPOINT);
        String hoursServiceEndpoint = env.getProperty(HOURS_SERVICE_ENDPOINT);
        String bookingsServiceEndpoint = env.getProperty(BOOKINGS_SERVICE_ENDPOINT);

        List<RequestParameter> hoursRequestParameters = new ArrayList<>();
        hoursRequestParameters.add(new RequestParameter("startDate", startDateString));
        hoursRequestParameters.add(new RequestParameter("endDate", endDateString));
        List<RequestParameter> bookingsRequestParameters = new ArrayList<>(hoursRequestParameters);

        if (workerUsername != null) {
            hoursRequestParameters.add(new RequestParameter("workerUsername", workerUsername));
            bookingsRequestParameters.add(new RequestParameter("workerUsername", workerUsername));
        }
        if (creatorUsername != null) {
            hoursRequestParameters.add(new RequestParameter("creatorUsername", creatorUsername));
        }
        if (customerUsername != null) {
            bookingsRequestParameters.add(new RequestParameter("creatorUsername", customerUsername));
        }

        String hoursTemplateUrl = addRequestParameters(hoursServiceEndpoint + "/all", hoursRequestParameters);
        List<HoursResponse> hoursList;
        try {
            hoursList = convertMapListToHoursList(restTemplate.getForObject(hoursTemplateUrl, List.class));
        } catch (HttpClientErrorException e) {
            HashMap<String, Object> response = new HashMap<>();
            response.put("service", "hours");
            response.put("details", e.getMessage());
            return new ResponseEntity(response, e.getStatusCode());
        }

        String bookingsTemplateUrl = addRequestParameters(bookingsServiceEndpoint + "/all", bookingsRequestParameters);
        List<BookingResponse> bookingsList = new ArrayList<>();
        try {
            bookingsList = convertMapListToBookingList(restTemplate.getForObject(bookingsTemplateUrl, List.class));
        } catch (HttpClientErrorException e) {
            if (!e.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                HashMap<String, Object> response = new HashMap<>();
                response.put("service", "hours");
                response.put("details", e.getMessage());
                return new ResponseEntity(response, e.getStatusCode());
            }
        }

        List<AvailabilityEntity> allAvailabilities = availabilityService.checkAllAvailabilities(hoursList, bookingsList);

        return new ResponseEntity(allAvailabilities, HttpStatus.ACCEPTED);
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
    public ResponseEntity getAvailabilityInDate(@RequestParam(name = "date") String dateString,
                                                @RequestParam(required = false) String workerUsername,
                                                @RequestParam(required = false) String creatorUsername,
                                                @RequestParam(required = false) String customerUsername) {

        log.info("dateString:{}, workerUsername:{}, creatorUsername:{}, ", dateString, workerUsername, creatorUsername);

        String userServiceEndpoint = env.getProperty(USER_SERVICE_ENDPOINT);
        String hoursServiceEndpoint = env.getProperty(HOURS_SERVICE_ENDPOINT);
        String bookingsServiceEndpoint = env.getProperty(BOOKINGS_SERVICE_ENDPOINT);

        List<RequestParameter> hoursRequestParameters = new ArrayList<>();
        hoursRequestParameters.add(new RequestParameter("date", dateString));
        List<RequestParameter> bookingsRequestParameters = new ArrayList<>(hoursRequestParameters);

        if (workerUsername != null) {
            hoursRequestParameters.add(new RequestParameter("workerUsername", workerUsername));
            bookingsRequestParameters.add(new RequestParameter("workerUsername", workerUsername));
        }
        if (creatorUsername != null) {
            hoursRequestParameters.add(new RequestParameter("creatorUsername", creatorUsername));
        }
        if (customerUsername != null) {
            bookingsRequestParameters.add(new RequestParameter("creatorUsername", customerUsername));
        }

        String hoursTemplateUrl = addRequestParameters(hoursServiceEndpoint + "/all", hoursRequestParameters);
        List<HoursResponse> hoursList;
        try {
            hoursList = convertMapListToHoursList(restTemplate.getForObject(hoursTemplateUrl, List.class));
        } catch (HttpClientErrorException e) {
            HashMap<String, Object> response = new HashMap<>();
            response.put("service", "hours");
            response.put("details", e.getMessage());
            return new ResponseEntity(response, e.getStatusCode());
        }

        String bookingsTemplateUrl = addRequestParameters(bookingsServiceEndpoint + "/all", bookingsRequestParameters);
        List<BookingResponse> bookingsList = new ArrayList<>();
        try {
            bookingsList = convertMapListToBookingList(restTemplate.getForObject(bookingsTemplateUrl, List.class));
        } catch (HttpClientErrorException e) {
            if (!e.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                HashMap<String, Object> response = new HashMap<>();
                response.put("service", "hours");
                response.put("details", e.getMessage());
                return new ResponseEntity(response, e.getStatusCode());
            }
        }

        List<AvailabilityEntity> allAvailabilities = availabilityService.checkAllAvailabilities(hoursList, bookingsList);

        return new ResponseEntity(allAvailabilities, HttpStatus.ACCEPTED);

    }


    /**
     * return all available hours for the given worker and creator
     *
     * @param workerUsername
     * @param creatorUsername
     * @return
     */
    @GetMapping("/all")
    public ResponseEntity getAllAvailabilities(@RequestParam(required = false) String workerUsername,
                                               @RequestParam(required = false) String creatorUsername,
                                               @RequestParam(required = false) String customerUsername) {
        log.info("workerUsername:{}, creatorUsername:{}, ", workerUsername, creatorUsername);

        String hoursServiceEndpoint = env.getProperty(HOURS_SERVICE_ENDPOINT);
        String bookingsServiceEndpoint = env.getProperty(BOOKINGS_SERVICE_ENDPOINT);

        List<RequestParameter> hoursRequestParameters = new ArrayList<>();
        List<RequestParameter> bookingsRequestParameters = new ArrayList<>();

        if (workerUsername != null) {
            hoursRequestParameters.add(new RequestParameter("workerUsername", workerUsername));
            bookingsRequestParameters.add(new RequestParameter("workerUsername", workerUsername));
        }
        if (creatorUsername != null) {
            hoursRequestParameters.add(new RequestParameter("creatorUsername", creatorUsername));
        }
        if (customerUsername != null) {
            bookingsRequestParameters.add(new RequestParameter("creatorUsername", customerUsername));
        }

        String hoursTemplateUrl = addRequestParameters(hoursServiceEndpoint + "/all", hoursRequestParameters);
        List<HoursResponse> hoursList;
        try {
            hoursList = convertMapListToHoursList(restTemplate.getForObject(hoursTemplateUrl, List.class));
        } catch (HttpClientErrorException e) {
            HashMap<String, Object> response = new HashMap<>();
            response.put("service", "hours");
            response.put("details", e.getMessage());
            return new ResponseEntity(response, e.getStatusCode());
        }

        String bookingsTemplateUrl = addRequestParameters(bookingsServiceEndpoint + "/all", bookingsRequestParameters);
        List<BookingResponse> bookingsList = new ArrayList<>();
        try {
            bookingsList = convertMapListToBookingList(restTemplate.getForObject(bookingsTemplateUrl, List.class));
        } catch (HttpClientErrorException e) {
            if (!e.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                HashMap<String, Object> response = new HashMap<>();
                response.put("service", "hours");
                response.put("details", e.getMessage());
                return new ResponseEntity(response, e.getStatusCode());
            }
        }

        List<AvailabilityEntity> allAvailabilities = availabilityService.checkAllAvailabilities(hoursList, bookingsList);

        return new ResponseEntity(allAvailabilities, HttpStatus.ACCEPTED);
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


    /**
     * Adds the provided requestParameters to the provided url in the format needed for API requests.
     * For example: <url>?hoursId=1&workerUsername=bob
     *
     * @param url               The url to add request parameters to
     * @param requestParameters The request parameters to add to the url
     * @return The url with the request parameters added
     */
    private String addRequestParameters(String url, List<RequestParameter> requestParameters) {
        if (requestParameters != null && !requestParameters.isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder(url);
            for (int i = 0; i < requestParameters.size(); i++) {
                if (i == 0) {
                    stringBuilder.append("?");
                } else {
                    stringBuilder.append("&");
                }
                stringBuilder.append(requestParameters.get(i));
            }

            return stringBuilder.toString();
        }

        return url;
    }

//	private <E> List<E> convertMapListToEntityList(List<Map> mapList, Class<E> entityClass) {
//		final ObjectMapper objectMapper = new ObjectMapper();
//		return mapList.stream()
//				.map(map -> objectMapper.convertValue(map, entityClass))
//				.collect(Collectors.toList());
//	}

    private List<HoursResponse> convertMapListToHoursList(List<Map> mapList) {
        return mapList.stream().map(map -> {
            Object hoursId = map.get("hoursId");
            Object workerUsername = map.get("workerUsername");
            Object creatorUsername = map.get("creatorUsername");
            Object startDateTime = map.get("startDateTime");
            Object endDateTime = map.get("endDateTime");

            return new HoursResponse(
                    hoursId == null ? null : Integer.parseInt(hoursId.toString()),
                    workerUsername == null ? null : workerUsername.toString(),
                    creatorUsername == null ? null : creatorUsername.toString(),
                    startDateTime == null ? null : LocalDateTime.parse(startDateTime.toString()),
                    endDateTime == null ? null : LocalDateTime.parse(endDateTime.toString()));
        }).collect(Collectors.toList());
    }

    private List<BookingResponse> convertMapListToBookingList(List<Map> mapList) {
        return mapList.stream().map(map -> {
            Object hoursId = map.get("bookingId");
            Object workerUsername = map.get("workerUsername");
            Object customerUsername = map.get("customerUsername");
            Object startDateTime = map.get("startDateTime");
            Object endDateTime = map.get("endDateTime");

            return new BookingResponse(
                    hoursId == null ? null : Integer.parseInt(hoursId.toString()),
                    workerUsername == null ? null : workerUsername.toString(),
                    customerUsername == null ? null : customerUsername.toString(),
                    startDateTime == null ? null : LocalDateTime.parse(startDateTime.toString()),
                    endDateTime == null ? null : LocalDateTime.parse(endDateTime.toString()));
        }).collect(Collectors.toList());
    }
}
