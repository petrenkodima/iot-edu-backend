package ru.iot.edu.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "student_group")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Уникальный идентификатор записи студента в группе", example = "1")
    private long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "student_id", referencedColumnName = "id")
    @Schema(description = "Студент, записанный в группу", example = "John Doe")
    private Student student;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "groups_id", referencedColumnName = "id")
    @Schema(description = "Группа, в которую записан студент", example = "Group 1")
    private Group group;
}
