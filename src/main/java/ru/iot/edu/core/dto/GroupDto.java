package ru.iot.edu.core.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class GroupDto {
    @Schema(description = "Идентификатор группы", example = "1")
    private long id;

    @Schema(description = "Название группы", example = "Группа 1")
    private String name;

    @Schema(description = "Описание группы", example = "Группа дневного обучения")
    private String description;
}
