package ru.iot.edu.model.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.security.core.GrantedAuthority;


public enum Role implements GrantedAuthority {

    @Schema(description = "Роль пользователя по умолчанию")
    DEFAULT,

    @Schema(description = "Роль студента")
    STUDENT,

    @Schema(description = "Роль учителя")
    TEACHER,

    @Schema(description = "Роль администратора")
    ADMIN;

    private static final String PREFIX = "ROLE_";

    @Override
    public String getAuthority() {
        return PREFIX + name();
    }

    public String getName() {
        return name();
    }
}
