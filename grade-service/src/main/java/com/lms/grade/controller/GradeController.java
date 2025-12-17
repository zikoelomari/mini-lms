package com.lms.grade.controller;

import com.lms.grade.dto.GradeRequest;
import com.lms.grade.dto.GradeResponse;
import com.lms.grade.dto.GradeStatistics;
import com.lms.grade.service.GradeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/grades")
@RequiredArgsConstructor
@Slf4j
public class GradeController {

    private final GradeService gradeService;

    @PostMapping
    public ResponseEntity<GradeResponse> createGrade(@Valid @RequestBody GradeRequest request) {
        log.info("POST /grades - Création d'une note");
        GradeResponse response = gradeService.createGrade(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GradeResponse> getGradeById(@PathVariable Long id) {
        log.info("GET /grades/{} - Récupération par ID", id);
        return ResponseEntity.ok(gradeService.getGradeById(id));
    }

    @GetMapping("/{id}/details")
    public ResponseEntity<GradeResponse> getGradeWithDetails(@PathVariable Long id) {
        log.info("GET /grades/{}/details - Récupération avec détails", id);
        return ResponseEntity.ok(gradeService.getGradeWithDetails(id));
    }

    @GetMapping
    public ResponseEntity<List<GradeResponse>> getAllGrades() {
        log.info("GET /grades - Récupération de toutes les notes");
        return ResponseEntity.ok(gradeService.getAllGrades());
    }

    @GetMapping("/enrollment/{enrollmentId}")
    public ResponseEntity<List<GradeResponse>> getGradesByEnrollment(@PathVariable Long enrollmentId) {
        log.info("GET /grades/enrollment/{} - Récupération par inscription", enrollmentId);
        return ResponseEntity.ok(gradeService.getGradesByEnrollment(enrollmentId));
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<GradeResponse>> getGradesByStudent(@PathVariable Long studentId) {
        log.info("GET /grades/student/{} - Récupération par étudiant", studentId);
        return ResponseEntity.ok(gradeService.getGradesByStudent(studentId));
    }

    @GetMapping("/student/{studentId}/details")
    public ResponseEntity<List<GradeResponse>> getGradesByStudentWithDetails(
            @PathVariable Long studentId) {
        log.info("GET /grades/student/{}/details - Récupération par étudiant avec détails", studentId);
        return ResponseEntity.ok(gradeService.getGradesByStudentWithDetails(studentId));
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<GradeResponse>> getGradesByCourse(@PathVariable Long courseId) {
        log.info("GET /grades/course/{} - Récupération par cours", courseId);
        return ResponseEntity.ok(gradeService.getGradesByCourse(courseId));
    }

    @GetMapping("/course/{courseId}/details")
    public ResponseEntity<List<GradeResponse>> getGradesByCourseWithDetails(
            @PathVariable Long courseId) {
        log.info("GET /grades/course/{}/details - Récupération par cours avec détails", courseId);
        return ResponseEntity.ok(gradeService.getGradesByCourseWithDetails(courseId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GradeResponse> updateGrade(
            @PathVariable Long id,
            @Valid @RequestBody GradeRequest request) {
        log.info("PUT /grades/{} - Mise à jour", id);
        return ResponseEntity.ok(gradeService.updateGrade(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGrade(@PathVariable Long id) {
        log.info("DELETE /grades/{} - Suppression", id);
        gradeService.deleteGrade(id);
        return ResponseEntity.noContent().build();
    }

    // ========== STATISTIQUES ==========

    @GetMapping("/student/{studentId}/statistics")
    public ResponseEntity<GradeStatistics> getStudentStatistics(@PathVariable Long studentId) {
        log.info("GET /grades/student/{}/statistics - Statistiques étudiant", studentId);
        return ResponseEntity.ok(gradeService.getStudentStatistics(studentId));
    }

    @GetMapping("/course/{courseId}/statistics")
    public ResponseEntity<GradeStatistics> getCourseStatistics(@PathVariable Long courseId) {
        log.info("GET /grades/course/{}/statistics - Statistiques cours", courseId);
        return ResponseEntity.ok(gradeService.getCourseStatistics(courseId));
    }

    @GetMapping("/student/{studentId}/average")
    public ResponseEntity<Double> getStudentAverage(@PathVariable Long studentId) {
        log.info("GET /grades/student/{}/average - Moyenne étudiant", studentId);
        Double average = gradeService.getStudentAverage(studentId);
        return average != null 
                ? ResponseEntity.ok(average) 
                : ResponseEntity.noContent().build();
    }

    @GetMapping("/course/{courseId}/average")
    public ResponseEntity<Double> getCourseAverage(@PathVariable Long courseId) {
        log.info("GET /grades/course/{}/average - Moyenne cours", courseId);
        Double average = gradeService.getCourseAverage(courseId);
        return average != null 
                ? ResponseEntity.ok(average) 
                : ResponseEntity.noContent().build();
    }
}
