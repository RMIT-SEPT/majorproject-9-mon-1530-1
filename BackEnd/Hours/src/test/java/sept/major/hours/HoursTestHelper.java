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

    protected Map<String, Object> randomEntityMap() {
        return new HashMap<String, Object>() {{
            put("hoursId", randomAlphanumericString(4));
            put("workerUsername", randomAlphanumericString(20));
            put("customerUsername", randomAlphanumericString(20));
            put("startDateTime", LocalDateTime.now().toString());
            put("endDateTime", LocalDateTime.now().toString());
        }};
    }

    protected Map<String, Object> randomEntityMap(String id) {
        return new HashMap<String, Object>() {{
            put("hoursId", id);
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

    protected HoursEntity entityMapToEntity(Map<String, Object> map) {
        return new HoursEntity(
                (map.get("hoursId") == null) ? null : map.get("hoursId").toString(),
                (map.get("workerUsername") == null) ? null : map.get("workerUsername").toString(),
                (map.get("customerUsername") == null) ? null : map.get("customerUsername").toString(),
                (map.get("startDateTime") == null) ? null : map.get("startDateTime").toString(),
                (map.get("endDateTime") == null) ? null : map.get("endDateTime").toString()
        );
    }
}
