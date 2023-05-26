package ru.iot.edu.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import ru.iot.edu.model.auth.User;

import javax.persistence.*;

@Entity
@Table(name = "teachers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Идентификатор учителя", example = "1")
    private long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "users_id", referencedColumnName = "id")
    @Schema(description = "Пользователь, связанный с учителем", example = "{id: 1, firstName: 'Иван', lastName: 'Иванов'}")
    private User user;
}

