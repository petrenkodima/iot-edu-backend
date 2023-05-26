package ru.iot.edu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.iot.edu.model.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Student findByUserId(long userId);
}