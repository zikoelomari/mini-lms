package com.lms.enrollment.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnrollmentRequest {

    @NotNull(message = "L'ID de l'étudiant est obligatoire")
    private Long studentId;

    @NotNull(message = "L'ID du cours est obligatoire")
    private Long courseId;
}
