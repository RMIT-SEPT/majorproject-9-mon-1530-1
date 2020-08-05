package sept.major.hours.controller;

import org.springframework.stereotype.Service;
import sept.major.common.controller.ControllerHelper;
import sept.major.common.service.CrudService;
import sept.major.hours.entity.HoursEntity;

@Service
public class HoursControllerHelper extends ControllerHelper<HoursEntity, String> {

    @Override
    public CrudService<HoursEntity, String> getService() {
        return null;
    }
}
