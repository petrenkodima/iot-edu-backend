package ru.iot.edu.core.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class StandDto {
    @Schema(description = "Идентификатор стенда")
    private long id;

    @Schema(description = "Название стенда")
    private String name;

    @Schema(description = "Описание стенда")
    private String description;

    @Schema(description = "Название порта, к которому подключен стенд")
    private String portName;
}

