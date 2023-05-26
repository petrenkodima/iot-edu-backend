package ru.iot.edu.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.iot.edu.model.Course;
import ru.iot.edu.model.TeacherCourse;
import ru.iot.edu.service.TeacherCourseService;

import java.util.List;

@RestController
@RequestMapping("/teachers/{teacherId}/courses")
public class TeacherCourseController {
    private final TeacherCourseService teacherCourseService;

    @Autowired
    public TeacherCourseController(TeacherCourseService teacherCourseService) {
        this.teacherCourseService = teacherCourseService;
    }

    @GetMapping
    @Operation(summary = "Получить все курсы для определенного преподавателя",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Курсы найдены",
                            content = {@Content(mediaType = "application/json",
                                    schema = @Schema(implementation = TeacherCourse.class))}),
                    @ApiResponse(responseCode = "404", description = "Учитель не найден",
                            content = @Content),
                    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера",
                            content = @Content)
            })
    public ResponseEntity<List<TeacherCourse>> getAllCoursesByTeacherId(
            @Parameter(description = "ID учителя", example = "1") @PathVariable long teacherId) {
        List<TeacherCourse> courses = teacherCourseService.getAllCoursesByTeacherId(teacherId);
        if (courses.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(courses);
    }
}
