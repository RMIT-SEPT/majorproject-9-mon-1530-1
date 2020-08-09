package sept.major.hours.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import sept.major.common.exception.RecordNotFoundException;
import sept.major.common.service.CrudService;
import sept.major.hours.entity.HoursEntity;
import sept.major.hours.repository.HoursRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class HoursService extends CrudService<HoursEntity, String> {

    HoursRepository hoursRepository;

    @Autowired
    public HoursService(HoursRepository hoursRepository) {
        this.hoursRepository = hoursRepository;
    }

    @Override
    protected JpaRepository<HoursEntity, String> getRepository() {
        return hoursRepository;
    }

    public List<HoursEntity> getAllHours(String workerUsername, String customerUsername) throws RecordNotFoundException {
        List<HoursEntity> allEntity = hoursRepository.findAll();
        return filterUsernames(allEntity, workerUsername, customerUsername);
    }

    public List<HoursEntity> getHoursInDate(LocalDate date, String workerUsername, String customerUsername) throws RecordNotFoundException {
        List<HoursEntity> hoursInDate = hoursRepository.findAllByDate(date);
        return filterUsernames(hoursInDate, workerUsername, customerUsername);
    }

    public List<HoursEntity> getHoursBetweenDates(LocalDate startDate, LocalDate endDate, String workerUsername, String customerUsername) throws RecordNotFoundException {
        List<HoursEntity> hoursBetweenDates = hoursRepository.findAllByDateGreaterThanEqualAndDateLessThanEqual(startDate, endDate);
        return filterUsernames(hoursBetweenDates, workerUsername, customerUsername);

    }

    private List<HoursEntity> filterUsernames(List<HoursEntity> hoursList, String workerUsername, String customerUsername) throws RecordNotFoundException {
        if (workerUsername != null && customerUsername != null) {
            hoursList.removeIf(hoursEntity -> (!workerUsername.equals(hoursEntity.getWorkerUsername())
                    || !customerUsername.equals(hoursEntity.getCustomerUsername())));
        } else if (workerUsername != null) {
            hoursList.removeIf(hoursEntity -> workerUsername.equals(hoursEntity.getWorkerUsername()));
        } else if (customerUsername != null) {
            hoursList.removeIf(hoursEntity -> customerUsername.equals(hoursEntity.getCustomerUsername()));
        }

        if(hoursList.size() == 0) {
            throw new RecordNotFoundException(String.format("No records within provided bounds were found"));
        }

        return hoursList;
    }
}
