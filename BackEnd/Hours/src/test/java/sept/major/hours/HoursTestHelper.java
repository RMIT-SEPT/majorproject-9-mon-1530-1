package sept.major.hours;

import sept.major.hours.entity.HoursEntity;

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

    public static int randomInt(int length) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            stringBuilder.append((int) ((Math.random() * (9) + 9)));
        }

        return new Integer(stringBuilder.toString());
    }

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

    public static Map<String, String> randomEntityMap() {
        LocalDateTime timeNow = LocalDateTime.now();
        timeNow = timeNow.minusNanos(timeNow.getNano());

        HashMap<String, String> entityMap = new HashMap<>();

        entityMap.put("workerUsername", randomAlphanumericString(20));
        entityMap.put("creatorUsername", randomAlphanumericString(20));
        entityMap.put("startDateTime", timeNow.toString());
        entityMap.put("endDateTime", timeNow.toString());

        return entityMap;
    }

    public static LocalDate pastDate(int years, int months, int days) {
        LocalDate date = LocalDate.now();
        date = date.minusYears(years);
        date = date.minusMonths(months);
        return date.minusDays(days);
    }

    public static LocalDate futureDate(int years, int months, int days) {
        LocalDate date = LocalDate.now();
        date = date.plusYears(years);
        date = date.plusMonths(months);
        return date.plusDays(days);
    }

    public static LocalDateTime pastDateTime(int years, int months, int days) {
        LocalDateTime date = LocalDateTime.now();
        date = date.minusYears(years);
        date = date.minusMonths(months);
        date = date.minusDays(days);
        return date;
    }

    public static LocalDateTime futureDateTime(int years, int months, int days) {
        LocalDateTime date = LocalDateTime.now();
        date = date.plusYears(years);
        date = date.plusMonths(months);
        date = date.plusDays(days);
        return date;
    }

    public static HoursEntity entityMapToEntity(Map<String, String> map) {
        return new HoursEntity(
                (map.get("workerUsername") == null) ? null : map.get("workerUsername").toString(),
                (map.get("creatorUsername") == null) ? null : map.get("creatorUsername").toString(),
                LocalDateTime.parse(map.get("startDateTime")),
                LocalDateTime.parse(map.get("endDateTime"))
        );
    }

    public static HoursEntity randomEntity(int id) {
        return entityMapToEntity(randomEntityMap(id));
    }

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

    public static List<HoursEntity> deepCopy(List<HoursEntity> toCopy) {
        if (toCopy == null || toCopy.contains(null)) {
            return null;
        }
        List<HoursEntity> copiedList = new ArrayList<>();
        toCopy.forEach(hoursEntity -> {
            HoursEntity copiedEntity = new HoursEntity(
                    hoursEntity.getWorkerUsername(),
                    hoursEntity.getCreatorUsername(),
                    hoursEntity.getStartDateTime(),
                    hoursEntity.getEndDateTime());
            copiedEntity.setHoursId(hoursEntity.getHoursId());
            copiedList.add(copiedEntity);
        });

        return copiedList;
    }
}
