package ru.iot.edu.model.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID пользователя", example = "1")
    private Long id;

    @Column(nullable = false)
    @Schema(description = "Имя пользователя", example = "Иван")
    private String firstName;

    @Column(nullable = false)
    @Schema(description = "Фамилия пользователя", example = "Иванов")
    private String lastName;

    @Column(nullable = false, unique = true)
    @Schema(description = "Имя пользователя (логин)", example = "ivan")
    private String username;

    @Column(nullable = false)
    @Schema(description = "Пароль пользователя", example = "myPassword")
    private String password;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    @Schema(description = "Роли пользователя", example = "[\"STUDENT\",\"TEACHER\"]")
    private Set<Role> roles;

}
