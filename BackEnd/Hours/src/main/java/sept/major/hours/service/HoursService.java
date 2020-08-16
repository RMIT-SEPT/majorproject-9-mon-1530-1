package sept.major.hours.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import sept.major.common.exception.RecordNotFoundException;
import sept.major.common.exception.ResponseErrorException;
import sept.major.common.response.ResponseError;
import sept.major.common.service.CrudService;
import sept.major.hours.entity.HoursEntity;
import sept.major.hours.repository.HoursRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

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

    public List<HoursEntity> getHoursInDate(LocalDate date, String workerUsername, String customerUsername) throws RecordNotFoundException, ResponseErrorException {
        if(date == null) {
            throw new ResponseErrorException(new HashSet<>(Arrays.asList(new ResponseError("date", "Must be provided"))));
        }
        List<HoursEntity> hoursInDate = hoursRepository.findAllBetweenDates(date.atStartOfDay(), date.plusDays(1).atStartOfDay());
        return filterUsernames(hoursInDate, workerUsername, customerUsername);
    }

    public List<HoursEntity> getHoursBetweenDates(LocalDateTime startDate, LocalDateTime endDate, String workerUsername, String customerUsername) throws RecordNotFoundException {
        List<HoursEntity> hoursBetweenDates = hoursRepository.findAllBetweenDates(startDate, endDate);
        return filterUsernames(hoursBetweenDates, workerUsername, customerUsername);

    }

    private List<HoursEntity> filterUsernames(List<HoursEntity> hoursList, String workerUsername, String customerUsername) throws RecordNotFoundException {
        List<HoursEntity> result;

        if (workerUsername != null && customerUsername != null) {
            result = hoursList.stream()
                    .filter(hoursEntity -> workerUsername.equals(hoursEntity.getWorkerUsername()) && customerUsername.equals(hoursEntity.getCustomerUsername()))
                    .collect(Collectors.toList());
        } else if (workerUsername != null) {
            result = hoursList.stream()
                    .filter(hoursEntity -> workerUsername.equals(hoursEntity.getWorkerUsername()))
                    .collect(Collectors.toList());
        } else if (customerUsername != null) {
            result = hoursList.stream()
                    .filter(hoursEntity -> customerUsername.equals(hoursEntity.getCustomerUsername()))
                    .collect(Collectors.toList());
        } else {
            result = hoursList;
        }

        if (result.size() == 0) {
            throw new RecordNotFoundException(String.format("No records within provided bounds were found"));
        }

        return result;
    }
}
