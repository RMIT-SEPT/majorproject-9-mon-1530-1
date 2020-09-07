package sept.major.hours.controller.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import sept.major.hours.controller.HoursController;
import sept.major.hours.controller.HoursControllerHelper;
import sept.major.hours.repository.HoursRepository;
import sept.major.hours.service.HoursService;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public abstract class HoursUnitTestHelper {

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
}
