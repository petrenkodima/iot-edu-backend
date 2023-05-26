package ru.iot.edu.core.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.core.io.FileSystemResource;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Builder
@AllArgsConstructor
public class ExecuteTaskDto {
    @Schema(description = "Название файла")
    private String nameFile;

    @Schema(description = "Имя пользователя, кто запустил задание")
    private String initiatorUsername;

    @Schema(description = "Идентификатор лабораторной работы")
    private long labID;

    @Schema(description = "Идентификатор стенда")
    private long standId;

    @Schema(description = "Имя пользователя, который выполняет задание")
    private String roleUsername;

    @Schema(description = "Продолжительность задания в минутах")
    private long duration;

    @Schema(description = "Порт USB, к которому подключен устройство")
    private String usbPort;

}








