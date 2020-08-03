package sept.major.users;

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
public abstract class UserServiceTestHelper {

    UserServiceController userServiceController;
    UserServiceControllerHelper userServiceControllerHelper;
    UserService userService;

    @Mock
    UsersRepository mockedUserRepository;

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
}
