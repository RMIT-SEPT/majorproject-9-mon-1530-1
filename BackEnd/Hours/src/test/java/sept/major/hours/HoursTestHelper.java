package sept.major.hours;

import sept.major.hours.entity.HoursEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

public abstract class HoursTestHelper {
    /**
     * Generates a random string with numbers and letters (both upper case and lower case)
     *
     * @param length The length of the string to generate
     * @return The string generated
     */
    public static String randomAlphanumericString(int length) {
        final int[] uppercaseRange = {65, 91}; // The ASCII range for upper case letters
        final int[] lowerCaseRange = {97, 123}; // The ASCII range for lower case letters
        final int[] numbersRange = {48, 58}; // The ASCII range for numbers

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int rangeToUse = (int) ((Math.random() * (4 - 1) + 1)); // Generates number between 1-3
            int charToUse;
            if (rangeToUse == 1) {
                // Get random number in the range
                charToUse = (int) ((Math.random() * (uppercaseRange[0] - uppercaseRange[1]) + uppercaseRange[1]));
            } else if (rangeToUse == 2) {
                // Get random number in the range
                charToUse = (int) ((Math.random() * (lowerCaseRange[0] - lowerCaseRange[1]) + lowerCaseRange[1]));
            } else {
                // Get random number in the range
                charToUse = (int) ((Math.random() * (numbersRange[0] - numbersRange[1]) + numbersRange[1]));
            }
            stringBuilder.append((char) charToUse);
        }

