package sept.major.hours.blackbox.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import sept.major.hours.blackbox.HoursBlackBoxTests;
import sept.major.hours.repository.HoursRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static sept.major.hours.HoursTestHelper.*;

public class RepositoryBlackBoxTests extends HoursBlackBoxTests {

    @Autowired
    public HoursRepository hoursRepository;

    @Test
    @DisplayName("findAllInRange boundary testing with only one record returned")
    void oneRecordFindAllInRange() {
        LocalDate currentDate = LocalDate.now();

        Map<String, String> existingEntity = randomEntityMap();
        existingEntity.put("startDateTime", currentDate.atTime(9, 30).toString());
        existingEntity.put("endDateTime", currentDate.atTime(17, 30).toString());
        hoursRepository.save(createHoursEntity(existingEntity));

        // Valid: Start before and end after
        assertThat(hoursRepository.findAllInRange(currentDate.atStartOfDay(), currentDate.atTime(23, 59))).isNotEmpty();

        // Valid: Start before and end within
        assertThat(hoursRepository.findAllInRange(currentDate.atTime(8, 30), currentDate.atTime(11, 30))).isNotEmpty();

        // Valid: Start same and end same
        assertThat(hoursRepository.findAllInRange(currentDate.atTime(9, 30), currentDate.atTime(17, 30))).isNotEmpty();

        // Valid: Start same and end within
        assertThat(hoursRepository.findAllInRange(currentDate.atTime(9, 30), currentDate.atTime(11, 30))).isNotEmpty();

        // Valid: Start within and end within
        assertThat(hoursRepository.findAllInRange(currentDate.atTime(11, 30), currentDate.atTime(13, 30))).isNotEmpty();

        // Valid: Start within and end after
        assertThat(hoursRepository.findAllInRange(currentDate.atTime(11, 30), currentDate.atTime(18, 30))).isNotEmpty();

        // Valid: Start at end and end after
        assertThat(hoursRepository.findAllInRange(currentDate.atTime(17, 30), currentDate.atTime(18, 30))).isNotEmpty();

        // Valid: Start and end at start
        assertThat(hoursRepository.findAllInRange(currentDate.atTime(9, 30), currentDate.atTime(9, 30))).isNotEmpty();

        // Valid: Start and end at end
        assertThat(hoursRepository.findAllInRange(currentDate.atTime(17, 30), currentDate.atTime(17, 30))).isNotEmpty();

        // No records found: Start after and end after
        assertThat(hoursRepository.findAllInRange(currentDate.atTime(18, 30), currentDate.atTime(20, 30))).isEmpty();
    }

    @Test
    @DisplayName("findAllInRange returning multiple records")
    void multipleRecordsFindAllInRange() {
        LocalDate currentDate = LocalDate.now();

        Map<String, String> firstRecord = randomEntityMap();
        firstRecord.put("startDateTime", currentDate.atTime(9, 30).toString());
        firstRecord.put("endDateTime", currentDate.atTime(11, 30).toString());
        hoursRepository.save(createHoursEntity(firstRecord));

        assertThat(hoursRepository.findAllInRange(currentDate.atTime(8, 30), currentDate.atTime(20, 30))).hasSize(1);

        Map<String, String> secondRecord = randomEntityMap();
        secondRecord.put("startDateTime", currentDate.atTime(13, 30).toString());
        secondRecord.put("endDateTime", currentDate.atTime(15, 30).toString());
        hoursRepository.save(createHoursEntity(secondRecord));

        assertThat(hoursRepository.findAllInRange(currentDate.atTime(8, 30), currentDate.atTime(20, 30))).hasSize(2);

        Map<String, String> thirdRecord = randomEntityMap();
        thirdRecord.put("startDateTime", currentDate.atTime(17, 30).toString());
        thirdRecord.put("endDateTime", currentDate.atTime(19, 30).toString());
        hoursRepository.save(createHoursEntity(thirdRecord));

        assertThat(hoursRepository.findAllInRange(currentDate.atTime(8, 30), currentDate.atTime(20, 30))).hasSize(3);

        assertThat(hoursRepository.findAllInRange(currentDate.atTime(7, 30), currentDate.atTime(8, 0))).isEmpty();
        assertThat(hoursRepository.findAllInRange(currentDate.atTime(12, 30), currentDate.atTime(13, 0))).isEmpty();
        assertThat(hoursRepository.findAllInRange(currentDate.atTime(16, 30), currentDate.atTime(17, 0))).isEmpty();
        assertThat(hoursRepository.findAllInRange(currentDate.atTime(20, 30), currentDate.atTime(21, 0))).isEmpty();

    }

    @Test
    @DisplayName("Boundary testing of findConflictingHours")
    void findConflictingHours() {
        LocalDate currentDate = LocalDate.now();
        String workerUsername = randomAlphanumericString(20);
        String creatorUsername = randomAlphanumericString(20);

        Map<String, String> existingEntity = randomEntityMap();
        existingEntity.put("startDateTime", currentDate.atTime(9, 30).toString());
        existingEntity.put("endDateTime", currentDate.atTime(17, 30).toString());
        existingEntity.put("workerUsername", workerUsername);
        existingEntity.put("creatorUsername", creatorUsername);
        hoursRepository.save(createHoursEntity(existingEntity));

        // Conflict: Start within existing
        conflictHoursWithUsernames(workerUsername, currentDate.atTime(11, 30), currentDate.atTime(19, 30));

        // Conflict: end within existing
        conflictHoursWithUsernames(workerUsername, currentDate.atTime(6, 30), currentDate.atTime(11, 30));

        // Conflict: Start and end within
        conflictHoursWithUsernames(workerUsername, currentDate.atTime(11, 30), currentDate.atTime(15, 30));

        // Conflict: Start before and end after
        conflictHoursWithUsernames(workerUsername, currentDate.atTime(8, 30), currentDate.atTime(18, 30));

        // Valid: Start and end before
        nonConflictHoursWithUsernames(workerUsername, currentDate.atTime(2, 30), currentDate.atTime(8, 30));

        // Valid: Start and end after
        nonConflictHoursWithUsernames(workerUsername, currentDate.atTime(18, 30), currentDate.atTime(23, 30));

        // Valid: End at start of existing
        nonConflictHoursWithUsernames(workerUsername, currentDate.atTime(2, 30), currentDate.atTime(9, 30));

        // Valid: Start at end of existing
        nonConflictHoursWithUsernames(workerUsername, currentDate.atTime(17, 30), currentDate.atTime(23, 30));
    }

    void conflictHoursWithUsernames(String workerUsername, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        String randomWorker = randomAlphanumericString(20);

        // Different worker and creator
        assertThat(hoursRepository.findConflictingHours(randomWorker, startDateTime, endDateTime).isEmpty());

        // worker is what was provided
        assertThat(hoursRepository.findConflictingHours(workerUsername, startDateTime, endDateTime)).isNotEmpty();
    }

    void nonConflictHoursWithUsernames(String workerUsername, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        String randomWorker = randomAlphanumericString(20);

        // Different worker and creator
        assertThat(hoursRepository.findConflictingHours(randomWorker, startDateTime, endDateTime).isEmpty());

        // worker is what was provided
        assertThat(hoursRepository.findConflictingHours(workerUsername, startDateTime, endDateTime)).isEmpty();
    }

}
