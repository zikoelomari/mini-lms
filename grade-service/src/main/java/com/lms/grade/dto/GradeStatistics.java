package com.lms.grade.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GradeStatistics {

    private Long entityId;          // studentId ou courseId
    private String entityType;      // "STUDENT" ou "COURSE"
    private Double average;
    private Double minimum;
    private Double maximum;
    private Long totalGrades;
    private Long passingGrades;
    private Long failingGrades;
    private Double passRate;        // Pourcentage de réussite
    private String overallGrade;    // Note lettre moyenne

    public static GradeStatistics empty(Long entityId, String entityType) {
        return GradeStatistics.builder()
                .entityId(entityId)
                .entityType(entityType)
                .average(0.0)
                .minimum(0.0)
                .maximum(0.0)
                .totalGrades(0L)
                .passingGrades(0L)
                .failingGrades(0L)
                .passRate(0.0)
                .overallGrade("N/A")
                .build();
    }
}
