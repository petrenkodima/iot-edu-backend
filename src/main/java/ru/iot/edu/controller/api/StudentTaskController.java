package ru.iot.edu.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.iot.edu.core.dto.*;
import ru.iot.edu.core.dto.mapper.IotEduMapper;
import ru.iot.edu.model.StudentTask;
import ru.iot.edu.model.StudentTaskStatus;
import ru.iot.edu.model.Task;
import ru.iot.edu.model.auth.User;
import ru.iot.edu.repository.StudentTasksRepository;
import ru.iot.edu.repository.TaskRepository;
import ru.iot.edu.repository.UserRepo;
import ru.iot.edu.service.StudentService;
import ru.iot.edu.service.StudentTaskService;

import javax.validation.Valid;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.UUID.randomUUID;
import static java.util.stream.Collectors.groupingBy;
import static ru.iot.edu.config.WebSecurityConfig.API_PATH;
import static ru.iot.edu.util.FileUtil.getUploadedFileContent;


@RestController
@RequestMapping(value = API_PATH + "/student_task")
@CrossOrigin(origins = "*")
public class StudentTaskController {
    //todo to service
    private final StudentTasksRepository studentTasksRepository;
    private final StudentService studentService;

    private final StudentTaskService studentTaskService;
    private final TaskRepository taskRepository;
    private final UserRepo userRepo;
    private final IotEduMapper mapper;

    public StudentTaskController(StudentTasksRepository studentTasksRepository, StudentService studentService, StudentTaskService studentTaskService, TaskRepository taskRepository, UserRepo userRepo, IotEduMapper mapper) {
        this.studentTasksRepository = studentTasksRepository;
        this.studentService = studentService;
        this.studentTaskService = studentTaskService;
        this.taskRepository = taskRepository;
        this.userRepo = userRepo;
        this.mapper = mapper;
    }


