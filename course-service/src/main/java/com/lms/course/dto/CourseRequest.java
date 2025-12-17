package com.lms.course.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseRequest {

    @NotBlank(message = "Le code du cours est obligatoire")
    private String courseCode;

    @NotBlank(message = "Le titre est obligatoire")
    private String title;

    private String description;

    @NotBlank(message = "Le nom du professeur est obligatoire")
    private String professorName;

    @NotNull(message = "Le nombre de crédits est obligatoire")
    @Min(value = 1, message = "Le nombre de crédits minimum est 1")
    @Max(value = 10, message = "Le nombre de crédits maximum est 10")
    private Integer credits;

    @Positive(message = "La capacité maximale doit être positive")
    private Integer maxCapacity;
}
