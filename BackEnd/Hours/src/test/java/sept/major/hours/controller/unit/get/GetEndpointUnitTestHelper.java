package sept.major.hours.controller.unit.get;

import org.springframework.http.ResponseEntity;
import sept.major.hours.controller.unit.UnitTestHelper;
import sept.major.hours.entity.HoursEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static sept.major.hours.HoursTestHelper.*;

public abstract class GetEndpointUnitTestHelper extends UnitTestHelper {

    /**
     * GET by rage, GET by date and GET all endpoints all user usernames to filter records.
     * To save on repeated code we have an abstract implementation of testing username filters,
     * to test the username filters the tests call this method
     *
     * @param expected  What the test expects the result to be
     * @param returned  What the mocked interface method will return
     * @param startDate The start date to retrieve records after. Equal to date for GET by date and null for GET all
     * @param endDate   The end date to retrieve records before. Equal to date for GET by date and null for GET all
     */
    protected void testWithUsernameFilters(ResponseEntity expected, List<HoursEntity> returned, String startDate, String endDate) {
        runTest(expected, returned, startDate, endDate, null, null); // Test without any usernames

        // If returned is null then there is no wey to test usernames
        if (returned != null) {
            workerUsernameFilter(expected, returned, startDate, endDate);
            creatorUsernameFilter(expected, returned, startDate, endDate);
            creatorAndWorkerUsernameFilter(expected, returned, startDate, endDate);
        }
    }

    /*
        Give all returned records the worker username and add another record without that worker username.
        If the controller correctly filters worker usernames then the additional record will be ignored and all the others kept.
     */
    private void workerUsernameFilter(ResponseEntity expected, List<HoursEntity> returned, String startDate, String endDate) {
        String workerUsername = randomAlphanumericString(20);

        List<HoursEntity> workerUsernameEntities = returned.stream().map(hoursEntity -> {
            HoursEntity newHoursEntity = new HoursEntity(
                    workerUsername,
                    hoursEntity.getCreatorUsername(),
                    hoursEntity.getStartDateTime(),
                    hoursEntity.getEndDateTime());
            newHoursEntity.setHoursId(hoursEntity.getHoursId());
            return newHoursEntity;
        }).collect(Collectors.toList());


        // Adding the additional record with the random worker username
        workerUsernameEntities.add(randomEntityWithDateRange(randomInt(4),
                (startDate == null) ? null : LocalDateTime.parse(startDate),
                (endDate == null) ? null : LocalDateTime.parse(endDate)));

        // Updates the expected records with the worker username so that test correctly evaluates the result.
        ResponseEntity updatedExpected = updateExpectedWithUsername(expected, workerUsername, null);

        runTest(updatedExpected, workerUsernameEntities, startDate, endDate, workerUsername, null);


    }

    /*
        Give all returned records the creator username and add another record without that creator username.
        If the controller correctly filters creator usernames then the additional record will be ignored and all the others kept.
     */
    private void creatorUsernameFilter(ResponseEntity expected, List<HoursEntity> returned, String startDate, String endDate) {
        String creatorUsername = randomAlphanumericString(20);

        List<HoursEntity> creatorUsernameEntities = returned.stream().map(hoursEntity -> {
            HoursEntity newHoursEntity = new HoursEntity(
                    hoursEntity.getWorkerUsername(),
                    creatorUsername,
                    hoursEntity.getStartDateTime(),
                    hoursEntity.getEndDateTime());
            newHoursEntity.setHoursId(hoursEntity.getHoursId());
            return newHoursEntity;
        }).collect(Collectors.toList());

        // Adding the additional record with the random creator username
        creatorUsernameEntities.add(randomEntityWithDateRange(randomInt(4),
                (startDate == null) ? null : LocalDateTime.parse(startDate),
                (endDate == null) ? null : LocalDateTime.parse(endDate)));

        // Updates the expected records with the worker username so that test correctly evaluates the result.
        ResponseEntity updatedExpected = updateExpectedWithUsername(expected, null, creatorUsername);

        runTest(updatedExpected, creatorUsernameEntities, startDate, endDate, null, creatorUsername);

    }

    /*
       Give all returned records both the worker and creator usernames, then adds a record with just the worker username and a
       record with just the creator username and then finally a record with neither usernames.
       If the controller correctly filters creator usernames then the additional records will be ignored and all the others kept.
    */
    private void creatorAndWorkerUsernameFilter(ResponseEntity expected, List<HoursEntity> returned, String startDate, String endDate) {
        String creatorUsername = randomAlphanumericString(20);
        String workerUsername = randomAlphanumericString(20);

        List<HoursEntity> usernameEntities = returned.stream().map(hoursEntity -> {
            HoursEntity newHoursEntity = new HoursEntity(
                    workerUsername,
                    creatorUsername,
                    hoursEntity.getStartDateTime(),
                    hoursEntity.getEndDateTime());
            newHoursEntity.setHoursId(hoursEntity.getHoursId());
            return newHoursEntity;
        }).collect(Collectors.toList());

        // Add the record with just the creator username
        HoursEntity creatorUsernameEntity = randomEntityWithDateRange(randomInt(4), (startDate == null) ? null : LocalDateTime.parse(startDate), (endDate == null) ? null : LocalDateTime.parse(endDate));
        creatorUsernameEntity.setCreatorUsername(creatorUsername);
        usernameEntities.add(creatorUsernameEntity);

        // Add the record with just the worker username
        HoursEntity workerUsernameEntity = randomEntityWithDateRange(randomInt(4), (startDate == null) ? null : LocalDateTime.parse(startDate), (endDate == null) ? null : LocalDateTime.parse(endDate));
        workerUsernameEntity.setWorkerUsername(workerUsername);
        usernameEntities.add(workerUsernameEntity);

        // Add the record with neither username
        usernameEntities.add(randomEntityWithDateRange(randomInt(4), (startDate == null) ? null : LocalDateTime.parse(startDate), (endDate == null) ? null : LocalDateTime.parse(endDate)));

        runTest(updateExpectedWithUsername(expected, workerUsername, creatorUsername), usernameEntities, startDate, endDate, workerUsername, creatorUsername);
    }

    /*
        Updates the expected response with the username provided.
        We don't know if the expected response is list or a single entity so firstly check and update respectively
     */
    private ResponseEntity updateExpectedWithUsername(ResponseEntity expected, String workerUsername, String creatorUsername) {
        Object expectedBody = expected.getBody();
        if (expectedBody instanceof HoursEntity) { // Expected response is a single entity
            HoursEntity hoursEntity = (HoursEntity) expectedBody;
            if (workerUsername != null) {
                hoursEntity.setWorkerUsername(workerUsername);
            }
            if (creatorUsername != null) {
                hoursEntity.setCreatorUsername(creatorUsername);
            }
            return new ResponseEntity(hoursEntity, expected.getStatusCode());
        }
        if (expectedBody instanceof List) { // Expected response is a list
            List<HoursEntity> entityList = (List<HoursEntity>) expectedBody;
            entityList.forEach(hoursEntity -> {
                if (workerUsername != null) {
                    hoursEntity.setWorkerUsername(workerUsername);
                }
                if (creatorUsername != null) {
                    hoursEntity.setCreatorUsername(creatorUsername);
                }
            });

            return new ResponseEntity(entityList, expected.getStatusCode());
        }

        return expected;
    }

    protected abstract void runTest(ResponseEntity expected, List<HoursEntity> returned, String startDate, String endDate, String workerUsername, String creatorUsername);
}
