package ru.iot.edu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.iot.edu.model.Task;
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
}
