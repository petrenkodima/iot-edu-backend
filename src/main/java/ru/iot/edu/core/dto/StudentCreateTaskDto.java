package ru.iot.edu.core.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@NoArgsConstructor
public class StudentCreateTaskDto {
    @Schema(description = "Идентификатор задачи")
    private long taskId;

    @Schema(description = "Файл, содержащий скрипт (решние задачи)")
    private MultipartFile file;
}
