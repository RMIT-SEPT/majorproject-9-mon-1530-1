package sept.major.users.controller.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import sept.major.users.controller.UserServiceController;
import sept.major.users.controller.UserServiceControllerHelper;
import sept.major.users.repository.UsersRepository;
import sept.major.users.service.UserService;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest

public class UnitTestHelper {
    protected UserServiceController userServiceController;
    protected UserServiceControllerHelper userServiceControllerHelper;
    protected UserService userService;

    @Mock
    protected UsersRepository mockedUserRepository;

    @BeforeEach
    public void setUp() {
        userService = new UserService(mockedUserRepository);
        userServiceControllerHelper = new UserServiceControllerHelper(userService);
        userServiceController = new UserServiceController(userService, userServiceControllerHelper);
    }

    @Test
    void contextLoads() {
        assertThat(userServiceController).isNotNull();
    }
}
