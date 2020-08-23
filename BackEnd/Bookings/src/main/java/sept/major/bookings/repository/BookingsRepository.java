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
    @Query("select b from BookingEntity b where b.startDateTime >= :startDateTime and b.endDateTime <= :endDateTime")
    List<BookingEntity> findAllBetweenDates(LocalDateTime startDateTime, LocalDateTime endDateTime);
}
