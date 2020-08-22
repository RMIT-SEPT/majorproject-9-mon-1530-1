package sept.major.hours.controller;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sept.major.common.controller.ControllerHelper;
import sept.major.hours.entity.HoursEntity;
import sept.major.hours.service.HoursService;

@Service
public class HoursControllerHelper extends ControllerHelper<HoursEntity, String> {

    @Getter
    private HoursService service;

    @Autowired
    public HoursControllerHelper(HoursService service) {
        this.service = service;
    }
}
