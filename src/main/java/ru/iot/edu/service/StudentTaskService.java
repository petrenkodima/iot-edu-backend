package ru.iot.edu.service;

import org.springframework.stereotype.Service;
import ru.iot.edu.repository.StudentTasksRepository;

@Service
public class StudentTaskService {
    private final StudentTasksRepository studentTasksRepository;

    public StudentTaskService(StudentTasksRepository studentTaskRepository) {
        this.studentTasksRepository = studentTaskRepository;
    }

    public boolean deleteStudentTask(Long studentId, Long taskId) {
        return studentTasksRepository.deleteByStudentIdAndId(studentId, taskId);
    }
}
