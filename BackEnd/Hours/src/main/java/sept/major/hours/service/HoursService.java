package sept.major.hours.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import sept.major.common.service.CrudService;
import sept.major.hours.entity.HoursEntity;

@Service
public class HoursService extends CrudService<HoursEntity, String> {
    @Override
    protected JpaRepository<HoursEntity, String> getRepository() {
        return null;
    }
}