        return stringBuilder.toString();
    }


    /**
     * Generate a random integer with the provided length
     *
     * @param length How many digits in the integer
     * @return The integer generated
     */
    public static Integer randomInt(int length) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            stringBuilder.append((int) ((Math.random() * (9) + 9)));
        }

        return new Integer(stringBuilder.toString());
    }

    /**
     * Generates with map randomly with the values to represent a {@link HoursEntity}
     *
     * @return The randomly generated map
     */
    public static Map<String, String> randomEntityMap() {
        return new HashMap<String, String>() {{
            put("workerUsername", randomAlphanumericString(20));
            put("creatorUsername", randomAlphanumericString(20));
            put("startDateTime", LocalDateTime.now().toString());
            put("endDateTime", LocalDateTime.now().plusHours(4).toString());
        }};
    }

    /**
     * Generates with map randomly with the values to represent a {@link HoursEntity} with the startDateTime and endDateTime
     * being the {@link LocalDateTime} provided
     *
     * @param timeToUse The value of the startDateTime and endDateTime
     * @return The randomly generated map
     */
    public static Map<String, String> randomEntityMap(LocalDateTime timeToUse) {
        return new HashMap<String, String>() {{
            put("workerUsername", randomAlphanumericString(20));
            put("creatorUsername", randomAlphanumericString(20));
            put("startDateTime", timeToUse.toString());
            put("endDateTime", timeToUse.toString());
        }};
    }

    /**
     * Generates with map randomly with the values to represent a {@link HoursEntity} with the startDateTime and endDateTime
     * being based on the {@link LocalDate} provided and the hoursId being the id provided
     *
     * @param id   The value of the hoursId
     * @param date The value to base the startDateTime and endDateTime on
     * @return The randomly generated map
     */
    public static HoursEntity randomEntityWithDate(Integer id, LocalDate date) {
        HoursEntity hoursEntity = new HoursEntity(
                randomAlphanumericString(20),
                randomAlphanumericString(20),
                date.atStartOfDay(),
                date.atTime(23, 59)
        );
        hoursEntity.setHoursId(id);
        return hoursEntity;
    }

    /**
     * Generates with map randomly with the values to represent a {@link HoursEntity} with hoursId being the provided id
     *
     * @param id The value of the hoursId
     * @return The randomly generated map
     */
    public static Map<String, String> randomEntityMap(Integer id) {
        LocalDateTime timeNow = LocalDateTime.now();
        timeNow = timeNow.minusNanos(timeNow.getNano());

        HashMap<String, String> entityMap = new HashMap<>();

        entityMap.put("hoursId", (id == null) ? null : id.toString());
        entityMap.put("workerUsername", randomAlphanumericString(20));
        entityMap.put("creatorUsername", randomAlphanumericString(20));
        entityMap.put("startDateTime", timeNow.toString());
        entityMap.put("endDateTime", timeNow.toString());

        return entityMap;
    }

    /**
     *
     * Generates with map randomly with the values to represent a {@link HoursEntity} with the startDateTime and endDateTime
     * being the {@link LocalDateTime}s provided and the hoursId being the id provided
     *
     * @param id The value of the hoursId
     * @param startDateTime The value of the startDateTime
     * @param endDateTime The value of the endDateTime
     * @return The random generated map
     */
    public static HoursEntity randomEntityWithDateRange(Integer id, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        HoursEntity hoursEntity = new HoursEntity(
                randomAlphanumericString(20),
                randomAlphanumericString(20),
                startDateTime,
                endDateTime
        );
        hoursEntity.setHoursId(id);
        return hoursEntity;
    }

    /**
     * Creates a {@link HoursEntity} based on the values in the provided HashMap
     *
     * @param entityMap The {@link HashMap} to create the {@link HoursEntity} from
     * @return The {@link HoursEntity} created from the {@link HashMap}
     */
    public static HoursEntity createHoursEntity(Map<String, String> entityMap) {

        LocalDateTime startDateTime = null;
        if (entityMap.get("startDateTime") != null) {
            try {
                startDateTime = LocalDateTime.parse(entityMap.get("startDateTime"));
            } catch (DateTimeParseException e) {
                // Some tests will use an invalid startDateTime and therefor this is ignored
            }
        }

        LocalDateTime endDateTime = null;
        if (entityMap.get("endDateTime") != null) {
            try {
                endDateTime = LocalDateTime.parse(entityMap.get("endDateTime"));
            } catch (DateTimeParseException e) {
                // Some tests will use an invalid endDateTime and therefor this is ignored
            }
        }

        return new HoursEntity(
                entityMap.get("workerUsername"),
                entityMap.get("creatorUsername"),
                startDateTime,
                endDateTime
        );
    }


    /**
     * Convenience method for created a random {@link HoursEntity}
     *
     * @return The random {@link HoursEntity}
     */
    public static HoursEntity randomEntity() {
        return createHoursEntity(randomEntityMap());
    }


    /**
     * Convenience method for created a random {@link HoursEntity} with hoursId being the provided
     * @param id The value of the hoursId
     * @return The random {@link HoursEntity}
     */
    public static HoursEntity randomEntity(int id) {
        return createHoursEntity(randomEntityMap(id));
    }


    /**
     * A LocalDate years/months/days before the current date
     *
     * @param years  How many years to go back
     * @param months How many months to go back
     * @param days   How many days to go back
     * @return
     */
    public static LocalDate pastDate(int years, int months, int days) {
        LocalDate date = LocalDate.now();
        date = date.minusYears(years);
        date = date.minusMonths(months);
        return date.minusDays(days);
    }

    /**
     *
     * A LocalDate years/months/days after the current date
     *
     * @param years How many years to go forward
     * @param months How many months to go forward
     * @param days How many days to go forward
     * @return
     */
    public static LocalDate futureDate(int years, int months, int days) {
        LocalDate date = LocalDate.now();
        date = date.plusYears(years);
        date = date.plusMonths(months);
        return date.plusDays(days);
    }

    /**
     *
     * A LocalDateTime years/months/days before the current date
     *
     * @param years How many years to go back
     * @param months How many months to go back
     * @param days How many days to go back
     * @return
     */
    public static LocalDateTime pastDateTime(int years, int months, int days) {
        LocalDateTime date = LocalDateTime.now();
        date = date.minusYears(years);
        date = date.minusMonths(months);
        date = date.minusDays(days);
        return date;
    }

    /**
     *
     * A LocalDateTime years/months/days after the current date
     *
     * @param years How many years to go forward
     * @param months How many months to go forward
     * @param days How many days to go forward
     * @return
     */
    public static LocalDateTime futureDateTime(int years, int months, int days) {
        LocalDateTime date = LocalDateTime.now();
        date = date.plusYears(years);
        date = date.plusMonths(months);
        date = date.plusDays(days);
        return date;
    }


    /**
     *
     * Updates the provided entity with the values provided
     *
     * @param existing The entity to update
     * @param patchValues The values to update the entity with
     * @return The updated entity
     */
    public static HoursEntity patchEntity(HoursEntity existing, HashMap<String, String> patchValues) {

        HoursEntity newEntity = new HoursEntity(existing.getWorkerUsername(), existing.getCreatorUsername(), existing.getStartDateTime(), existing.getEndDateTime());

        if (patchValues.get("hoursId") != null) {
            newEntity.setHoursId(new Integer(patchValues.get("hoursId")));
        }

        if (patchValues.get("workerUsername") != null) {
            newEntity.setWorkerUsername(patchValues.get("workerUsername"));
        }

        if (patchValues.get("creatorUsername") != null) {
            newEntity.setCreatorUsername(patchValues.get("creatorUsername"));
        }

        if (patchValues.get("startDateTime") != null) {
            newEntity.setStartDateTime(LocalDateTime.parse(patchValues.get("startDateTime")));
        }

        if (patchValues.get("endDateTime") != null) {
            newEntity.setEndDateTime(LocalDateTime.parse(patchValues.get("endDateTime")));
        }

        return newEntity;
    }
}
