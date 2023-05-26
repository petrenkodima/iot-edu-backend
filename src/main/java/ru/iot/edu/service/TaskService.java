package ru.iot.edu.service;

import org.springframework.stereotype.Service;
import ru.iot.edu.model.CourseTask;
import ru.iot.edu.model.Task;
import ru.iot.edu.repository.CourseTaskRepository;
import ru.iot.edu.repository.TaskRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public void deleteTask(Long taskId) {
        taskRepository.deleteById(taskId);
    }
}
