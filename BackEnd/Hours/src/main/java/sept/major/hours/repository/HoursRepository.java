package sept.major.hours.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sept.major.hours.entity.HoursEntity;

import javax.persistence.Table;

@Table(name = "hours", schema = "hours")
@Repository
public interface HoursRepository extends JpaRepository<HoursEntity, String> {
}
