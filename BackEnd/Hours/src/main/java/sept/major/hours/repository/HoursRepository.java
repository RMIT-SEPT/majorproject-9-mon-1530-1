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
    @Query("select h from HoursEntity h where (" +
            "not (:startDateTime < h.startDateTime and :endDateTime < h.startDateTime)" +
            "and not (:startDateTime > h.endDateTime and :endDateTime > h.endDateTime)" +
            ")")
    List<HoursEntity> findAllInRange(LocalDateTime startDateTime, LocalDateTime endDateTime);
    
    @Query("select h from HoursEntity h where (" +
            "(:workerUsername =  h.workerUsername)" +
            "and not (:startDateTime <= h.startDateTime and :endDateTime <= h.startDateTime)" +
            "and not (:startDateTime >= h.endDateTime and :endDateTime >= h.endDateTime)" +
            ")")
    List<HoursEntity> findConflictingHours(String workerUsername, LocalDateTime startDateTime, LocalDateTime endDateTime);
}

