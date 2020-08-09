package sept.major.bookings.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sept.major.bookings.entity.BookingEntity;

import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;

@Table(name = "bookings", schema = "bookings")
@Repository
public interface BookingsRepository extends JpaRepository<BookingEntity, String> {
    //List<BookingEntity> findAllByStartDateLessThanEqualAndEndDateGreaterThanEqual(Date endDate, Date startDate);
    List<BookingEntity> findAllByStartTimeGreaterThanEqualAndEndTimeLessThanEqual(LocalDateTime startTime, LocalDateTime endTime);
}
