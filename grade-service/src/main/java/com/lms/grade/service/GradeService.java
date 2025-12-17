package com.lms.grade.service;

import com.lms.grade.client.CourseClient;
import com.lms.grade.client.EnrollmentClient;
import com.lms.grade.client.StudentClient;
import com.lms.grade.dto.*;
import com.lms.grade.entity.Grade;
import com.lms.grade.exception.GradeAlreadyExistsException;
import com.lms.grade.exception.GradeNotFoundException;
import com.lms.grade.exception.InvalidGradeException;
import com.lms.grade.repository.GradeRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class GradeService {

    private final GradeRepository gradeRepository;
    private final EnrollmentClient enrollmentClient;
    private final StudentClient studentClient;
    private final CourseClient courseClient;

    @CircuitBreaker(name = "gradeService", fallbackMethod = "createGradeFallback")
    public GradeResponse createGrade(GradeRequest request) {
        log.info("Création d'une note pour l'inscription: {}", request.getEnrollmentId());

        // RG-02-NOTE: Validation de la note (0-20)
        if (request.getValue() < 0 || request.getValue() > 20) {
            throw new InvalidGradeException(request.getValue());
        }

        // Récupérer studentId et courseId depuis la requête ou via Feign
        Long studentId = request.getStudentId();
        Long courseId = request.getCourseId();

        // Si studentId ou courseId ne sont pas fournis, essayer via Feign
        if (studentId == null || courseId == null) {
            try {
                EnrollmentDTO enrollment = enrollmentClient.getEnrollmentById(request.getEnrollmentId());
                if (enrollment != null && enrollment.getId() != null) {
                    if (studentId == null) studentId = enrollment.getStudentId();
                    if (courseId == null) courseId = enrollment.getCourseId();
                }
            } catch (Exception e) {
                log.warn("Impossible de récupérer l'inscription via Feign: {}", e.getMessage());
            }
        }

        // Vérifier que studentId et courseId sont définis
        if (studentId == null || courseId == null) {
            throw new InvalidGradeException("Impossible de déterminer l'étudiant ou le cours. Veuillez fournir studentId et courseId.");
        }

        // Déterminer le type de note
        Grade.GradeType gradeType = request.getGradeType() != null
                ? request.getGradeType()
                : Grade.GradeType.EXAM;

        // Vérifier si une note de ce type existe déjà
        if (gradeRepository.existsByEnrollmentIdAndGradeType(request.getEnrollmentId(), gradeType)) {
            throw new GradeAlreadyExistsException(request.getEnrollmentId(), gradeType);
        }

        Grade grade = Grade.builder()
                .enrollmentId(request.getEnrollmentId())
                .studentId(studentId)
                .courseId(courseId)
                .value(request.getValue())
                .gradeType(gradeType)
                .coefficient(request.getCoefficient() != null ? request.getCoefficient() : 1.0)
                .comment(request.getComment())
                .gradedBy(request.getGradedBy())
                .build();

        Grade savedGrade = gradeRepository.save(grade);
        log.info("Note créée avec succès: ID {}", savedGrade.getId());

        return GradeResponse.fromEntity(savedGrade);
    }

    public GradeResponse createGradeFallback(GradeRequest request, Throwable t) {
        log.error("Fallback pour createGrade: {}", t.getMessage());
        throw new InvalidGradeException("Service temporairement indisponible. Veuillez réessayer plus tard.");
    }

    @Transactional(readOnly = true)
    public GradeResponse getGradeById(Long id) {
        log.info("Recherche de la note avec l'ID: {}", id);
        Grade grade = gradeRepository.findById(id)
                .orElseThrow(() -> new GradeNotFoundException(id));
        return GradeResponse.fromEntity(grade);
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "gradeService", fallbackMethod = "getGradeWithDetailsFallback")
    public GradeResponse getGradeWithDetails(Long id) {
        log.info("Recherche de la note avec détails: {}", id);

        Grade grade = gradeRepository.findById(id)
                .orElseThrow(() -> new GradeNotFoundException(id));

        StudentDTO student = studentClient.getStudentById(grade.getStudentId());
        CourseDTO course = courseClient.getCourseById(grade.getCourseId());

        return GradeResponse.fromEntityWithDetails(grade, student, course);
    }

    public GradeResponse getGradeWithDetailsFallback(Long id, Throwable t) {
        log.warn("Fallback pour getGradeWithDetails: {}", t.getMessage());
        Grade grade = gradeRepository.findById(id)
                .orElseThrow(() -> new GradeNotFoundException(id));
        return GradeResponse.fromEntity(grade);
    }

    @Transactional(readOnly = true)
    public List<GradeResponse> getGradesByEnrollment(Long enrollmentId) {
        log.info("Récupération des notes de l'inscription: {}", enrollmentId);
        return gradeRepository.findByEnrollmentId(enrollmentId).stream()
                .map(GradeResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<GradeResponse> getGradesByStudent(Long studentId) {
        log.info("Récupération des notes de l'étudiant: {}", studentId);
        return gradeRepository.findByStudentId(studentId).stream()
                .map(GradeResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "gradeService", fallbackMethod = "getGradesByStudentWithDetailsFallback")
    public List<GradeResponse> getGradesByStudentWithDetails(Long studentId) {
        log.info("Récupération des notes avec détails pour l'étudiant: {}", studentId);

        List<Grade> grades = gradeRepository.findByStudentId(studentId);

        return grades.stream()
                .map(grade -> {
                    CourseDTO course = courseClient.getCourseById(grade.getCourseId());
                    return GradeResponse.fromEntityWithDetails(grade, null, course);
                })
                .collect(Collectors.toList());
    }

    public List<GradeResponse> getGradesByStudentWithDetailsFallback(Long studentId, Throwable t) {
        log.warn("Fallback pour getGradesByStudentWithDetails: {}", t.getMessage());
        return getGradesByStudent(studentId);
    }

    @Transactional(readOnly = true)
    public List<GradeResponse> getGradesByCourse(Long courseId) {
        log.info("Récupération des notes du cours: {}", courseId);
        return gradeRepository.findByCourseId(courseId).stream()
                .map(GradeResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "gradeService", fallbackMethod = "getGradesByCourseWithDetailsFallback")
    public List<GradeResponse> getGradesByCourseWithDetails(Long courseId) {
        log.info("Récupération des notes avec détails pour le cours: {}", courseId);

        List<Grade> grades = gradeRepository.findByCourseId(courseId);

        return grades.stream()
                .map(grade -> {
                    StudentDTO student = studentClient.getStudentById(grade.getStudentId());
                    return GradeResponse.fromEntityWithDetails(grade, student, null);
                })
                .collect(Collectors.toList());
    }

    public List<GradeResponse> getGradesByCourseWithDetailsFallback(Long courseId, Throwable t) {
        log.warn("Fallback pour getGradesByCourseWithDetails: {}", t.getMessage());
        return getGradesByCourse(courseId);
    }

    @Transactional(readOnly = true)
    public List<GradeResponse> getAllGrades() {
        log.info("Récupération de toutes les notes");
        return gradeRepository.findAll().stream()
                .map(GradeResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public GradeResponse updateGrade(Long id, GradeRequest request) {
        log.info("Mise à jour de la note avec l'ID: {}", id);

        Grade grade = gradeRepository.findById(id)
                .orElseThrow(() -> new GradeNotFoundException(id));

        // RG-02-NOTE: Validation de la note
        if (request.getValue() < 0 || request.getValue() > 20) {
            throw new InvalidGradeException(request.getValue());
        }

        grade.setValue(request.getValue());
        if (request.getCoefficient() != null) {
            grade.setCoefficient(request.getCoefficient());
        }
        if (request.getComment() != null) {
            grade.setComment(request.getComment());
        }
        if (request.getGradedBy() != null) {
            grade.setGradedBy(request.getGradedBy());
        }

        Grade updatedGrade = gradeRepository.save(grade);
        log.info("Note mise à jour avec succès");

        return GradeResponse.fromEntity(updatedGrade);
    }

    public void deleteGrade(Long id) {
        log.info("Suppression de la note: {}", id);

        if (!gradeRepository.existsById(id)) {
            throw new GradeNotFoundException(id);
        }

        gradeRepository.deleteById(id);
        log.info("Note supprimée avec succès");
    }

    // ========== STATISTIQUES ==========

    @Transactional(readOnly = true)
    public GradeStatistics getStudentStatistics(Long studentId) {
        log.info("Calcul des statistiques pour l'étudiant: {}", studentId);

        List<Grade> grades = gradeRepository.findByStudentId(studentId);
        if (grades.isEmpty()) {
            return GradeStatistics.empty(studentId, "STUDENT");
        }

        Double average = gradeRepository.calculateAverageByStudent(studentId);
        Double weightedAverage = gradeRepository.calculateWeightedAverageByStudent(studentId);
        Double min = gradeRepository.findMinByStudent(studentId);
        Double max = gradeRepository.findMaxByStudent(studentId);
        Long passing = gradeRepository.countPassingByStudent(studentId);
        long total = grades.size();
        long failing = total - passing;
        double passRate = (double) passing / total * 100;

        return GradeStatistics.builder()
                .entityId(studentId)
                .entityType("STUDENT")
                .average(weightedAverage != null ? Math.round(weightedAverage * 100.0) / 100.0 : average)
                .minimum(min)
                .maximum(max)
                .totalGrades(total)
                .passingGrades(passing)
                .failingGrades(failing)
                .passRate(Math.round(passRate * 100.0) / 100.0)
                .overallGrade(getLetterGrade(weightedAverage != null ? weightedAverage : average))
                .build();
    }

    @Transactional(readOnly = true)
    public GradeStatistics getCourseStatistics(Long courseId) {
        log.info("Calcul des statistiques pour le cours: {}", courseId);

        List<Grade> grades = gradeRepository.findByCourseId(courseId);
        if (grades.isEmpty()) {
            return GradeStatistics.empty(courseId, "COURSE");
        }

        Double average = gradeRepository.calculateAverageByCourse(courseId);
        Double weightedAverage = gradeRepository.calculateWeightedAverageByCourse(courseId);
        Double min = gradeRepository.findMinByCourse(courseId);
        Double max = gradeRepository.findMaxByCourse(courseId);
        Long passing = gradeRepository.countPassingByCourse(courseId);
        long total = grades.size();
        long failing = total - passing;
        double passRate = (double) passing / total * 100;

        return GradeStatistics.builder()
                .entityId(courseId)
                .entityType("COURSE")
                .average(weightedAverage != null ? Math.round(weightedAverage * 100.0) / 100.0 : average)
                .minimum(min)
                .maximum(max)
                .totalGrades(total)
                .passingGrades(passing)
                .failingGrades(failing)
                .passRate(Math.round(passRate * 100.0) / 100.0)
                .overallGrade(getLetterGrade(weightedAverage != null ? weightedAverage : average))
                .build();
    }

    @Transactional(readOnly = true)
    public Double getStudentAverage(Long studentId) {
        Double avg = gradeRepository.calculateWeightedAverageByStudent(studentId);
        return avg != null ? Math.round(avg * 100.0) / 100.0 : null;
    }

    @Transactional(readOnly = true)
    public Double getCourseAverage(Long courseId) {
        Double avg = gradeRepository.calculateWeightedAverageByCourse(courseId);
        return avg != null ? Math.round(avg * 100.0) / 100.0 : null;
    }

    private String getLetterGrade(Double value) {
        if (value == null) return "N/A";
        if (value >= 16) return "A";
        if (value >= 14) return "B";
        if (value >= 12) return "C";
        if (value >= 10) return "D";
        return "F";
    }
}