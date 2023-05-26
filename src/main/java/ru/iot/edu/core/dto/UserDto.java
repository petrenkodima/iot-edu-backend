package ru.iot.edu.core.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.iot.edu.model.auth.Role;

import javax.validation.constraints.NotEmpty;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    @Schema(description = "Идентификатор пользователя", example = "1")
    private Long id;

    @NotEmpty
    @Schema(description = "Имя пользователя", example = "Иван")
    private String firstName;

    @NotEmpty
    @Schema(description = "Фамилия пользователя", example = "Иванов")
    private String lastName;

    @NotEmpty(message = "username should not be empty")
    @Schema(description = "Имя пользователя", example = "ivanov_123")
    private String username;

    @NotEmpty(message = "Password should be empty")
    @Schema(description = "Пароль пользователя", example = "Qwerty123")
    private String password;

    @Schema(description = "Список ролей пользователя")
    private Set<Role> roles;
}

