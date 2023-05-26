package ru.iot.edu.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "courses")
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Уникальный идентификатор курса", example = "1")
    private long id;

    @Schema(description = "Название курса", example = "IoT")
    private String name;

    @Schema(description = "Описание курса", example = "Курс по основам IoT")
    private String description;
}
