package sept.major.users.service;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sept.major.common.exception.RecordNotFoundException;
import sept.major.common.service.CrudService;
import sept.major.users.entity.UserEntity;
import sept.major.users.repository.UsersRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

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


    public void updatePassword(String username, String oldPassword,String newPassword) {
		Optional<UserEntity> optionalUser = repository.findByUsernameAndPassword(username,oldPassword); 
		
		try {
			UserEntity user = optionalUser.get();
			user.setPassword(newPassword);
			repository.save(user);
		} catch (NoSuchElementException e) {
			System.out.println("not matched");
			throw new RuntimeException("Error, User not found", e) ;
		}
    }

	public boolean comparePassword(String username, String plainTextPassword) {
		boolean matchFound;
		
//		Optional<UserEntity> optionalUser = repository.findByUsernameAndPassword(username,
//				hashedPassword);
		
		Optional<UserEntity> optionalUser = repository.findByUsername(username); 

		if ( optionalUser.isPresent())
			return optionalUser.get().checkPassword(plainTextPassword);
		else
			matchFound = false;

		return matchFound;
    }
}
