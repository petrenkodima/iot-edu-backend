package ru.iot.edu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.iot.edu.model.Group;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
}


