package ru.iot.edu.service;

import org.springframework.stereotype.Service;
import ru.iot.edu.model.Student;
import ru.iot.edu.repository.StudentRepository;

@Service
public class StudentService {
    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student findByUserId(long id) {
        return studentRepository.findByUserId(id);
    }
}
