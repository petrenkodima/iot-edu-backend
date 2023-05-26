package ru.iot.edu.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "course_task")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Идентификатор задачи курса", example = "1")
    private long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "tasks_id", referencedColumnName = "id")
    @Schema(description = "Задача, связанная с курсом")
    private Task task;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "courses_id", referencedColumnName = "id")
    @Schema(description = "Курс, связанный с задачей")
    private Course course;
}

