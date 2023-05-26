package ru.iot.edu.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "stands")
@Getter
@Setter
@NoArgsConstructor
public class Stand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Идентификатор стенда", example = "1")
    private long id;

    @Schema(description = "Название стенда", example = "Стенд 1")
    private String name;

    @Schema(description = "Описание стенда", example = "Этот стенд используется для демонстрации работы приложения")
    private String description;

    @Schema(description = "Имя порта для подключения к стенду", example = "COM1")
    private String portName;
}
