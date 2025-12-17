package com.lms.student.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GradeDTO {
    private Long id;
    private Long enrollmentId;
    private Long studentId;
    private Long courseId;
    private Double value;
    private String gradeType;
    private String letterGrade;
    private Boolean passing;
    private String gradedAt;
}
