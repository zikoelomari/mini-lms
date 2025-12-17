package com.lms.enrollment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseDTO {
    private Long id;
    private String courseCode;
    private String title;
    private String description;
    private String professorName;
    private Integer credits;
    private Integer maxCapacity;
    private Integer currentEnrollment;
    private Integer availableSpots;
    private String status;
}
