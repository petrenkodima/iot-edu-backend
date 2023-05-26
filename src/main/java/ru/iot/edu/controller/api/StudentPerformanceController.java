package ru.iot.edu.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.iot.edu.model.StudentTask;
import ru.iot.edu.service.StudentPerformanceService;

import java.util.List;

import static ru.iot.edu.config.WebSecurityConfig.API_PATH;


@RestController
@RequestMapping(value = API_PATH + "/students/{studentId}/performance")
@CrossOrigin(origins = "*")
public class StudentPerformanceController {

    private final StudentPerformanceService studentPerformanceService;

    public StudentPerformanceController(StudentPerformanceService studentPerformanceService) {
        this.studentPerformanceService = studentPerformanceService;
    }

    /**
     * Получает среднюю оценку студента по задачам.
     *
     * @param studentId идентификатор студента
     * @return средняя оценка или код 404, если студент не найден или не имеет задач
     */
    @PreAuthorize("hasAnyRole('ROLE_STUDENT', 'ROLE_TEACHER')")
    @GetMapping
    @Operation(summary = "Получает среднюю оценку студента по задачам")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно получена средняя оценка",
                    content = { @Content(mediaType = "application/json", schema = @Schema(type = "number")) }),
            @ApiResponse(responseCode = "404", description = "Студент не найден или не имеет задач",
                    content = @Content)
    })
    public ResponseEntity<Double> getStudentPerformance(@PathVariable Long studentId) {
        List<StudentTask> studentTasks = studentPerformanceService.getStudentPerformance(studentId);
        Double averageGrade = studentPerformanceService.calculateAverageGrade(studentTasks);

        if (averageGrade == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(averageGrade);
    }
}
