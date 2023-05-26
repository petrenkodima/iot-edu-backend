package ru.iot.edu.core.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateTaskDto {
    @Schema(description = "Название задачи", example = "Задача №1")
    private String name;

    @Schema(description = "Описание задачи", example = "Загрузите скрипт, который будет моргать красным светодиодом каждые 15 секунд")
    private String description;

    @Schema(description = "Продолжительность задачи в минутах", example = "60")
    private long duration = 10;

    @Schema(description = "Идентификатор стенда для выполнения задачи", example = "123")
    private long standId;

    @Schema(description = "Идентификатор курса, в рамках которого будет выполнена задача", example = "456")
    private long courseId;

}
