package ru.iot.edu.core.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GroupCourseDto {
    @Schema(description = "Идентификатор связи группы и курса")
    private long id;

    @Schema(description = "Идентификатор группы")
    private long groupId;

    @Schema(description = "Идентификатор курса")
    private long courseId;

    @Schema(description = "Флаг, указывающий на то, отображается ли данная связь на сайте")
    private boolean isShow;

}
