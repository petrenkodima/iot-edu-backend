package ru.iot.edu.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.iot.edu.core.dto.CreateTaskDto;
import ru.iot.edu.core.dto.TaskDto;
import ru.iot.edu.core.dto.mapper.IotEduMapper;
import ru.iot.edu.model.Course;
import ru.iot.edu.model.CourseTask;
import ru.iot.edu.model.Stand;
import ru.iot.edu.model.Task;
import ru.iot.edu.repository.CourseRepository;
import ru.iot.edu.repository.CourseTaskRepository;
import ru.iot.edu.repository.StandRepository;
import ru.iot.edu.service.CourseTaskService;
import ru.iot.edu.service.TaskService;

import java.util.List;
import java.util.NoSuchElementException;

import static ru.iot.edu.config.WebSecurityConfig.API_PATH;


@RestController
@RequestMapping(value = API_PATH + "/tasks")
@CrossOrigin(origins = "*")
public class TaskController {
    private final TaskService taskService;
    private final CourseTaskService courseTaskService;
    private final StandRepository standRepository;
    private final CourseRepository courseRepository;
    private final CourseTaskRepository courseTaskRepository;
    private final IotEduMapper mapper;

    public TaskController(TaskService taskService, CourseTaskService courseTaskService, StandRepository standRepository, CourseRepository courseRepository, CourseTaskRepository courseTaskRepository, IotEduMapper mapper) {
        this.taskService = taskService;
        this.courseTaskService = courseTaskService;
        this.standRepository = standRepository;
        this.courseRepository = courseRepository;
        this.courseTaskRepository = courseTaskRepository;
        this.mapper = mapper;
    }

    /**
     * Получение списка заданий.
     *
     * @param courseId Идентификатор курса.
     * @return Список DTO заданий.
     */
    @GetMapping
    @Operation(summary = "Получение списка заданий", description = "Метод для получения списка заданий. Если указан идентификатор курса, то возвращаются только задания этого курса.")
    @ApiResponse(responseCode = "200", description = "Список DTO заданий.")
    public List<TaskDto> getAll(@RequestParam(required = false) Long courseId) {
        if (courseId != null) {
            return mapper.mapAsList(courseTaskService.getAllByCourseId(courseId), TaskDto.class);
        }
        return mapper.mapAsList(courseTaskService.getAll(), TaskDto.class);
    }

    /**
     * Получение задания по Id.
     *
     * @param taskId Идентификатор лабораторной.
     * @return Список DTO заданий.
     */
    @GetMapping("/{taskId}")
    @Operation(summary = "Получение задания по Id", description = "Метод для получения задания.")
    @ApiResponse(responseCode = "200", description = "DTO задания.")
    public TaskDto getById(@PathVariable Long taskId) {
        Task task = courseTaskService.getById(taskId).orElseThrow(() ->
                new NoSuchElementException("not found task by id"));
        return mapper.map(task, TaskDto.class);
    }


    /**
     * Метод для создания задания.
     *
     * @param taskForCreate Объект, содержащий информацию о задании.
     * @return Результат создания задания.
     */
    @PostMapping(produces = "application/json")
    @Operation(summary = "Создание задания", description = "Метод для создания задания")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Задание успешно создано"),
            @ApiResponse(responseCode = "400", description = "Переданы некорректные данные"),
            @ApiResponse(responseCode = "404", description = "Не найдены стенды или курсы")
    })
    public TaskDto create(@RequestBody CreateTaskDto taskForCreate) {
        Stand stand = standRepository.findById(taskForCreate.getStandId())
                .orElseThrow(() -> new NoSuchElementException("no found stands"));

        Course course = courseRepository.findById(taskForCreate.getCourseId())
                .orElseThrow(() -> new NoSuchElementException("no found course"));
        Task task = Task.builder()
                .duration(taskForCreate.getDuration())
                .name(taskForCreate.getName())
                .description(taskForCreate.getDescription())
                .stand(stand)
                .build();
        Task saveTask = courseTaskService.create(task);
        courseTaskRepository.save(
                CourseTask.builder()
                        .course(course)
                        .task(task)
                        .build()
        );
        return mapper.map(saveTask, TaskDto.class);
    }

    /**
     * Метод удаления задания.
     *
     * @param taskId идентификатор задания, которое нужно удалить
     * @return ResponseEntity со статусом 204 в случае успешного удаления задания и статусом 404, если задание не найдено
     */
    @PreAuthorize("hasAnyRole('ROLE_TEACHER')")
    @DeleteMapping("/{taskId}")
    @Operation(summary = "Удалить задание", description = "Удалить задание по его идентификатору")
    @ApiResponse(responseCode = "204", description = "Задание успешно удалено")
    @ApiResponse(responseCode = "404", description = "Задание не найдено")
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId) {
        taskService.deleteTask(taskId);
        return ResponseEntity.noContent().build();
    }
}
