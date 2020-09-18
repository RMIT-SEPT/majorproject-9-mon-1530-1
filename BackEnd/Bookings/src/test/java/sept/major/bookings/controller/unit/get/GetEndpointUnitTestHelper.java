package sept.major.bookings.controller.unit.get;

import org.springframework.http.ResponseEntity;
import sept.major.bookings.controller.unit.UnitTestHelper;
import sept.major.bookings.entity.BookingEntity;

import java.time.LocalDateTime;
import java.util.List;

import static sept.major.bookings.BookingsTestHelper.*;

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
    protected void testWithUsernameFilters(ResponseEntity expected, List<BookingEntity> returned, String startDate, String endDate) {
        runTest(expected, returned, startDate, endDate, null, null); // Test without any usernames

        // If returned is null then there is no wey to test usernames
        if (returned != null) {
            workerUsernameFilter(expected, returned, startDate, endDate);
            customerUsernameFilter(expected, returned, startDate, endDate);
            customerAndWorkerUsernameFilter(expected, returned, startDate, endDate);
        }
    }

    /*
        Give all returned records the worker username and add another record without that worker username.
        If the controller correctly filters worker usernames then the additional record will be ignored and all the others kept.
     */
    private void workerUsernameFilter(ResponseEntity expected, List<BookingEntity> returned, String startDate, String endDate) {
        String workerUsername = randomAlphanumericString(20);

        List<BookingEntity> workerUsernameEntities = deepCopy(returned); // Need to deep copy the returned list otherwise it would impact other tests
        workerUsernameEntities.forEach(hoursEntity -> hoursEntity.setWorkerUsername(workerUsername));

        // Adding the additional record with the random worker username
        workerUsernameEntities.add(randomEntityWithDateRange(randomInt(4),
                (startDate == null) ? null : LocalDateTime.parse(startDate),
                (endDate == null) ? null : LocalDateTime.parse(endDate)));

        // Updates the expected records with the worker username so that test correctly evaluates the result.
        ResponseEntity updatedExpected = updateExpectedWithUsername(expected, workerUsername, null);

        runTest(updatedExpected, workerUsernameEntities, startDate, endDate, workerUsername, null);


    }

    /*
        Give all returned records the customer username and add another record without that customer username.
        If the controller correctly filters customer usernames then the additional record will be ignored and all the others kept.
     */
    private void customerUsernameFilter(ResponseEntity expected, List<BookingEntity> returned, String startDate, String endDate) {
        String customerUsername = randomAlphanumericString(20);

        List<BookingEntity> customerUsernameEntities = deepCopy(returned); // Need to deep copy the returned list otherwise it would impact other tests
        customerUsernameEntities.forEach(hoursEntity -> hoursEntity.setCustomerUsername(customerUsername));

        // Adding the additional record with the random customer username
        customerUsernameEntities.add(randomEntityWithDateRange(randomInt(4),
                (startDate == null) ? null : LocalDateTime.parse(startDate),
                (endDate == null) ? null : LocalDateTime.parse(endDate)));

        // Updates the expected records with the worker username so that test correctly evaluates the result.
        ResponseEntity updatedExpected = updateExpectedWithUsername(expected, null, customerUsername);

        runTest(updatedExpected, customerUsernameEntities, startDate, endDate, null, customerUsername);

    }

    /*
       Give all returned records both the worker and customer usernames, then adds a record with just the worker username and a
       record with just the customer username and then finally a record with neither usernames.
       If the controller correctly filters customer usernames then the additional records will be ignored and all the others kept.
    */
    private void customerAndWorkerUsernameFilter(ResponseEntity expected, List<BookingEntity> returned, String startDate, String endDate) {
        String customerUsername = randomAlphanumericString(20);
        String workerUsername = randomAlphanumericString(20);

        List<BookingEntity> usernameEntities = deepCopy(returned); // Need to deep copy the returned list otherwise it would impact other tests
        usernameEntities.forEach(hoursEntity -> {
            hoursEntity.setCustomerUsername(customerUsername);
            hoursEntity.setWorkerUsername(workerUsername);
        });

        // Add the record with just the customer username
        BookingEntity customerUsernameEntity = randomEntityWithDateRange(randomInt(4), (startDate == null) ? null : LocalDateTime.parse(startDate), (endDate == null) ? null : LocalDateTime.parse(endDate));
        customerUsernameEntity.setCustomerUsername(customerUsername);
        usernameEntities.add(customerUsernameEntity);

        // Add the record with just the worker username
        BookingEntity workerUsernameEntity = randomEntityWithDateRange(randomInt(4), (startDate == null) ? null : LocalDateTime.parse(startDate), (endDate == null) ? null : LocalDateTime.parse(endDate));
        workerUsernameEntity.setWorkerUsername(workerUsername);
        usernameEntities.add(workerUsernameEntity);

        // Add the record with neither username
        usernameEntities.add(randomEntityWithDateRange(randomInt(4), (startDate == null) ? null : LocalDateTime.parse(startDate), (endDate == null) ? null : LocalDateTime.parse(endDate)));

        runTest(updateExpectedWithUsername(expected, workerUsername, customerUsername), usernameEntities, startDate, endDate, workerUsername, customerUsername);
    }

    /*
        Updates the expected response with the username provided.
        We don't know if the expected response is list or a single entity so firstly check and update respectively
     */
    private ResponseEntity updateExpectedWithUsername(ResponseEntity expected, String workerUsername, String customerUsername) {
        Object expectedBody = expected.getBody();
        if (expectedBody instanceof BookingEntity) { // Expected response is a single entity
            BookingEntity hoursEntity = (BookingEntity) expectedBody;
            if (workerUsername != null) {
                hoursEntity.setWorkerUsername(workerUsername);
            }
            if (customerUsername != null) {
                hoursEntity.setCustomerUsername(customerUsername);
            }
            return new ResponseEntity(hoursEntity, expected.getStatusCode());
        }
        if (expectedBody instanceof List) { // Expected response is a list
            List<BookingEntity> entityList = (List<BookingEntity>) expectedBody;
            entityList.forEach(hoursEntity -> {
                if (workerUsername != null) {
                    hoursEntity.setWorkerUsername(workerUsername);
                }
                if (customerUsername != null) {
                    hoursEntity.setCustomerUsername(customerUsername);
                }
            });

            return new ResponseEntity(entityList, expected.getStatusCode());
        }

        return expected;
    }

    protected abstract void runTest(ResponseEntity expected, List<BookingEntity> returned, String startDate, String endDate, String workerUsername, String customerUsername);
}
