package com.lms.student.service;

import com.lms.student.client.CourseClient;
import com.lms.student.client.EnrollmentClient;
import com.lms.student.client.GradeClient;
import com.lms.student.dto.*;
import com.lms.student.entity.Student;
import com.lms.student.exception.StudentNotFoundException;
import com.lms.student.repository.StudentRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudentProfileService {

    private final StudentRepository studentRepository;
    private final EnrollmentClient enrollmentClient;
    private final CourseClient courseClient;
    private final GradeClient gradeClient;

    /**
     * API de synthèse - Attente A du cahier des charges
     * Retourne la fiche complète d'un étudiant (Infos perso + Cours + Notes)
     * Implémente RG-03-AGREG: gestion des pannes partielles
     */
    @Transactional(readOnly = true)
    @CircuitBreaker(name = "studentProfile", fallbackMethod = "getFullProfileFallback")
    public StudentFullProfile getFullProfile(Long studentId) {
        log.info("Récupération du profil complet pour l'étudiant: {}", studentId);

        // 1. Récupérer les informations de l'étudiant
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException(studentId));
        StudentResponse studentResponse = StudentResponse.fromEntity(student);

        // 2. Récupérer les inscriptions
        List<StudentFullProfile.EnrollmentInfo> enrollmentInfos = new ArrayList<>();
        String enrollmentsStatus = "available";
        try {
            List<EnrollmentDTO> enrollments = enrollmentClient.getEnrollmentsByStudent(studentId);
            enrollmentInfos = enrichEnrollments(enrollments);
        } catch (Exception e) {
            log.warn("Impossible de récupérer les inscriptions: {}", e.getMessage());
            enrollmentsStatus = "indisponible";
        }

        // 3. Récupérer les notes (RG-03-AGREG)
        List<StudentFullProfile.GradeInfo> gradeInfos = new ArrayList<>();
        String gradesStatus = "available";
        try {
            List<GradeDTO> grades = gradeClient.getGradesByStudent(studentId);
            gradeInfos = enrichGrades(grades);
        } catch (Exception e) {
            log.warn("Impossible de récupérer les notes: {}", e.getMessage());
            gradesStatus = "indisponible";
        }

        // 4. Récupérer les statistiques
        StudentFullProfile.StudentStatistics statistics = buildStatistics(studentId, enrollmentInfos, gradeInfos);

        return StudentFullProfile.builder()
                .student(studentResponse)
                .enrollments(enrollmentInfos)
                .grades(gradeInfos)
                .statistics(statistics)
                .enrollmentsStatus(enrollmentsStatus)
                .gradesStatus(gradesStatus)
                .build();
    }

    /**
     * Fallback method - RG-03-AGREG
     * Retourne les infos partielles si un service est HS
     */
    public StudentFullProfile getFullProfileFallback(Long studentId, Throwable t) {
        log.warn("Fallback pour getFullProfile({}): {}", studentId, t.getMessage());

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException(studentId));
        StudentResponse studentResponse = StudentResponse.fromEntity(student);

        return StudentFullProfile.builder()
                .student(studentResponse)
                .enrollments(Collections.emptyList())
                .grades(Collections.emptyList())
                .statistics(StudentFullProfile.StudentStatistics.builder()
                        .average(null)
                        .totalCourses(0L)
                        .completedCourses(0L)
                        .totalGrades(0L)
                        .passingGrades(0L)
                        .passRate(0.0)
                        .overallGrade("N/A")
                        .build())
                .enrollmentsStatus("indisponible")
                .gradesStatus("indisponible")
                .build();
    }

    private List<StudentFullProfile.EnrollmentInfo> enrichEnrollments(List<EnrollmentDTO> enrollments) {
        return enrollments.stream()
                .map(enrollment -> {
                    CourseDTO course = null;
                    try {
                        course = courseClient.getCourseById(enrollment.getCourseId());
                    } catch (Exception e) {
                        log.warn("Impossible de récupérer le cours {}: {}", 
                                enrollment.getCourseId(), e.getMessage());
                    }

                    return StudentFullProfile.EnrollmentInfo.builder()
                            .enrollmentId(enrollment.getId())
                            .courseId(enrollment.getCourseId())
                            .courseCode(course != null ? course.getCourseCode() : "N/A")
                            .courseTitle(course != null ? course.getTitle() : "Cours indisponible")
                            .professorName(course != null ? course.getProfessorName() : "N/A")
                            .credits(course != null ? course.getCredits() : null)
                            .status(enrollment.getStatus())
                            .enrollmentDate(enrollment.getEnrollmentDate())
                            .build();
                })
                .collect(Collectors.toList());
    }

    private List<StudentFullProfile.GradeInfo> enrichGrades(List<GradeDTO> grades) {
        // Grouper les notes par cours pour éviter les appels répétitifs
        Map<Long, List<GradeDTO>> gradesByCourse = grades.stream()
                .collect(Collectors.groupingBy(GradeDTO::getCourseId));

        List<StudentFullProfile.GradeInfo> gradeInfos = new ArrayList<>();

        for (Map.Entry<Long, List<GradeDTO>> entry : gradesByCourse.entrySet()) {
            Long courseId = entry.getKey();
            CourseDTO course = null;
            try {
                course = courseClient.getCourseById(courseId);
            } catch (Exception e) {
                log.warn("Impossible de récupérer le cours {}: {}", courseId, e.getMessage());
            }

            final CourseDTO finalCourse = course;
            entry.getValue().forEach(grade -> {
                gradeInfos.add(StudentFullProfile.GradeInfo.builder()
                        .gradeId(grade.getId())
                        .courseId(courseId)
                        .courseTitle(finalCourse != null ? finalCourse.getTitle() : "Cours indisponible")
                        .value(grade.getValue())
                        .gradeType(grade.getGradeType())
                        .letterGrade(grade.getLetterGrade())
                        .passing(grade.getPassing())
                        .gradedAt(grade.getGradedAt())
                        .build());
            });
        }

        return gradeInfos;
    }

    private StudentFullProfile.StudentStatistics buildStatistics(
            Long studentId,
            List<StudentFullProfile.EnrollmentInfo> enrollments,
            List<StudentFullProfile.GradeInfo> grades) {

        Double average = null;
        try {
            average = gradeClient.getStudentAverage(studentId);
        } catch (Exception e) {
            log.warn("Impossible de récupérer la moyenne: {}", e.getMessage());
        }

        long totalCourses = enrollments.size();
        long completedCourses = enrollments.stream()
                .filter(e -> "COMPLETED".equals(e.getStatus()))
                .count();
        long totalGrades = grades.size();
        long passingGrades = grades.stream()
                .filter(g -> g.getPassing() != null && g.getPassing())
                .count();
        double passRate = totalGrades > 0 ? (double) passingGrades / totalGrades * 100 : 0;

        String overallGrade = "N/A";
        if (average != null) {
            if (average >= 16) overallGrade = "A";
            else if (average >= 14) overallGrade = "B";
            else if (average >= 12) overallGrade = "C";
            else if (average >= 10) overallGrade = "D";
            else overallGrade = "F";
        }

        return StudentFullProfile.StudentStatistics.builder()
                .average(average)
                .totalCourses(totalCourses)
                .completedCourses(completedCourses)
                .totalGrades(totalGrades)
                .passingGrades(passingGrades)
                .passRate(Math.round(passRate * 100.0) / 100.0)
                .overallGrade(overallGrade)
                .build();
    }
}
