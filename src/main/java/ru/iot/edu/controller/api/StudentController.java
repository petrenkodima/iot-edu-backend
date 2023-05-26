package ru.iot.edu.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.iot.edu.model.Student;import ru.iot.edu.repository.StudentRepository;

import java.util.List;

import static ru.iot.edu.config.WebSecurityConfig.API_PATH;


@RestController
@RequestMapping(value = API_PATH + "/students")
@CrossOrigin(origins = "*")
public class StudentController {
    private final StudentRepository studentRepository;

    public StudentController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    /**
     * Получить всех студентов
     *
     * @return список всех студентов
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @Operation(summary = "Получить всех студентов", tags = {"students"})
    @ApiResponse(responseCode = "200", description = "Список всех студентов")
    public List<Student> getAll() {
        return studentRepository.findAll();
    }

}