    /**
     * Получение всех заданий студента, либо только заданий для конкретной лабораторной работы
     *
     * @param labId          id лабораторной работы
     * @param forOnlyMe      флаг, указывающий на то, что нужно получить только задания текущего пользователя
     * @param authentication аутентификационные данные пользователя
     * @return список заданий студента
     */
    @GetMapping
    @Operation(summary = "Получение всех заданий студента, либо только заданий для конкретной лабораторной работы",
            description = "Получение списка всех заданий студента или только заданий для конкретной лабораторной работы, опционально для текущего пользователя",
            parameters = {
                    @Parameter(name = "labId", description = "ID лабораторной работы", in = ParameterIn.QUERY),
                    @Parameter(name = "forOnlyMe", description = "Флаг, указывающий на то, что нужно получить только задания текущего пользователя", in = ParameterIn.QUERY),
                    @Parameter(name = "authentication", description = "Данные аутентификации пользователя", in = ParameterIn.HEADER)
            })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный запрос"),
            @ApiResponse(responseCode = "404", description = "Не найдено задание или пользователь")
    })
    public List<StudentTaskDto> getAll(
            @RequestParam(required = false) Long labId,
            @RequestParam(defaultValue = "false") boolean forOnlyMe,
            Authentication authentication
    ) {
        if (labId != null) {
            List<StudentTask> tasks = studentTasksRepository.findByTaskId(labId);
            if (forOnlyMe) {
                return mapper.mapAsList(tasks, StudentTaskDto.class);
            }
            User user = userRepo.findByUsername(authentication.getName())
                    .orElseThrow(() -> new NoSuchElementException("not found user"));
            return mapper.mapAsList(
                    tasks.stream()
                            .filter(s -> s.getStudent().getUser().getId().equals(user.getId()))
                            .collect(Collectors.toList()),
                    StudentTaskDto.class);
        }
        return mapper.mapAsList(studentTasksRepository.findAll(), StudentTaskDto.class);
    }

    /**
     * Получить задания всех студентов для лабораторной работы
     *
     * @param taskId идентификатор лабораторной работы
     * @return список заданий студентов для лабораторной работы
     * @throws IllegalStateException если не найдена лабораторная работа с указанным идентификатором
     */
    @PreAuthorize("hasAnyRole('ROLE_TEACHER')")
    @GetMapping("/teacher/{taskId}")
    @Operation(summary = "Получить задания всех студентов для лабораторной работы", description = "Получает задания всех студентов для указанной лабораторной работы")
    @ApiResponse(responseCode = "200", description = "Список заданий студентов для лабораторной работы")
    public ResponseEntity<TeacherLabTasksDto> getAllStudentsTasksForLab(@PathVariable Long taskId) {

        Task foundTask = taskRepository.findById(taskId).orElseThrow(() -> new IllegalStateException("No task found with the specified task ID"));

        Map<String, List<StudentTask>> grouping = studentTasksRepository.findAll().stream().collect(groupingBy(st -> st.getStudent().getId() + "_" + st.getTask().getId()));
        List<StudentTask> findStudentsTasks = new ArrayList<>();

        grouping.forEach((keyGroup, studentTasks) -> {
            if (studentTasks.stream().anyMatch(x -> x.getStatus() != null && x.getStatus().equals(StudentTaskStatus.AFFIXED))) {
                return;
            }
            Optional<StudentTask> lastStudentTask = studentTasks.stream()
                    .filter(x -> x.getTask().getId() == taskId)
                    .max(Comparator.comparing(StudentTask::getCreatedAt));
            lastStudentTask.ifPresent(findStudentsTasks::add);
        });

        List<TeacherLabTaskDto> studentsTask = new ArrayList<>();

        for (StudentTask task : findStudentsTasks) {
            User user = task.getStudent().getUser();
            studentsTask.add(
                    TeacherLabTaskDto.builder()
                            .id(task.getId())
                            .studentName(String.format("%s %s", user.getLastName(), user.getFirstName()))
                            .grade(task.getGrade())
                            .filePath(task.getFilePath())
                            .createdAt(task.getCreatedAt())
                            .teacherComment("empty")
                            .build()
            );
        }

        return ResponseEntity.ok(
                TeacherLabTasksDto.builder()
                        .task(foundTask)
                        .tasks(studentsTask)
                        .build()
        );
    }



    /**
     * Получение последнего задания текущего пользователя, либо задания для конкретной лабораторной работы
     *
     * @param labId          id лабораторной работы
     * @param authentication аутентификационные данные пользователя
     * @return ResponseEntity с DTO объектом задания студента или ответом not found
     */
    @Operation(summary = "Получение последнего задания текущего пользователя или задания для конкретной лабораторной работы")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный запрос, объект задания студента", content = @Content(schema = @Schema(implementation = StudentTaskDto.class))),
            @ApiResponse(responseCode = "404", description = "Задание не найдено")})
    @GetMapping("/last_my_task")
    @PreAuthorize("hasRole('ROLE_STUDENT')")
    public ResponseEntity<StudentTaskDto> getLast(@RequestParam(required = false) Long labId, Authentication authentication) {
        User user = userRepo.findByUsername(authentication.getName())
                .orElseThrow(() -> new NoSuchElementException("not found user"));
        Optional<StudentTask> findStudentTask = studentTasksRepository
                .findTopByTaskIdAndStudentUserIdOrderByCreatedAtDesc(labId, user.getId());
        return findStudentTask.map(studentTask -> ResponseEntity.ok(mapper.map(
                                studentTask,
                                StudentTaskDto.class)
                        )
                )
                .orElseGet(() -> ResponseEntity.notFound().build());

    }

    /**
     * Проверка наличия задания текущего пользователя для конкретной лабораторной работы
     *
     * @param labId          id лабораторной работы
     * @param authentication аутентификационные данные пользователя
     * @return true - если задание найдено, false - в противном случае
     */
    @Operation(summary = "Проверка наличия задания текущего пользователя для конкретной лабораторной работы")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Задание найдено"),
            @ApiResponse(responseCode = "204", description = "Задание не найдено")})
    @GetMapping("/exist_my_task")
    @PreAuthorize("hasRole('ROLE_STUDENT')")
    public boolean existMyTask(
            @RequestParam(required = false) Long labId, Authentication authentication
    ) {
        User user = userRepo.findByUsername(authentication.getName())
                .orElseThrow(() -> new NoSuchElementException("not found user"));
        Optional<StudentTask> findStudentTask = studentTasksRepository
                .findTopByTaskIdAndStudentUserIdOrderByCreatedAtDesc(labId, user.getId());
        return findStudentTask.isPresent();

    }

    /**
     * Метод для создания задания студентом.
     *
     * @param studentTaskDto ДТО, содержащее данные о создаваемом задании.
     * @param authentication Информация об аутентификации пользователя.
     * @return Сущность StudentTask, созданная студентом.
     */
    @PostMapping
    @Operation(summary = "Создание задания студентом", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное создание задания"),
            @ApiResponse(responseCode = "400", description = "Некорректный запрос"),
            @ApiResponse(responseCode = "401", description = "Не авторизован"),
            @ApiResponse(responseCode = "403", description = "Запрещено создавать задание не от своего имени"),
            @ApiResponse(responseCode = "404", description = "Не найдено задание или пользователь")
    })
    public StudentTask createTask(@ModelAttribute StudentCreateTaskDto studentTaskDto, Authentication authentication) {

        String fileName = String.join(
                "_",
                "file",
                //todo remove filename
                studentTaskDto.getFile().getOriginalFilename(),
                authentication.getName(),
                randomUUID().toString()
        );
        uploadScript(fileName, studentTaskDto.getFile());


        User user = userRepo.findByUsername(authentication.getName())
                .orElseThrow(() -> new NoSuchElementException("username not found or user not login"));

        StudentTask task = StudentTask.builder()
                .student(studentService.findByUserId(user.getId()))
                .task(taskRepository
                        .findById(studentTaskDto.getTaskId())
                        .orElseThrow(() -> new NoSuchElementException("not found task"))
                )
                .createdAt(LocalDateTime.now())
                .filePath(fileName)
                .build();
        return studentTasksRepository.save(task);
    }

    /**
     * Удаляет задание студента по его идентификатору.
     *
     * @param studentId идентификатор студента
     * @param taskId    идентификатор задания
     * @return объект ResponseEntity со статусом "204 No Content" в случае успешного удаления,
     * или "404 Not Found" в случае, если задание не найдено
     */
    @PreAuthorize("hasAnyRole('ROLE_TEACHER')")
    @DeleteMapping("/{taskId}")
    @Operation(summary = "Удалить задание студента", description = "Удалить задание студента по его идентификатору")
    @ApiResponse(responseCode = "204", description = "Задание студента успешно удалено")
    @ApiResponse(responseCode = "404", description = "Задание студента не найдено")
    public ResponseEntity<Void> deleteStudentTask(@PathVariable Long studentId, @PathVariable Long taskId) {
        boolean deleted = studentTaskService.deleteStudentTask(studentId, taskId);

        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public static String getUploadedDirPath() {
        Path root = FileSystems.getDefault().getPath("").toAbsolutePath();
        return Path.of(root.toString(), "uploaded").toAbsolutePath().toString();
    }

    /**
     * Получение файла с учетом директории, с возможностью ее создания.
     *
     * @param directory Директория, в которой должен быть создан файл.
     * @param filename  Имя создаваемого файла.
     * @return Файл с указанным именем в указанной директории.
     * @throws IllegalArgumentException если произошла ошибка при создании директории.
     */
    @Operation(summary = "Получение файла с учетом директории, с возможностью ее создания")
    @ApiResponse(responseCode = "200", description = "Возвращает файл с указанным именем в указанной директории.")
    @ApiResponse(responseCode = "400", description = "Ошибка при создании директории.")
    private static File fileWithDirectoryAssurance(String directory, String filename) {
        File dir = new File(directory);
        if (!dir.exists()) {
            boolean isCreatedDir = dir.mkdirs();
            if (!isCreatedDir) throw new IllegalArgumentException("error when folder create");
        }
        return new File(Path.of(directory, filename).toUri());
    }

    /**
     * Метод для загрузки скрипта на сервер.
     *
     * @param name Имя скрипта.
     * @param file MultipartFile, содержащий файл скрипта.
     * @return Сообщение, описывающее результат загрузки скрипта.
     */
    @Operation(summary = "Загрузка скрипта", description = "Метод для загрузки скрипта на сервер")
    @ApiResponse(responseCode = "200", description = "Успешная загрузка скрипта")
    @ApiResponse(responseCode = "400", description = "Ошибка в запросе")
    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    public String uploadScript(String name, MultipartFile file) {
        String pathToUploaded = fileWithDirectoryAssurance(
                //todo move to function util or config
                getUploadedDirPath(),
                name + ".ino"
        ).getAbsolutePath();
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                try (BufferedOutputStream stream =
                             new BufferedOutputStream(new FileOutputStream(pathToUploaded))
                ) {
                    stream.write(bytes);
                }
                return "Вы удачно загрузили " + name + " в " + name + "-uploaded !";
            } catch (Exception e) {
                return "Вам не удалось загрузить " + name + " => " + e.getMessage();
            }
        } else {
            return "Вам не удалось загрузить " + name + " потому что файл пустой.";
        }
    }

    /**
     * Метод для установки оценки за задание студента.
     *
     * @param req объект класса SetGradeStudentTask, содержащий идентификатор задания студента и оценку.
     * @return HTTP-ответ со статусом 200 OK.
     * @throws IllegalStateException если задание студента не найдено.
     */
    @Operation(summary = "Установка оценки за задание студента", description = "Метод для установки оценки за задание студента")
    @ApiResponse(responseCode = "200", description = "Оценка успешно установлена")
    @ApiResponse(responseCode = "400", description = "Ошибка в запросе")
    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    @PostMapping("/grade")
    @PreAuthorize("hasRole('TEACHER')")
    ResponseEntity<Void> setGradeStudentTask(@Valid @RequestBody SetGradeStudentTask req) {
        StudentTask task = studentTasksRepository.findById(req.getStudentTaskId())
                .orElseThrow(() -> new IllegalStateException("no found student task"));
        task.setGrade(req.getGrade());
        task.setStatus(StudentTaskStatus.AFFIXED);
        studentTasksRepository.save(task);
        return ResponseEntity.ok().build();
    }

    /**
     * Метод для получения содержимого файла задания студента.
     *
     * @param studentTaskId идентификатор задания студента.
     * @return HTTP-ответ со статусом 200 OK и содержимым файла задания в теле ответа.
     * @throws IllegalStateException если задание студента не найдено.
     */
    @Operation(summary = "Получение содержимого файла задания студента", description = "Метод для получения содержимого файла задания студента")
    @ApiResponse(responseCode = "200", description = "Содержимое файла успешно получено")
    @ApiResponse(responseCode = "400", description = "Ошибка в запросе")
    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    @GetMapping("/file_content/{studentTaskId}")
    @PreAuthorize("hasAnyRole('ROLE_TEACHER', 'ROLE_STUDENT')")
    ResponseEntity<String> getFileContent(@PathVariable long studentTaskId) {
        StudentTask task = studentTasksRepository.findById(studentTaskId)
                .orElseThrow(() -> new IllegalStateException("no found student task"));
        String fileContent = getUploadedFileContent(task);
        return ResponseEntity.ok(fileContent);
    }


}
