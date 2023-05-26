package ru.iot.edu.core.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class CreateGroupDto {
    @Schema(description = "Название группы", example = "Группа 1")
    private String name;

    @Schema(description = "Описание группы", example = "Группа дневного обучения")
    private String description;

    @Schema(description = "Описание группы", example = "Группа дневного обучения")
    private List<Long> studentsIds;

}
