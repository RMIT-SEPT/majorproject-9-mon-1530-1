package sept.major.users.controller;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sept.major.common.controller.ControllerHelper;
import sept.major.users.entity.UserEntity;
import sept.major.users.unit.service.UserService;

@Service
public class UserServiceControllerHelper extends ControllerHelper<UserEntity, String> {

    @Getter
    UserService service;

    @Autowired
    public UserServiceControllerHelper(UserService userService) {
        this.service = userService;
//        sept.major.common.entity.BrodeyTestClass brodeyTestClass = new sept.major.common.entity.BrodeyTestClass();
    }

}
