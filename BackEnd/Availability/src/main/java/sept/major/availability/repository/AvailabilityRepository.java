package sept.major.availability.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import sept.major.availability.entity.AvailabilityEntity;

import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;

@Table(name = "availability") //TODO most likely this class is not needed 
@Repository
public interface AvailabilityRepository extends JpaRepository<AvailabilityEntity, String> {
    @Query("select h from AvailabilityEntity h where h.startDateTime >= :startDateTime and h.endDateTime <= :endDateTime")
    List<AvailabilityEntity> findAllBetweenDates(LocalDateTime startDateTime, LocalDateTime endDateTime);
}
