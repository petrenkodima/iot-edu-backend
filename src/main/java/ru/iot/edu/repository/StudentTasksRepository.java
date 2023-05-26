package ru.iot.edu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.iot.edu.model.StudentTask;

import java.util.List;
import java.util.Optional;


@Repository
public interface StudentTasksRepository extends JpaRepository<StudentTask, Long> {
    List<StudentTask> findByTaskId(long taskId);

    Optional<StudentTask> findTopByTaskIdAndStudentUserIdOrderByCreatedAtDesc(long taskId, long studentUserId);

    boolean deleteByStudentIdAndId(Long studentId, Long id);
}



