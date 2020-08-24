package sept.major.availability.controller;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sept.major.availability.entity.AvailabilityEntity;
import sept.major.availability.service.AvailabilityService;
import sept.major.common.controller.ControllerHelper;
import sept.major.common.service.CrudService;

@Service
public class AvailabilityControllerHelper extends ControllerHelper<AvailabilityEntity, String> {

    @Getter
    private AvailabilityService service;

    @Autowired
    public AvailabilityControllerHelper(AvailabilityService service) {
        this.service = service;
    }
}
