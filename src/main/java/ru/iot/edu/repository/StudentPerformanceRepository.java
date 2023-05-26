package ru.iot.edu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.iot.edu.model.StudentTask;

import java.util.List;

@Repository
public interface StudentPerformanceRepository extends JpaRepository<StudentTask, Long> {
    List<StudentTask> findByStudentId(Long studentId);
}
