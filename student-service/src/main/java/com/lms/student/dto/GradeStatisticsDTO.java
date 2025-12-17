package com.lms.student.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GradeStatisticsDTO {
    private Long entityId;
    private String entityType;
    private Double average;
    private Double minimum;
    private Double maximum;
    private Long totalGrades;
    private Long passingGrades;
    private Long failingGrades;
    private Double passRate;
    private String overallGrade;
}
