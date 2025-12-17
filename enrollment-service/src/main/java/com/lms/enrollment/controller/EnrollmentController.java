package com.lms.enrollment.controller;

import com.lms.enrollment.dto.EnrollmentRequest;
import com.lms.enrollment.dto.EnrollmentResponse;
import com.lms.enrollment.entity.Enrollment;
import com.lms.enrollment.service.EnrollmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enrollments")
@RequiredArgsConstructor
@Slf4j
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @PostMapping
    public ResponseEntity<EnrollmentResponse> createEnrollment(
            @Valid @RequestBody EnrollmentRequest request) {
        log.info("POST /enrollments - Création d'une inscription");
        EnrollmentResponse response = enrollmentService.createEnrollment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EnrollmentResponse> getEnrollmentById(@PathVariable Long id) {
        log.info("GET /enrollments/{} - Récupération par ID", id);
        return ResponseEntity.ok(enrollmentService.getEnrollmentById(id));
    }

    @GetMapping("/{id}/details")
    public ResponseEntity<EnrollmentResponse> getEnrollmentWithDetails(@PathVariable Long id) {
        log.info("GET /enrollments/{}/details - Récupération avec détails", id);
        return ResponseEntity.ok(enrollmentService.getEnrollmentWithDetails(id));
    }

    @GetMapping
    public ResponseEntity<List<EnrollmentResponse>> getAllEnrollments() {
        log.info("GET /enrollments - Récupération de toutes les inscriptions");
        return ResponseEntity.ok(enrollmentService.getAllEnrollments());
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<EnrollmentResponse>> getEnrollmentsByStudent(
            @PathVariable Long studentId) {
        log.info("GET /enrollments/student/{} - Récupération par étudiant", studentId);
        return ResponseEntity.ok(enrollmentService.getEnrollmentsByStudent(studentId));
    }

    @GetMapping("/student/{studentId}/details")
    public ResponseEntity<List<EnrollmentResponse>> getEnrollmentsByStudentWithDetails(
            @PathVariable Long studentId) {
        log.info("GET /enrollments/student/{}/details - Récupération par étudiant avec détails", studentId);
        return ResponseEntity.ok(enrollmentService.getEnrollmentsByStudentWithDetails(studentId));
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<EnrollmentResponse>> getEnrollmentsByCourse(
            @PathVariable Long courseId) {
        log.info("GET /enrollments/course/{} - Récupération par cours", courseId);
        return ResponseEntity.ok(enrollmentService.getEnrollmentsByCourse(courseId));
    }

    @GetMapping("/course/{courseId}/details")
    public ResponseEntity<List<EnrollmentResponse>> getEnrollmentsByCourseWithDetails(
            @PathVariable Long courseId) {
        log.info("GET /enrollments/course/{}/details - Récupération par cours avec détails", courseId);
        return ResponseEntity.ok(enrollmentService.getEnrollmentsByCourseWithDetails(courseId));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<EnrollmentResponse> updateStatus(
            @PathVariable Long id,
            @RequestParam Enrollment.EnrollmentStatus status) {
        log.info("PATCH /enrollments/{}/status - Mise à jour statut vers {}", id, status);
        return ResponseEntity.ok(enrollmentService.updateEnrollmentStatus(id, status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEnrollment(@PathVariable Long id) {
        log.info("DELETE /enrollments/{} - Suppression", id);
        enrollmentService.deleteEnrollment(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/exists")
    public ResponseEntity<Boolean> checkEnrollmentExists(
            @RequestParam Long studentId,
            @RequestParam Long courseId) {
        log.info("GET /enrollments/exists - Vérification existence: étudiant {} cours {}", 
                studentId, courseId);
        return ResponseEntity.ok(enrollmentService.enrollmentExists(studentId, courseId));
    }

    @GetMapping("/course/{courseId}/count")
    public ResponseEntity<Long> countActiveEnrollments(@PathVariable Long courseId) {
        log.info("GET /enrollments/course/{}/count - Comptage inscriptions actives", courseId);
        return ResponseEntity.ok(enrollmentService.countActiveEnrollmentsByCourse(courseId));
    }
}
