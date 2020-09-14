package sept.major.bookings;

import sept.major.bookings.entity.BookingEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BookingsTestHelper {
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

    public static Map<String, String> randomEntityMap() {
        return new HashMap<String, String>() {{
            put("workerUsername", randomAlphanumericString(20));
            put("customerUsername", randomAlphanumericString(20));
            put("startDateTime", LocalDateTime.now().toString());
            put("endDateTime", LocalDateTime.now().plusHours(4).toString());
        }};
    }

    public static Map<String, String> randomEntityMap(LocalDateTime timeToUse) {
        return new HashMap<String, String>() {{
            put("workerUsername", randomAlphanumericString(20));
            put("customerUsername", randomAlphanumericString(20));
            put("startDateTime", timeToUse.toString());
            put("endDateTime", timeToUse.toString());
        }};
    }

    public static BookingEntity randomEntityWithDate(Integer id, LocalDate date) {
        BookingEntity bookingEntity = new BookingEntity(
                randomAlphanumericString(20),
                randomAlphanumericString(20),
                date.atStartOfDay(),
                date.atTime(23, 59)
        );
        bookingEntity.setBookingId(id);
        return bookingEntity;
    }

    public static Map<String, String> randomEntityMap(Integer id) {
        LocalDateTime timeNow = LocalDateTime.now();
        timeNow = timeNow.minusNanos(timeNow.getNano());

        HashMap<String, String> entityMap = new HashMap<>();

        entityMap.put("bookingId", (id == null) ? null : id.toString());
        entityMap.put("workerUsername", randomAlphanumericString(20));
        entityMap.put("customerUsername", randomAlphanumericString(20));
        entityMap.put("startDateTime", timeNow.toString());
        entityMap.put("endDateTime", timeNow.toString());

        return entityMap;
    }

    public static BookingEntity randomEntityWithDateRange(Integer id, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        BookingEntity hoursEntity = new BookingEntity(
                randomAlphanumericString(20),
                randomAlphanumericString(20),
                startDateTime,
                endDateTime
        );
        hoursEntity.setBookingId(id);
        return hoursEntity;
    }

    public static BookingEntity createBookingEntity(Map<String, String> entityMap) {
        return new BookingEntity(
                (String) entityMap.get("workerUsername"),
                (String) entityMap.get("customerUsername"),
                LocalDateTime.parse(entityMap.get("startDateTime")),
                LocalDateTime.parse(entityMap.get("endDateTime"))
        );
    }


    public static BookingEntity randomEntity() {
        return createBookingEntity(randomEntityMap());
    }

    public static BookingEntity randomEntity(int id) {
        return createBookingEntity(randomEntityMap(id));
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

    public static List<BookingEntity> deepCopy(List<BookingEntity> toCopy) {
        if (toCopy == null || toCopy.contains(null)) {
            return null;
        }
        List<BookingEntity> copiedList = new ArrayList<>();
        toCopy.forEach(hoursEntity -> {
            BookingEntity copiedEntity = new BookingEntity(
                    hoursEntity.getWorkerUsername(),
                    hoursEntity.getCustomerUsername(),
                    hoursEntity.getStartDateTime(),
                    hoursEntity.getEndDateTime());
            copiedEntity.setBookingId(hoursEntity.getBookingId());
            copiedList.add(copiedEntity);
        });

        return copiedList;
    }
}
