package com.lms.enrollment.dto;

import com.lms.enrollment.entity.Enrollment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnrollmentResponse {

    private Long id;
    private Long studentId;
    private Long courseId;
    private String status;
    private LocalDateTime enrollmentDate;
    private LocalDateTime completionDate;

    // Informations enrichies (optionnelles)
    private StudentDTO student;
    private CourseDTO course;

    public static EnrollmentResponse fromEntity(Enrollment enrollment) {
        return EnrollmentResponse.builder()
                .id(enrollment.getId())
                .studentId(enrollment.getStudentId())
                .courseId(enrollment.getCourseId())
                .status(enrollment.getStatus().name())
                .enrollmentDate(enrollment.getEnrollmentDate())
                .completionDate(enrollment.getCompletionDate())
                .build();
    }

    public static EnrollmentResponse fromEntityWithDetails(
            Enrollment enrollment, 
            StudentDTO student, 
            CourseDTO course) {
        EnrollmentResponse response = fromEntity(enrollment);
        response.setStudent(student);
        response.setCourse(course);
        return response;
    }
}
