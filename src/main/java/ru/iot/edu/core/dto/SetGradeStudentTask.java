package ru.iot.edu.core.dto;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
public class SetGradeStudentTask {
    @Min(1)
    @Max(100)
    private int grade;
    private long studentTaskId;
}
