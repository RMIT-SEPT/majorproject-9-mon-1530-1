package sept.major.users.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import sept.major.users.UserServiceTestHelper;
import sept.major.users.entity.UserEntity;

@SpringBootTest
public class UsersServicePasswordTest extends UserServiceTestHelper {

	@Test
	void comparePasswordTestMiss() {
		String username = "";
		String password = "";

		boolean result = getUserService().comparePassword(username, password);

		assertFalse(result);
	}	
	
	@Test
	void comparePasswordTest() {
		String username = "ab@gmail.com";
		String password = "22222";

        UserEntity expected = new UserEntity(
                username,
                password,
                randomAlphanumericString(20),
                randomAlphanumericString(20),
                randomAlphanumericString(20),
                randomAlphanumericString(20)
        );
        
		when(mockedUserRepository.findByUsername(username)).thenReturn(Optional.of(expected));

		boolean result = getUserService().comparePassword(username, password);

		assertTrue(result);
	}

}
