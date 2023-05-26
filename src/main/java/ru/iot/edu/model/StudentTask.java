package ru.iot.edu.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "students_tasks")
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class StudentTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Уникальный идентификатор задания студента", example = "1")
    private long id;

    @Schema(description = "Оценка за задание студента", example = "1-100")
    private int grade;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "tasks_id", referencedColumnName = "id")
    private Task task;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "students_id", referencedColumnName = "id")
    private Student student;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "teachers_id", referencedColumnName = "id")
    private Teacher teacher;

    @Column(name = "file_path")
    @Schema(description = "Путь к файлу задания", example = "/home/user/tasks/1")
    private String filePath;

    @Column(name = "created_at", updatable = false)
    @Schema(description = "Дата и время создания задания студента", example = "2023-04-07T10:30:00")
    private LocalDateTime createdAt;

    @Column(name = "status")
    @Schema(description = "Статус задания ( отклонено, или проставлено)", example = "AFFIXED")
    @Enumerated(EnumType.STRING)
    private StudentTaskStatus status;

    //todo add comment for teacher
}
