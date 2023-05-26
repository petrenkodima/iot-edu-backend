package ru.iot.edu.core.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;


@Data
public class CreateCourseDto {
    //todo mb anotation with required or create new model for create method

    @Schema(description = "Название курса", example = "Погружение в IoT")
    private String name;

    @Schema(description = "Описание курса", example = "Курс по программированию жедеза для студентов IoT.")
    private String description;

    @Schema(description = "Список идентификаторов групп, привязанных к курсу", example = "[1, 2, 3]")
    private List<Long> groupsIds;
}

