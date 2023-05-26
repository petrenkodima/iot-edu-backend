package ru.iot.edu.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "task_group")
@Getter
@Setter
@NoArgsConstructor
public class TaskGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Идентификатор задачи в группе", example = "1")
    private long id;

    @Column(name = "expiration_date")
    @Schema(description = "Дата и время истечения срока выполнения задачи", example = "2023-05-07T10:30:00")
    private LocalDateTime expirationDate;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "tasks_id", referencedColumnName = "id")
    @Schema(description = "Задача", example = "1")
    private Task task;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "groups_id", referencedColumnName = "id")
    @Schema(description = "Группа", example = "1")
    private Group group;
}
