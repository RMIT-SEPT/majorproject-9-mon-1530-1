package sept.major.users.service;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import sept.major.common.exception.RecordNotFoundException;
import sept.major.common.service.CrudService;
import sept.major.users.entity.UserEntity;
import sept.major.users.repository.UsersRepository;

import java.util.*;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static sept.major.users.security.SecurityConstants.*;

@Service
public class UserService extends CrudService<UserEntity, String> {

    @Getter
    private UsersRepository repository;

    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

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

    public static String hashPassword(String password) {
        if (isNotEmpty(password)) {
            return passwordEncoder.encode(password);
        } else {
            return null;
        }

    }

    public static boolean doPasswordsMatch(String hashedPassword, String plainTextPassword) {
        return passwordEncoder.matches(plainTextPassword, hashedPassword);
    }

    public void updatePassword(String username, String oldPassword, String newPassword) {
        Optional<UserEntity> optionalUser = repository.findByUsernameAndPassword(username, hashPassword(oldPassword));

        try {
            UserEntity user = optionalUser.get();
            user.setPassword(hashPassword(newPassword));
            repository.save(user);
        } catch (NoSuchElementException e) {
            System.out.println("not matched");
            throw new RuntimeException("Error, User not found", e);
        }
    }

    public Optional<User> findByToken(String username, String token) {
        Optional<UserEntity> userEntityOptional = repository.findByUsernameAndToken(username, token);
        if (userEntityOptional.isPresent()) {
            UserEntity userEntity = userEntityOptional.get();

            Collection<GrantedAuthority> grantedAuthorities = Collections.EMPTY_LIST;

            if (userEntity.getUserType().equalsIgnoreCase(USER_CODE)) {
                grantedAuthorities = AuthorityUtils.createAuthorityList(USER_CODE);
            }

            if (userEntity.getUserType().equalsIgnoreCase(EMPLOYEE_CODE)) {
                grantedAuthorities = AuthorityUtils.createAuthorityList(USER_CODE, EMPLOYEE_CODE);
            }

            if (userEntity.getUserType().equalsIgnoreCase(ADMIN_CODE)) {
                grantedAuthorities = AuthorityUtils.createAuthorityList(USER_CODE, EMPLOYEE_CODE, ADMIN_CODE);
            }


            User user = new User(userEntity.getUsername(), userEntity.getPassword(), true, true, true, true,
                    grantedAuthorities);
            return Optional.of(user);
        }
        return Optional.empty();
    }

    public boolean comparePassword(String username, String plainTextPassword) {
        Optional<UserEntity> optionalUser = repository.findByUsername(username);
        return (optionalUser.isPresent() && doPasswordsMatch(optionalUser.get().getPassword(), plainTextPassword));
    }

    public String login(String username, String plainTextPassword) {
        Optional<UserEntity> userOptional = repository.findByUsername(username);

        String token = null;
        if (comparePassword(username, plainTextPassword)) {
            token = UUID.randomUUID().toString();
            UserEntity user = userOptional.get();
            user.setToken(token);
            repository.save(user);
        }
        return token;
    }
}
