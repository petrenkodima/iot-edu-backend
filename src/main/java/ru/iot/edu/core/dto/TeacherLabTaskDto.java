package ru.iot.edu.core.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class TeacherLabTaskDto {
    @Schema(description = "Идентификатор задания cтудента")
    private long id;

    @Schema(description = "Имя студента")
    private String studentName;

    @Schema(description = "Оценка за задание", example = "1 - 100 баллов")
    private int grade;

    @Schema(description = "Путь к файлу, загруженному студентом")
    private String filePath;

    @Schema(description = "Дата создания")
    private LocalDateTime createdAt;

    //todo add comment for teacher
    @Schema(description = "Комментарий преподавателя")
    private String teacherComment;
}
