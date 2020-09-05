package sept.major.users.unit.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import sept.major.users.entity.UserEntity;
import sept.major.users.unit.UserServiceTestHelper;

import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static sept.major.users.UserServiceTestHelper.randomAlphanumericString;

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
