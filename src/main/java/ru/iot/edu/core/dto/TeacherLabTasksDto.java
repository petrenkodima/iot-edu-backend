package ru.iot.edu.core.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import ru.iot.edu.model.Student;
import ru.iot.edu.model.Task;
import ru.iot.edu.model.Teacher;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class TeacherLabTasksDto {
    @Schema(description = "Задание")
    private Task task;

    @Schema(description = "Задания которые прикрепили студенты")
    private List<TeacherLabTaskDto> tasks;

}

