package ru.iot.edu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.iot.edu.model.CourseTask;

import java.util.List;

@Repository
public interface CourseTaskRepository extends JpaRepository<CourseTask, Long> {
    List<CourseTask> findAllByCourseId(long courseId);
}


