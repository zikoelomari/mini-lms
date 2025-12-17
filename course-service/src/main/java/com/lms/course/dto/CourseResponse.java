package com.lms.course.dto;

import com.lms.course.entity.Course;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseResponse {

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
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static CourseResponse fromEntity(Course course) {
        Integer available = null;
        if (course.getMaxCapacity() != null) {
            available = course.getMaxCapacity() - course.getCurrentEnrollment();
        }

        return CourseResponse.builder()
                .id(course.getId())
                .courseCode(course.getCourseCode())
                .title(course.getTitle())
                .description(course.getDescription())
                .professorName(course.getProfessorName())
                .credits(course.getCredits())
                .maxCapacity(course.getMaxCapacity())
                .currentEnrollment(course.getCurrentEnrollment())
                .availableSpots(available)
                .status(course.getStatus().name())
                .createdAt(course.getCreatedAt())
                .updatedAt(course.getUpdatedAt())
                .build();
    }
}
