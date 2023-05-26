package ru.iot.edu.core.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.iot.edu.model.auth.Role;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChangeRoleDto {

    @Schema(description = "Идентификатор пользователя", example = "1")
    private Long userId;

    @Schema(description = "Роль на которую нужно поменять")
    private Role role;
}

