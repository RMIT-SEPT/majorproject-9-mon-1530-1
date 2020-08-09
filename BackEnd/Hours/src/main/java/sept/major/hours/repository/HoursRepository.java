package sept.major.hours.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sept.major.hours.entity.HoursEntity;

import javax.persistence.Table;
import java.time.LocalDate;
import java.util.List;

@Table(name = "hours")
@Repository
public interface HoursRepository extends JpaRepository<HoursEntity, String> {
    List<HoursEntity> findAllByDate(LocalDate date);
    List<HoursEntity> findAllByDateGreaterThanEqualAndDateLessThanEqual(LocalDate startDate, LocalDate endDate);
}
