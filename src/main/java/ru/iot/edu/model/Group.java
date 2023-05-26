package ru.iot.edu.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "groups")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Идентификатор группы", example = "1")
    private long id;

    @Schema(description = "Название группы", example = "Группа А")
    private String name;

    @Schema(description = "Описание группы", example = "Группа дневного обучения")
    private String description;
}

