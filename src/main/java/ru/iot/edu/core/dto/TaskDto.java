package ru.iot.edu.core.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.iot.edu.model.Stand;

@Data
@NoArgsConstructor
public class TaskDto {
    @Schema(description = "Идентификатор задачи")
    private long id;

    @Schema(description = "Название задачи", example = "Лабораторная работа №1")
    private String name;

    @Schema(description = "Описание задачи", example = "Включить и выключить светодиод")
    private String description;

    @Schema(description = "Продолжительность выполнения задачи в минутах", example = "60")
    private long duration = 10;

    @Schema(description = "Стенд, на котором выполняется задача")
    private Stand stand;
}

