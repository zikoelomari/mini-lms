package com.lms.enrollment.service;

import com.lms.enrollment.client.CourseClient;
import com.lms.enrollment.client.StudentClient;
import com.lms.enrollment.dto.*;
import com.lms.enrollment.entity.Enrollment;
import com.lms.enrollment.exception.EnrollmentAlreadyExistsException;
import com.lms.enrollment.exception.EnrollmentNotFoundException;
import com.lms.enrollment.exception.EnrollmentValidationException;
import com.lms.enrollment.repository.EnrollmentRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final StudentClient studentClient;
    private final CourseClient courseClient;

    @CircuitBreaker(name = "enrollmentService", fallbackMethod = "createEnrollmentFallback")
    public EnrollmentResponse createEnrollment(EnrollmentRequest request) {
        log.info("Création d'une inscription: étudiant {} -> cours {}", 
                request.getStudentId(), request.getCourseId());

        // RG-01-INSCR: Vérifier que l'étudiant existe
        Boolean studentExists = studentClient.studentExists(request.getStudentId());
        if (!studentExists) {
            throw new EnrollmentValidationException(
                    "L'étudiant avec l'ID " + request.getStudentId() + " n'existe pas");
        }

        // RG-01-INSCR: Vérifier que le cours existe
        Boolean courseExists = courseClient.courseExists(request.getCourseId());
        if (!courseExists) {
            throw new EnrollmentValidationException(
                    "Le cours avec l'ID " + request.getCourseId() + " n'existe pas");
        }

        // Vérifier que l'inscription n'existe pas déjà
        if (enrollmentRepository.existsByStudentIdAndCourseId(
                request.getStudentId(), request.getCourseId())) {
            throw new EnrollmentAlreadyExistsException(
                    request.getStudentId(), request.getCourseId());
        }

        // Vérifier les places disponibles
        Boolean hasSpots = courseClient.hasAvailableSpots(request.getCourseId());
        if (!hasSpots) {
            throw new EnrollmentValidationException(
                    "Le cours " + request.getCourseId() + " est complet");
        }

        // Créer l'inscription
        Enrollment enrollment = Enrollment.builder()
                .studentId(request.getStudentId())
                .courseId(request.getCourseId())
                .status(Enrollment.EnrollmentStatus.ACTIVE)
                .build();

        Enrollment savedEnrollment = enrollmentRepository.save(enrollment);

        // Incrémenter le compteur d'inscriptions du cours
        courseClient.incrementEnrollment(request.getCourseId());

        log.info("Inscription créée avec succès: ID {}", savedEnrollment.getId());

        return EnrollmentResponse.fromEntity(savedEnrollment);
    }

    public EnrollmentResponse createEnrollmentFallback(EnrollmentRequest request, Throwable t) {
        log.error("Fallback pour createEnrollment: {}", t.getMessage());
        throw new EnrollmentValidationException(
                "Service temporairement indisponible. Veuillez réessayer plus tard.");
    }

    @Transactional(readOnly = true)
    public EnrollmentResponse getEnrollmentById(Long id) {
        log.info("Recherche de l'inscription avec l'ID: {}", id);
        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new EnrollmentNotFoundException(id));
        return EnrollmentResponse.fromEntity(enrollment);
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "enrollmentService", fallbackMethod = "getEnrollmentWithDetailsFallback")
    public EnrollmentResponse getEnrollmentWithDetails(Long id) {
        log.info("Recherche de l'inscription avec détails: {}", id);
        
        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new EnrollmentNotFoundException(id));

        StudentDTO student = studentClient.getStudentById(enrollment.getStudentId());
        CourseDTO course = courseClient.getCourseById(enrollment.getCourseId());

        return EnrollmentResponse.fromEntityWithDetails(enrollment, student, course);
    }

    public EnrollmentResponse getEnrollmentWithDetailsFallback(Long id, Throwable t) {
        log.warn("Fallback pour getEnrollmentWithDetails: {}", t.getMessage());
        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new EnrollmentNotFoundException(id));
        
        // RG-03-AGREG: Retourner les infos partielles
        EnrollmentResponse response = EnrollmentResponse.fromEntity(enrollment);
        response.setStudent(StudentDTO.builder()
                .id(enrollment.getStudentId())
                .firstName("Données")
                .lastName("Indisponibles")
                .build());
        response.setCourse(CourseDTO.builder()
                .id(enrollment.getCourseId())
                .title("Données indisponibles")
                .build());
        return response;
    }

    @Transactional(readOnly = true)
    public List<EnrollmentResponse> getEnrollmentsByStudent(Long studentId) {
        log.info("Récupération des inscriptions de l'étudiant: {}", studentId);
        return enrollmentRepository.findByStudentId(studentId).stream()
                .map(EnrollmentResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "enrollmentService", fallbackMethod = "getEnrollmentsByStudentWithDetailsFallback")
    public List<EnrollmentResponse> getEnrollmentsByStudentWithDetails(Long studentId) {
        log.info("Récupération des inscriptions avec détails pour l'étudiant: {}", studentId);
        
        List<Enrollment> enrollments = enrollmentRepository.findByStudentId(studentId);
        
        return enrollments.stream()
                .map(enrollment -> {
                    CourseDTO course = courseClient.getCourseById(enrollment.getCourseId());
                    return EnrollmentResponse.fromEntityWithDetails(enrollment, null, course);
                })
                .collect(Collectors.toList());
    }

    public List<EnrollmentResponse> getEnrollmentsByStudentWithDetailsFallback(Long studentId, Throwable t) {
        log.warn("Fallback pour getEnrollmentsByStudentWithDetails: {}", t.getMessage());
        return enrollmentRepository.findByStudentId(studentId).stream()
                .map(EnrollmentResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<EnrollmentResponse> getEnrollmentsByCourse(Long courseId) {
        log.info("Récupération des inscriptions du cours: {}", courseId);
        return enrollmentRepository.findByCourseId(courseId).stream()
                .map(EnrollmentResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "enrollmentService", fallbackMethod = "getEnrollmentsByCourseWithDetailsFallback")
    public List<EnrollmentResponse> getEnrollmentsByCourseWithDetails(Long courseId) {
        log.info("Récupération des inscriptions avec détails pour le cours: {}", courseId);
        
        List<Enrollment> enrollments = enrollmentRepository.findByCourseId(courseId);
        
        return enrollments.stream()
                .map(enrollment -> {
                    StudentDTO student = studentClient.getStudentById(enrollment.getStudentId());
                    return EnrollmentResponse.fromEntityWithDetails(enrollment, student, null);
                })
                .collect(Collectors.toList());
    }

    public List<EnrollmentResponse> getEnrollmentsByCourseWithDetailsFallback(Long courseId, Throwable t) {
        log.warn("Fallback pour getEnrollmentsByCourseWithDetails: {}", t.getMessage());
        return enrollmentRepository.findByCourseId(courseId).stream()
                .map(EnrollmentResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<EnrollmentResponse> getAllEnrollments() {
        log.info("Récupération de toutes les inscriptions");
        return enrollmentRepository.findAll().stream()
                .map(EnrollmentResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public EnrollmentResponse updateEnrollmentStatus(Long id, Enrollment.EnrollmentStatus status) {
        log.info("Mise à jour du statut de l'inscription {} vers {}", id, status);

        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new EnrollmentNotFoundException(id));

        Enrollment.EnrollmentStatus oldStatus = enrollment.getStatus();
        enrollment.setStatus(status);

        if (status == Enrollment.EnrollmentStatus.COMPLETED) {
            enrollment.setCompletionDate(LocalDateTime.now());
        }

        // Si on passe de ACTIVE à DROPPED/CANCELLED, décrémenter le compteur
        if (oldStatus == Enrollment.EnrollmentStatus.ACTIVE && 
            (status == Enrollment.EnrollmentStatus.DROPPED || 
             status == Enrollment.EnrollmentStatus.CANCELLED)) {
            try {
                courseClient.decrementEnrollment(enrollment.getCourseId());
            } catch (Exception e) {
                log.warn("Impossible de décrémenter le compteur du cours: {}", e.getMessage());
            }
        }

        Enrollment updatedEnrollment = enrollmentRepository.save(enrollment);
        return EnrollmentResponse.fromEntity(updatedEnrollment);
    }

    public void deleteEnrollment(Long id) {
        log.info("Suppression de l'inscription: {}", id);

        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new EnrollmentNotFoundException(id));

        // Décrémenter le compteur si l'inscription était active
        if (enrollment.getStatus() == Enrollment.EnrollmentStatus.ACTIVE) {
            try {
                courseClient.decrementEnrollment(enrollment.getCourseId());
            } catch (Exception e) {
                log.warn("Impossible de décrémenter le compteur du cours: {}", e.getMessage());
            }
        }

        enrollmentRepository.deleteById(id);
        log.info("Inscription supprimée avec succès");
    }

    @Transactional(readOnly = true)
    public boolean enrollmentExists(Long studentId, Long courseId) {
        return enrollmentRepository.existsByStudentIdAndCourseId(studentId, courseId);
    }

    @Transactional(readOnly = true)
    public long countActiveEnrollmentsByCourse(Long courseId) {
        return enrollmentRepository.countByCourseIdAndStatus(
                courseId, Enrollment.EnrollmentStatus.ACTIVE);
    }
}
