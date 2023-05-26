package ru.iot.edu.core.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import ru.iot.edu.model.Student;
import ru.iot.edu.model.Task;
import ru.iot.edu.model.Teacher;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class StudentTaskDto {
    @Schema(description = "Идентификатор задания")
    private long id;

    @Schema(description = "Оценка за задание", example = "1 - 100 баллов")
    private int grade;

    @Schema(description = "Задание")
    private Task task;

    @Schema(description = "Студент")
    private Student student;

    @Schema(description = "Преподаватель")
    private Teacher teacher;

    @Schema(description = "Путь к файлу, загруженному студентом")
    private String filePath;

    //todo add comment for teacher
    @Schema(description = "Комментарий преподавателя")
    private String teacherComment;

    @Schema(description = "Дата создания")
    private LocalDateTime createdAt;

}
