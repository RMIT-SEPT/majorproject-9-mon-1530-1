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
    List<HoursEntity> findAllByStartDateTimeBetween(LocalDateTime startDateTime, LocalDateTime endDateTime);

    @Query("select h from HoursEntity h where (" +
            "(:workerUsername =  h.workerUsername)" +
            "and (h.startDateTime >= :startDateTime and h.startDateTime <= :endDateTime)" +
            "and (h.endDateTime >= :startDateTime and h.endDateTime <= :endDateTime)" +
            ")")
    List<HoursEntity> findConflictingHours(String workerUsername, LocalDateTime startDateTime, LocalDateTime endDateTime);

    //    @Query("select h from HoursEntity h where not (" +
//            "(:startDateTime <= h.startDateTime and :endDateTime <= h.startDateTime) or" +
//            "(:startDateTime > h.endDateTime))")
}

