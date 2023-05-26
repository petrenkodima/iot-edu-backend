package ru.iot.edu.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "tasks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Идентификатор задачи")
    private long id;

    @Schema(description = "Название задачи", example = "Задача №1")
    private String name;

    @Schema(description = "Описание задачи", example = "Измеряй температуру в лаборатории")
    private String description;

    @Schema(description = "Продолжительность выполнения задачи в минутах", example = "30")
    private long duration;

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "stands_id", referencedColumnName = "id")
    @Schema(description = "Стенд, на котором будет проходить задача")
    private Stand stand;
}
