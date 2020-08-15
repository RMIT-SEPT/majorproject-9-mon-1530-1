package sept.major.users.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import sept.major.users.UserServiceTestHelper;

@SpringBootTest
public class UsersServicePasswordTest extends UserServiceTestHelper {
	
	@Test
	  void comparePasswordTestMiss() {
		  String username ="";
		  String password ="";
		  
		 boolean result= getUserService().comparePassword(username, password);
		 
		 assertFalse(result);
	  }
	@Test
	  void comparePasswordTest() {
		  String username ="ab@gmail.com";
		  String password ="22222";
		  
		 boolean result= getUserService().comparePassword(username, password);
		 
		 assertTrue(result);
	  }

}
