package sept.major.hours;

import sept.major.hours.entity.HoursEntity;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class HoursTestHelper {
    public static String randomAlphanumericString(int length) {
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

    public static Map<String, Object> randomEntityMap() {
        return new HashMap<String, Object>() {{
            put("hoursId", randomAlphanumericString(4));
            put("workerUsername", randomAlphanumericString(20));
            put("customerUsername", randomAlphanumericString(20));
            put("startDateTime", Timestamp.valueOf(LocalDateTime.now()).toString());
            put("endDateTime", Timestamp.valueOf(LocalDateTime.now()).toString());
        }};
    }

    public static Map<String, Object> randomEntityMap(String id) {
        return new HashMap<String, Object>() {{
            put("hoursId", id);
            put("workerUsername", randomAlphanumericString(20));
            put("customerUsername", randomAlphanumericString(20));
            put("startDateTime", Timestamp.valueOf(LocalDateTime.now()).toString());
            put("endDateTime", Timestamp.valueOf(LocalDateTime.now()).toString());
        }};
    }

    public static LocalDate pastDate(int years, int months, int days) {
        LocalDate date = LocalDate.now();
        date = date.minusYears(years);
        date = date.minusMonths(months);
        return date.minusDays(days);
    }

    public static HoursEntity entityMapToEntity(Map<String, Object> map) {
        return new HoursEntity(
                (map.get("hoursId") == null) ? null : map.get("hoursId").toString(),
                (map.get("workerUsername") == null) ? null : map.get("workerUsername").toString(),
                (map.get("customerUsername") == null) ? null : map.get("customerUsername").toString(),
                (map.get("startDateTime") == null) ? null : map.get("startDateTime").toString(),
                (map.get("endDateTime") == null) ? null : map.get("endDateTime").toString()
        );
    }

    public static HoursEntity randomEntity(String id) {
        return entityMapToEntity(randomEntityMap(id));
    }

    public static HoursEntity randomEntityWithDate(String id, String date) {
        return new HoursEntity(
                id,
                randomAlphanumericString(20),
                randomAlphanumericString(20),
                date,
                date
        );
    }

    public static HoursEntity randomEntityWithDateRange(String id, String startDateTime, String endDateTime) {
        return new HoursEntity(
                id,
                randomAlphanumericString(20),
                randomAlphanumericString(20),
                startDateTime,
                endDateTime
        );
    }

    public static List<HoursEntity> deepCopy(List<HoursEntity> toCopy) {
        if (toCopy == null || toCopy.contains(null)) {
            return null;
        }
        List<HoursEntity> copiedList = new ArrayList<>();
        toCopy.forEach(hoursEntity -> {
            HoursEntity copiedEntity = new HoursEntity(hoursEntity.getHoursId(),
                    hoursEntity.getWorkerUsername(),
                    hoursEntity.getCustomerUsername(),
                    hoursEntity.getStartDateTime(),
                    hoursEntity.getEndDateTime());
            copiedList.add(copiedEntity);
        });

        return copiedList;
    }
}
