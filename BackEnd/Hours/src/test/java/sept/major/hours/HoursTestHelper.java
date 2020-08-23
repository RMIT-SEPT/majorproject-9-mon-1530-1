package sept.major.hours;

import sept.major.hours.entity.HoursEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public abstract class HoursTestHelper {
    protected String randomAlphanumericString(int length) {
        final int[] uppercaseRange = {65, 91};
        final int[] lowerCaseRange = {97, 123};
        final int[] numbersRange = {48, 58};

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int rangeToUse = (int) ((Math.random() * (4 - 1) + 1));
            int charToUse;
            if (rangeToUse == 1) {
                charToUse = (int) ((Math.random() * (uppercaseRange[0] - uppercaseRange[1]) + uppercaseRange[1]));
            } else if (rangeToUse == 2) {
                charToUse = (int) ((Math.random() * (lowerCaseRange[0] - lowerCaseRange[1]) + lowerCaseRange[1]));
            } else {
                charToUse = (int) ((Math.random() * (numbersRange[0] - numbersRange[1]) + numbersRange[1]));
            }
            stringBuilder.append((char) charToUse);
        }

        return stringBuilder.toString();
    }

    protected Integer randomInt(int length) {
        final int numbersUpper = 58;
        final int numberLower = 48;

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int charToUse = (int) ((Math.random() * (numberLower - numbersUpper) + numbersUpper));
            stringBuilder.append((char) charToUse);
        }

        return new Integer(stringBuilder.toString());
    }

    protected Map<String, String> randomEntityMap() {
        return new HashMap<String, String>() {{
            put("hoursId", randomInt(4).toString());
            put("workerUsername", randomAlphanumericString(20));
            put("customerUsername", randomAlphanumericString(20));
            put("startDateTime", LocalDateTime.now().toString());
            put("endDateTime", LocalDateTime.now().toString());
        }};
    }

    protected Map<String, String> randomEntityMap(Integer id) {
        return new HashMap<String, String>() {{
            put("hoursId", (id == null) ? null : id.toString());
            put("workerUsername", randomAlphanumericString(20));
            put("customerUsername", randomAlphanumericString(20));
            put("startDateTime", LocalDateTime.now().toString());
            put("endDateTime", LocalDateTime.now().toString());
        }};
    }

    protected LocalDate pastDate(int years, int months, int days) {
        LocalDate date = LocalDate.now();
        date = date.minusYears(years);
        date = date.minusMonths(months);
        return date.minusDays(days);
    }

    protected HoursEntity entityMapToEntity(Map<String, String> map) {
        return new HoursEntity(
                (map.get("workerUsername") == null) ? null : map.get("workerUsername").toString(),
                (map.get("customerUsername") == null) ? null : map.get("customerUsername").toString(),
                (map.get("startDateTime") == null) ? null : LocalDateTime.parse(map.get("startDateTime")),
                (map.get("endDateTime") == null) ? null : LocalDateTime.parse(map.get("endDateTime"))
        );
    }
}
