package com.lms.grade.dto;

import com.lms.grade.entity.Grade;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GradeResponse {

    private Long id;
    private Long enrollmentId;
    private Long studentId;
    private Long courseId;
    private Double value;
    private String gradeType;
    private Double coefficient;
    private String letterGrade;
    private Boolean passing;
    private String comment;
    private String gradedBy;
    private LocalDateTime gradedAt;

    // Informations enrichies (optionnelles)
    private StudentDTO student;
    private CourseDTO course;

    public static GradeResponse fromEntity(Grade grade) {
        return GradeResponse.builder()
                .id(grade.getId())
                .enrollmentId(grade.getEnrollmentId())
                .studentId(grade.getStudentId())
                .courseId(grade.getCourseId())
                .value(grade.getValue())
                .gradeType(grade.getGradeType().name())
                .coefficient(grade.getCoefficient())
                .letterGrade(grade.getLetterGrade())
                .passing(grade.isPassing())
                .comment(grade.getComment())
                .gradedBy(grade.getGradedBy())
                .gradedAt(grade.getGradedAt())
                .build();
    }

    public static GradeResponse fromEntityWithDetails(Grade grade, StudentDTO student, CourseDTO course) {
        GradeResponse response = fromEntity(grade);
        response.setStudent(student);
        response.setCourse(course);
        return response;
    }
}
