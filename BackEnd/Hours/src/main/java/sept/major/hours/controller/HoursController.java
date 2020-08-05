package sept.major.hours.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sept.major.hours.entity.HoursEntity;

import java.util.Map;

@RestController
@RequestMapping("/hours")
public class HoursController {

    @Autowired
    HoursControllerHelper hoursControllerHelper;

    @GetMapping("/range")
    public ResponseEntity getHoursInRange(@RequestParam(required = false) String startDate,
                                          @RequestParam(required = false) String endDate,
                                          @RequestParam(required = false) String workerUsename,
                                          @RequestParam(required = false) String customerUsername) {
        return new ResponseEntity("Endpoint not implemented", HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/date")
    public ResponseEntity getHoursInDate(@RequestParam String date,
                                         @RequestParam(required = false) String workerUsername,
                                         @RequestParam(required = false) String customerUsername) {
        return new ResponseEntity("Endpoint not implemented", HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/all")
    public ResponseEntity getAllHours(@RequestParam(required = false) String workerUsername,
                                      @RequestParam(required = false) String customerUsername) {
        return new ResponseEntity("Endpoint not implemented", HttpStatus.BAD_REQUEST);
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
