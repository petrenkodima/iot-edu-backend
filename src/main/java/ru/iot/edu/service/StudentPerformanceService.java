package ru.iot.edu.service;

import org.springframework.stereotype.Service;
import ru.iot.edu.model.StudentTask;
import ru.iot.edu.repository.StudentPerformanceRepository;

import java.util.List;

@Service
public class StudentPerformanceService {

    private final StudentPerformanceRepository studentPerformanceRepository;

    public StudentPerformanceService(StudentPerformanceRepository studentPerformanceRepository) {
        this.studentPerformanceRepository = studentPerformanceRepository;
    }

    /**
     * Получает список задач и оценок студента.
     *
     * @param studentId идентификатор студента
     * @return список задач и оценок студента
     */
    public List<StudentTask> getStudentPerformance(Long studentId) {
        return studentPerformanceRepository.findByStudentId(studentId);
    }

    /**
     * Вычисляет среднюю оценку по задачам студента.
     *
     * @param studentTasks список задач и оценок студента
     * @return средняя оценка или null, если список пуст
     */
    public Double calculateAverageGrade(List<StudentTask> studentTasks) {
        if (studentTasks.isEmpty()) {
            return null;
        }

        int sum = 0;
        for (StudentTask studentTask : studentTasks) {
            sum += studentTask.getGrade();
        }

        return (double) sum / studentTasks.size();
    }
}
