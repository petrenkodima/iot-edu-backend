package ru.iot.edu.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "group_course")
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class GroupCourse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Идентификатор группы-курса", example = "1")
    private long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "groups_id", referencedColumnName = "id")
    @Schema(description = "Группа, к которой относится курс", example = "{id: 1, name: 'Группа 1'}")
    private Group group;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "courses_id", referencedColumnName = "id")
    @Schema(description = "Курс, который связан с группой", example = "{id: 1, name: 'Курс 1', description: 'Описание курса'}")
    private Course course;

    @Column(name = "is_show")
    @Builder.Default
    @Schema(description = "Отображается ли курс для данной группы", example = "true")
    private boolean isShow = true;
}
