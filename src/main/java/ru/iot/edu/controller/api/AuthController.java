package ru.iot.edu.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.iot.edu.core.dto.ChangeRoleDto;
import ru.iot.edu.core.dto.UserDto;
import ru.iot.edu.model.*;
import ru.iot.edu.model.auth.Role;
import ru.iot.edu.model.auth.User;
import ru.iot.edu.repository.StudentRepository;
import ru.iot.edu.repository.TeacherRepository;
import ru.iot.edu.repository.UserRepo;
import ru.iot.edu.service.auth.UserDetailsServiceImpl;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api")
public class AuthController {

    private final UserDetailsServiceImpl userService;
    private final UserRepo userRepo;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;


    @Autowired
    public AuthController(UserDetailsServiceImpl userService,
                          UserRepo userRepo,
                          TeacherRepository teacherRepository,
                          StudentRepository studentRepository) {
        this.userService = userService;
        this.userRepo = userRepo;
        this.teacherRepository = teacherRepository;
        this.studentRepository = studentRepository;
    }

    /**
     * Регистрация нового пользователя в системе.
     *
     * @param userDto объект класса UserDto содержащий данные о новом пользователе
     * @param result  объект класса BindingResult содержащий результаты валидации userDto
     * @param model   объект класса Model для передачи атрибутов представлению
     * @return объект ResponseEntity со статусом 200 в случае успешной регистрации или со статусом 400
     * если пользователь уже существует или некорректный запрос
     */
    @PostMapping("/register/save")
    @SecurityRequirements()
    @Operation(summary = "Регистрация нового пользователя", description = "Регистрация нового пользователя в системе")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешная регистрация"),
            @ApiResponse(responseCode = "400", description = "Пользователь уже существует или некорректный запрос",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<Object> registration(@Valid @RequestBody UserDto userDto,
                                               BindingResult result,
                                               Model model) {

        Optional<User> existingUser = userService.findUserByUsername(userDto.getUsername());

        if (existingUser.isPresent()) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Пользователь с таким именем уже существует"));
        }

        if (result.hasErrors()) {
            List<FieldError> errors = result.getFieldErrors();
            List<ValidationError> validationErrors = new ArrayList<>();
            for (FieldError error : errors) {
                String fieldName = error.getField();
                String errorMessage = error.getDefaultMessage();
                validationErrors.add(new ValidationError(fieldName, errorMessage));
            }
            return ResponseEntity.badRequest().body(new ValidationErrorResponse(validationErrors));
        }

        userService.saveUserDto(userDto);
        return ResponseEntity.ok().build();
    }

    /**
     * Получение списка всех пользователей системы
     *
     * @param model модель для работы с данными
     * @return список пользователей
     */
    @GetMapping("/users")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Получение списка пользователей", description = "Получение списка всех пользователей системы")
    @ApiResponse(responseCode = "200", description = "Успешное получение списка пользователей")
    public List<UserDto> users(Model model) {
        return userService.findAllUsers();
    }

    /**
     * Получение имени текущего авторизованного пользователя
     *
     * @param authentication объект аутентификации
     * @return ResponseEntity с именем пользователя или статусом 401, если пользователь не авторизован
     */
    @GetMapping(value = "/username")
    @ResponseBody
    @Operation(summary = "Получение имени текущего пользователя", description = "Получение имени текущего авторизованного пользователя")
    @ApiResponse(responseCode = "200", description = "Успешное получение имени пользователя")
    @ApiResponse(responseCode = "401", description = "Неавторизованный запрос")
    public ResponseEntity<Map<String, String>> currentUserName(Authentication authentication) {
        if (authentication != null) {
            return ResponseEntity.ok(Map.of("username", authentication.getName()));
        }
        return new ResponseEntity(HttpStatus.UNAUTHORIZED);
    }

    /**
     * Получение списка ролей текущего авторизованного пользователя
     *
     * @param authentication объект аутентификации
     * @return ResponseEntity со списком ролей пользователя или статусом 401, если пользователь не авторизован
     */
    @GetMapping(value = "/whoiam")
    @ResponseBody
    @Operation(summary = "Получение списка ролей текущего пользователя", description = "Получение списка ролей текущего авторизованного пользователя")
    @ApiResponse(responseCode = "200", description = "Успешное получение списка ролей пользователя")
    @ApiResponse(responseCode = "401", description = "Неавторизованный запрос")
    public ResponseEntity<List<String>> whoIam(Authentication authentication) {
        if (authentication != null) {
            return ResponseEntity.ok(authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList()));
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    /**
     * Смена роли только для пользователя с дефолтной ролью
     *
     * @param changeRoleDto объект класса UserDto содержащий данные о новом пользователе
     * @return объект ResponseEntity со статусом 200 в случае успешной смены
     */
    @PostMapping("/change_role")
    @SecurityRequirements()
    @Operation(summary = "Смена роли", description = "Смена роли только для пользователя с дефолтной ролью")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешная смена"),
    })
    public ResponseEntity<?> changeRole(@RequestBody ChangeRoleDto changeRoleDto) {

        Optional<User> existingUser = userRepo.findById(changeRoleDto.getUserId());

        if (existingUser.isEmpty()) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Пользователя с таким именем не существует"));
        }
        User user = existingUser.get();
        if (user.getRoles().size() == 1 && user.getRoles().contains(Role.DEFAULT)) {
            if (changeRoleDto.getRole() == Role.STUDENT) {
                user.setRoles(new HashSet<>(List.of(Role.STUDENT)));
                userRepo.save(user);
                studentRepository.save(Student.builder()
                        .user(user)
                        .build());
               return ResponseEntity.ok().build();
            } else if (changeRoleDto.getRole() == Role.TEACHER) {
                user.setRoles(new HashSet<>(List.of(Role.TEACHER)));
                userRepo.save(user);
                teacherRepository.save(Teacher.builder()
                        .user(user)
                        .build());
                return ResponseEntity.ok().build();
            }
        }


        return ResponseEntity.badRequest().build();
    }


}