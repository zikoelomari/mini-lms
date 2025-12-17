package com.lms.student.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentFullProfile {

    private StudentResponse student;
    private List<EnrollmentInfo> enrollments;
    private List<GradeInfo> grades;
    private StudentStatistics statistics;
    private String gradesStatus;  // "available" ou "indisponible"
    private String enrollmentsStatus;  // "available" ou "indisponible"

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class EnrollmentInfo {
        private Long enrollmentId;
        private Long courseId;
        private String courseCode;
        private String courseTitle;
        private String professorName;
        private Integer credits;
        private String status;
        private String enrollmentDate;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class GradeInfo {
        private Long gradeId;
        private Long courseId;
        private String courseTitle;
        private Double value;
        private String gradeType;
        private String letterGrade;
        private Boolean passing;
        private String gradedAt;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class StudentStatistics {
        private Double average;
        private Long totalCourses;
        private Long completedCourses;
        private Long totalGrades;
        private Long passingGrades;
        private Double passRate;
        private String overallGrade;
    }
}
