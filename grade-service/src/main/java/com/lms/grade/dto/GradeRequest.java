package com.lms.grade.dto;

import com.lms.grade.entity.Grade;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GradeRequest {

    @NotNull(message = "L'ID de l'inscription est obligatoire")
    private Long enrollmentId;

    // Ajout pour permettre l'envoi direct depuis le frontend
    private Long studentId;

    // Ajout pour permettre l'envoi direct depuis le frontend
    private Long courseId;

    @NotNull(message = "La note est obligatoire")
    @DecimalMin(value = "0.0", message = "La note doit être supérieure ou égale à 0")
    @DecimalMax(value = "20.0", message = "La note doit être inférieure ou égale à 20")
    private Double value;

    private Grade.GradeType gradeType;

    private Double coefficient;

    private String comment;

    private String gradedBy;
}