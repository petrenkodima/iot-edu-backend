package ru.iot.edu.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.iot.edu.model.auth.User;
import ru.iot.edu.repository.UserRepo;
import ru.iot.edu.service.TaskExecutorService;

import java.util.NoSuchElementException;

import static ru.iot.edu.config.WebSecurityConfig.API_PATH;

@RestController
@RequestMapping(API_PATH + "/executor")
@CrossOrigin(origins = "*")
public class TaskExecutorController {
    private final TaskExecutorService taskExecutorService;
    private final UserRepo userRepository;

    public TaskExecutorController(TaskExecutorService taskExecutorService, UserRepo userRepository) {
        this.taskExecutorService = taskExecutorService;
        this.userRepository = userRepository;
    }

    /**
     * POST-запрос на выполнение задания.
     * @param authentication объект, представляющий аутентифицированного пользователя
     * @param studentTaskId идентификатор студенческого задания
     * @return Строка, содержащая результат выполнения задания в формате JSON.
     */
    @PostMapping(produces = "application/json")
    @Operation(summary = "Выполнение задания", description = "Выполняет задание по идентификатору студенческого задания")
    @ApiResponse(responseCode = "200", description = "Результат выполнения задания в формате JSON")
    public String execute(Authentication authentication, @RequestParam long studentTaskId) {
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new NoSuchElementException("not found user"));
        return taskExecutorService.executeScript(studentTaskId, user).getBody();
    }
}
