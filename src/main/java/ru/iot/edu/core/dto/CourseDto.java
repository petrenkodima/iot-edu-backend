package ru.iot.edu.core.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Data
public class CourseDto {
    //todo mb anotation with required or create new model for create method

    @Schema(description = "Идентификатор курса", example = "1")
    private long id;

    @Schema(description = "Название курса", example = "Погружение в IoT")
    private String name;

    @Schema(description = "Описание курса", example = "Курс по программированию жедеза для студентов IoT")
    private String description;
}
