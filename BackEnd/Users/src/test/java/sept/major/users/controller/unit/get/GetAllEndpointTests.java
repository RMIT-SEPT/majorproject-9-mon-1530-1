package sept.major.users.controller.unit.get;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sept.major.users.controller.unit.UnitTestHelper;
import sept.major.users.entity.UserEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static sept.major.users.UserTestHelper.randomAlphanumericString;
import static sept.major.users.UserTestHelper.randomEntity;

@SpringBootTest
public class GetAllEndpointTests extends UnitTestHelper {

    @Test
    @DisplayName("Successfully retrieve one record with no userType")
    void valid() {
        List<UserEntity> expected = Arrays.asList(randomEntity());

        runTest(new ResponseEntity(expected, HttpStatus.OK),
                expected, null);
    }


    @Test
    @DisplayName("No record found with no userType")
    void missingResult() {
        runTest(new ResponseEntity("No record was found", HttpStatus.NOT_FOUND),
                Arrays.asList(), null);
    }


    @Test
    @DisplayName("Successfully retrieve many records with no userType")
    void retrieveMany() {
        List<UserEntity> userEntities = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            userEntities.add(randomEntity());
        }
        runTest(new ResponseEntity(userEntities, HttpStatus.OK), userEntities, null);
    }

    @Test
    @DisplayName("Successfully retrieve one record with userType")
    void validWithUserType() {
        String userType = randomAlphanumericString(20);

        List<UserEntity> expected = Arrays.asList(randomEntity(userType));

        runTest(new ResponseEntity(expected, HttpStatus.OK),
                expected, userType);
    }


    @Test
    @DisplayName("No record found with userType")
    void missingResultWithUserType() {
        String username = randomAlphanumericString(20);
        runTest(new ResponseEntity(String.format("No record with a userType of %s was found", username), HttpStatus.NOT_FOUND),
                Arrays.asList(), username);
    }


    @Test
    @DisplayName("Successfully retrieve many records with userType")
    void retrieveManyWithUserType() {
        String userType = randomAlphanumericString(20);

        List<UserEntity> userEntities = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            userEntities.add(randomEntity(userType));
        }
        runTest(new ResponseEntity(userEntities, HttpStatus.OK), userEntities, userType);
    }


    protected void runTest(ResponseEntity expected, List<UserEntity> returned, String userType) {
        if (userType == null) {
            when(mockedUserRepository.findAll()).thenReturn(returned);
        } else {
            when(mockedUserRepository.findAllByUserType(userType)).thenReturn(returned);
        }
        when(mockedUserRepository.findAll()).thenReturn(returned);
        ResponseEntity result = userServiceController.getBulkUsers(userType);

        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(expected.getStatusCode());
        assertThat(result.getBody()).isEqualTo(expected.getBody());
    }


}
