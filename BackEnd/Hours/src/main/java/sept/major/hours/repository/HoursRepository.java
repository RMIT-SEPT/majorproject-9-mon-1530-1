package sept.major.hours.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sept.major.hours.entity.HoursEntity;

import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;

@Table(name = "hours")
@Repository
public interface HoursRepository extends JpaRepository<HoursEntity, Integer> {
    @Query("select h from HoursEntity h where h.startDateTime >= :startDateTime and h.endDateTime <= :endDateTime")
    List<HoursEntity> findAllBetweenDates(LocalDateTime startDateTime, LocalDateTime endDateTime);
}
