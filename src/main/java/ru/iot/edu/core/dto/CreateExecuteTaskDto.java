package ru.iot.edu.core.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class CreateExecuteTaskDto {
    @Schema(description = "Имя пользователя, инициирующего выполнение задания", example = "user123")
    private String initiatorUsername;

    @Schema(description = "Идентификатор лабораторной работы", example = "1")
    private long labID;

    @Schema(description = "Идентификатор стенда", example = "123")
    private long standId;

    @Schema(description = "Имя пользователя, роль которого будет использована для выполнения задания", example = "STUDENT")
    private String roleUsername;

    @Schema(description = "Продолжительность выполнения задания в секундах", example = "300")
    private long duration;

    @Schema(description = "Порт USB для подключения устройства", example = "COM3")
    private String usbPort;

    @Schema(description = "Название файла", example = "file.bin")
    private String nameFile;

    @Schema(description = "Файл, который будет загружен для выполнения задания")
    private MultipartFile file;

}








