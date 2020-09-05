package sept.major.users.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import sept.major.users.controller.UserServiceController;
import sept.major.users.controller.UserServiceControllerHelper;
import sept.major.users.repository.UsersRepository;
import sept.major.users.unit.service.UserService;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public abstract class UserServiceTestHelper {

    @Mock
    public UsersRepository mockedUserRepository;
    UserServiceController userServiceController;
    UserServiceControllerHelper userServiceControllerHelper;
    UserService userService;

    public UserService getUserService() {
        return userService;
    }

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
