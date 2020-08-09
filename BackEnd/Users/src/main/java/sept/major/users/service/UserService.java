package sept.major.users.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.Getter;
import sept.major.common.exception.RecordNotFoundException;
import sept.major.common.service.CrudService;
import sept.major.users.entity.UserEntity;
import sept.major.users.repository.UsersRepository;

@Service
public class UserService extends CrudService<UserEntity, String> {

    @Getter
    private UsersRepository repository;

    @Autowired
    public UserService(UsersRepository usersRepository) {
        this.repository = usersRepository;
    }

    public List<UserEntity> readBulkUsers(String userType) throws RecordNotFoundException {
        List<UserEntity> entityList;
        if (userType == null) {
            entityList = getRepository().findAll();
        } else {
            entityList = getRepository().findAllByUserType(userType);
        }

        if (entityList == null || entityList.size() == 0) {
            String message;
            if (userType == null) {
                message = "No record was found";
            } else {
                message = String.format("No record with a userType of %s was found", userType);
            }
            throw new RecordNotFoundException(message);
        } else {
            return entityList;
        }
    }


    public void updatePassword(String username, Map<String, Object> requestBody) {

    }

	public boolean comparePassword(String username, String password) {
		boolean matchFound;
		
		Optional<UserEntity> optionalUser = repository.findByUsernameAndPassword(username, password); 
		if ( optionalUser.isPresent())
			matchFound = true;
		else
			matchFound = false;

		return matchFound;
    }
}
