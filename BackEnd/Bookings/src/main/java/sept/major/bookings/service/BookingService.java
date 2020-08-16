package sept.major.bookings.service;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sept.major.common.exception.RecordNotFoundException;
import sept.major.common.service.CrudService;
import sept.major.bookings.entity.BookingEntity;
import sept.major.bookings.repository.BookingsRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@Service
public class BookingService extends CrudService<BookingEntity, String> {

    @Getter
    private BookingsRepository repository;

    @Autowired
    public BookingService(BookingsRepository bookingsRepository) {
        this.repository = bookingsRepository;
    }

    public List<BookingEntity> getBookingsInRange(LocalDateTime startTime) throws IllegalArgumentException, RecordNotFoundException {
        return getBookingsInRange(startTime, startTime.toLocalDate().atStartOfDay().plusDays(1).minusSeconds(1));
    }

    public List<BookingEntity> getBookingsInRange(LocalDateTime startTime, LocalDateTime endTime) throws IllegalArgumentException, RecordNotFoundException {
        List<BookingEntity> entityList;

        if (startTime == null) {
            throw new IllegalArgumentException("No start time was defined");
        } else {
            entityList = getRepository().findAllBetweenDates(startTime, endTime);
        }

        if (entityList == null || entityList.size() == 0) {
            throw new RecordNotFoundException(String.format("No records between %s and %s were found", startTime.toString(), endTime.toString()));
        }

        return  entityList;
    }
}
