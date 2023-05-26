package ru.iot.edu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.iot.edu.model.TaskGroup;

@Repository
public interface TaskGroupRepository extends JpaRepository<TaskGroup, Long> {
}


