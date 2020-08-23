package sept.major.hours.controller.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import sept.major.hours.HoursTestHelper;
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
public abstract class HoursUnitTestHelper extends HoursTestHelper {

    protected HoursController hoursController;
    protected HoursControllerHelper hoursControllerHelper;
    protected HoursService hoursService;

    @Mock
    protected HoursRepository mockedUserRepository;

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

    protected HoursEntity randomEntity(int id) {
        HoursEntity hoursEntity = new HoursEntity(
                randomAlphanumericString(20),
                randomAlphanumericString(20),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        hoursEntity.setHoursId(id);
        return hoursEntity;
    }

    protected HoursEntity randomEntityWithDate(Integer id, LocalDate date) {
        HoursEntity hoursEntity = new HoursEntity(
                randomAlphanumericString(20),
                randomAlphanumericString(20),
                date.atStartOfDay(),
                date.atTime(23, 59)
        );
        hoursEntity.setHoursId(id);
        return hoursEntity;
    }

    protected HoursEntity randomEntityWithDateRange(Integer id, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        HoursEntity hoursEntity = new HoursEntity(
                randomAlphanumericString(20),
                randomAlphanumericString(20),
                startDateTime,
                endDateTime
        );
        hoursEntity.setHoursId(id);
        return hoursEntity;
    }

    protected List<HoursEntity> deepCopy(List<HoursEntity> toCopy) {
        if (toCopy == null || toCopy.contains(null)) {
            return null;
        }
        List<HoursEntity> copiedList = new ArrayList<>();
        toCopy.forEach(hoursEntity -> {
            HoursEntity copiedEntity = new HoursEntity(hoursEntity.getWorkerUsername(),
                    hoursEntity.getCustomerUsername(),
                    hoursEntity.getStartDateTime(),
                    hoursEntity.getEndDateTime());
            copiedEntity.setHoursId(hoursEntity.getHoursId());
            copiedList.add(copiedEntity);
        });

        return copiedList;
    }
}
