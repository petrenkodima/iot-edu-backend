package ru.iot.edu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.iot.edu.model.Stand;

@Repository
public interface StandRepository extends JpaRepository<Stand, Long> {
}