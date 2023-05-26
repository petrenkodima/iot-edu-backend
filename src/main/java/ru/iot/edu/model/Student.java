package ru.iot.edu.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import ru.iot.edu.model.auth.User;

import javax.persistence.*;

@Entity
@Table(name = "students")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Идентификатор студента")
    private long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "users_id", referencedColumnName = "id")
    @Schema(description = "Пользователь-студент")
    private User user;
}

