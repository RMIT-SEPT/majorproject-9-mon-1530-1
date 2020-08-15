package sept.major.hours;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import sept.major.hours.controller.HoursController;
import sept.major.hours.controller.HoursControllerHelper;
import sept.major.hours.entity.HoursEntity;
import sept.major.hours.repository.HoursRepository;
import sept.major.hours.service.HoursService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public abstract class UserServiceTestHelper {

    HoursController hoursController;
    HoursControllerHelper hoursControllerHelper;
    HoursService hoursService;

    @Mock
    HoursRepository mockedUserRepository;

    @BeforeEach
    public void setUp() {
        hoursService = new HoursService(mockedUserRepository);
        hoursControllerHelper = new HoursControllerHelper(hoursService);
        hoursController = new HoursController(hoursService, hoursControllerHelper);
    }

    @Test
    void contextLoads() {
        assertThat(hoursController).isNotNull();
    }

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

    protected HoursEntity randomEntity(String id) {
        return new HoursEntity(
                id,
                randomAlphanumericString(20),
                randomAlphanumericString(20),
                LocalDateTime.now().toString(),
                LocalDateTime.now().toString()
        );
    }

    protected HoursEntity randomEntityWithDate(String id, String date) {
        return new HoursEntity(
                id,
                randomAlphanumericString(20),
                randomAlphanumericString(20),
                date,
                date
        );
    }

    protected HoursEntity randomEntityWithDateRange(String id, String startDateTime, String endDateTime) {
        return new HoursEntity(
                id,
                randomAlphanumericString(20),
                randomAlphanumericString(20),
                startDateTime,
                endDateTime
        );
    }

    protected LocalDate pastDate(int years, int months, int days) {
        LocalDate date = LocalDate.now();
        date = date.minusYears(years);
        date = date.minusMonths(months);
        return date.minusDays(days);
    }

    protected List<HoursEntity> deepCopy(List<HoursEntity> toCopy) {
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
