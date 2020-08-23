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
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookingService extends CrudService<BookingEntity, Integer> {

    @Getter
    private BookingsRepository repository;

    @Autowired
    public BookingService(BookingsRepository bookingsRepository) {
        this.repository = bookingsRepository;
    }

    public BookingEntity getSpecificBooking(Integer bookingId) throws RecordNotFoundException {
        Optional<BookingEntity> entity;
        entity = getRepository().findById(bookingId);
        if (entity.isPresent()) {
            return entity.get();
        }
        throw new RecordNotFoundException(String.format("No booking entity for id %d", bookingId));
    }

    public List<BookingEntity> getBookingsInRange(LocalDateTime startTime, String workerUsername, String customerUsername) throws IllegalArgumentException, RecordNotFoundException {
        if (startTime == null) {
            throw new IllegalArgumentException("No start time was defined");
        }
        return getBookingsInRange(startTime, startTime.toLocalDate().atStartOfDay().plusDays(1).minusSeconds(1), workerUsername, customerUsername);
    }

    public List<BookingEntity> getBookingsInRange(LocalDateTime startTime, LocalDateTime endTime, String workerUsername, String customerUsername) throws IllegalArgumentException, RecordNotFoundException {
        List<BookingEntity> entityList;

        if (startTime == null) {
            throw new IllegalArgumentException("No start time was defined");
        } else {
            entityList = getRepository().findAllBetweenDates(startTime, endTime);
        }

        if (entityList == null || entityList.size() == 0) {
            throw new RecordNotFoundException(String.format("No records between %s and %s were found", startTime.toString(), endTime.toString()));
        }

        if ((workerUsername != null) || (customerUsername != null))
        {
            return filterUsernames(entityList, workerUsername, customerUsername);
        }

        return entityList;
    }

    public List<BookingEntity> getBookingsFor (String workerUsername, String customerUsername) throws RecordNotFoundException {
        return filterUsernames(getRepository().findAll(), workerUsername, customerUsername);
    }

    private List<BookingEntity> filterUsernames(List<BookingEntity> bookingList, String[] workerUsernames, String[] customerUsernames) throws RecordNotFoundException {
        List<BookingEntity> result;

        if ((workerUsernames.length > 0) && (customerUsernames.length > 0)) {
            result = bookingList.stream()
                    .filter(
                            bookingEntity ->
                                    Arrays.stream(workerUsernames).anyMatch(workerUsername -> workerUsername.equals(bookingEntity.getWorkerUsername()))
                                    &&
                                    Arrays.stream(customerUsernames).anyMatch(customerUsername -> customerUsername.equals(bookingEntity.getCustomerUsername()))
                    ).collect(Collectors.toList());
        } else if (workerUsernames.length > 0) {
            result = bookingList.stream()
                    .filter(
                            bookingEntity -> Arrays.stream(workerUsernames).anyMatch(workerUsername -> workerUsername.equals(bookingEntity.getWorkerUsername()))
                    ).collect(Collectors.toList());
        } else if (customerUsernames.length > 0) {
            result = bookingList.stream()
                    .filter(
                            bookingEntity -> Arrays.stream(customerUsernames).anyMatch(customerUsername -> customerUsername.equals(bookingEntity.getCustomerUsername()))
                    ).collect(Collectors.toList());
        } else {
            result = bookingList;
        }

        return result;
    }

    private List<BookingEntity> filterUsernames(List<BookingEntity> bookingList, String workerUsername, String customerUsername) throws RecordNotFoundException {
        List<BookingEntity> result;

        if (workerUsername != null && customerUsername != null) {
            result = bookingList.stream()
                    .filter(bookingEntity -> workerUsername.equals(bookingEntity.getWorkerUsername()) && customerUsername.equals(bookingEntity.getCustomerUsername()))
                    .collect(Collectors.toList());
        } else if (workerUsername != null) {
            result = bookingList.stream()
                    .filter(bookingEntity -> workerUsername.equals(bookingEntity.getWorkerUsername()))
                    .collect(Collectors.toList());
        } else if (customerUsername != null) {
            result = bookingList.stream()
                    .filter(bookingEntity -> customerUsername.equals(bookingEntity.getCustomerUsername()))
                    .collect(Collectors.toList());
        } else {
            result = bookingList;
        }

        if (result.size() == 0) {
            throw new RecordNotFoundException(String.format("No records within provided bounds were found"));
        }

        return result;
    }
}
