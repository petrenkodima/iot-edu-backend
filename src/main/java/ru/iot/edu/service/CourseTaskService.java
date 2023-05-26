package ru.iot.edu.service;

import org.springframework.stereotype.Service;
import ru.iot.edu.model.CourseTask;
import ru.iot.edu.model.Task;
import ru.iot.edu.repository.CourseTaskRepository;
import ru.iot.edu.repository.TaskRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CourseTaskService {
    private final CourseTaskRepository courseTaskRepository;
    private final TaskRepository taskRepository;

    public CourseTaskService(CourseTaskRepository courseTaskRepository, TaskRepository taskRepository) {
        this.courseTaskRepository = courseTaskRepository;
        this.taskRepository = taskRepository;
    }

    public List<Task> getAllByCourseId(long courseId) {
        return courseTaskRepository.findAllByCourseId(courseId)
                .stream()
                .map(CourseTask::getTask)
                .collect(Collectors.toList());
    }

    public List<Task> getAll() {
        return taskRepository.findAll();
    }

    public Optional<Task> getById(Long taskId) {
        return taskRepository.findById(taskId);
    }

    public Task create(Task task) {
        return taskRepository.save(task);
    }
}

