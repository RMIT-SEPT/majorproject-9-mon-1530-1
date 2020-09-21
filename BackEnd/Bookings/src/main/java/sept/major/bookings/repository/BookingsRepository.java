package sept.major.bookings.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sept.major.bookings.entity.BookingEntity;

import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;

@Table(name = "bookings")
@Repository
public interface BookingsRepository extends JpaRepository<BookingEntity, Integer> {
    @Query("select b from BookingEntity b where (" +
            "not (:startDateTime < b.startDateTime and :endDateTime < b.startDateTime)" +
            "and not (:startDateTime > b.endDateTime and :endDateTime > b.endDateTime)" +
            ")")
    List<BookingEntity> findAllInRange(LocalDateTime startDateTime, LocalDateTime endDateTime);

//    @Query("select b from BookingEntity b where (" +
//            "(:workerUsername =  b.workerUsername)" +
//            "and (b.startDateTime >= :startDateTime and b.startDateTime <= :endDateTime)" +
//            "and (b.endDateTime >= :startDateTime and b.endDateTime <= :endDateTime)" +
//            ")")

    @Query("select b from BookingEntity b where (" +
            "((:workerUsername =  b.workerUsername) or (:customerUsername = b.customerUsername))" +
            "and not (:startDateTime <= b.startDateTime and :endDateTime <= b.startDateTime)" +
            "and not (:startDateTime >= b.endDateTime and :endDateTime >= b.endDateTime)" +
            ")")
    List<BookingEntity> findConflictingHours(String workerUsername, String customerUsername, LocalDateTime startDateTime, LocalDateTime endDateTime);
}
