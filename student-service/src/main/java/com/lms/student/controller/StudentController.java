package com.lms.student.controller;

import com.lms.student.dto.StudentFullProfile;
import com.lms.student.dto.StudentRequest;
import com.lms.student.dto.StudentResponse;
import com.lms.student.entity.Student;
import com.lms.student.service.StudentProfileService;
import com.lms.student.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
@Slf4j
public class StudentController {

    private final StudentService studentService;
    private final StudentProfileService studentProfileService;

    @PostMapping
    public ResponseEntity<StudentResponse> createStudent(@Valid @RequestBody StudentRequest request) {
        log.info("POST /students - Création d'un étudiant");
        StudentResponse response = studentService.createStudent(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentResponse> getStudentById(@PathVariable Long id) {
        log.info("GET /students/{} - Récupération par ID", id);
        return ResponseEntity.ok(studentService.getStudentById(id));
    }

    /**
     * API de synthèse - Attente A du cahier des charges
     * Fiche complète d'un étudiant (Infos + Cours + Notes)
     */
    @GetMapping("/{id}/full-profile")
    public ResponseEntity<StudentFullProfile> getStudentFullProfile(@PathVariable Long id) {
        log.info("GET /students/{}/full-profile - Profil complet", id);
        return ResponseEntity.ok(studentProfileService.getFullProfile(id));
    }

    @GetMapping("/number/{studentNumber}")
    public ResponseEntity<StudentResponse> getStudentByNumber(@PathVariable String studentNumber) {
        log.info("GET /students/number/{} - Récupération par numéro", studentNumber);
        return ResponseEntity.ok(studentService.getStudentByNumber(studentNumber));
    }

    @GetMapping
    public ResponseEntity<List<StudentResponse>> getAllStudents() {
        log.info("GET /students - Récupération de tous les étudiants");
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentResponse> updateStudent(
            @PathVariable Long id,
            @Valid @RequestBody StudentRequest request) {
        log.info("PUT /students/{} - Mise à jour", id);
        return ResponseEntity.ok(studentService.updateStudent(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        log.info("DELETE /students/{} - Suppression", id);
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<StudentResponse> updateStatus(
            @PathVariable Long id,
            @RequestParam Student.StudentStatus status) {
        log.info("PATCH /students/{}/status - Mise à jour statut vers {}", id, status);
        return ResponseEntity.ok(studentService.updateStudentStatus(id, status));
    }

    @GetMapping("/{id}/exists")
    public ResponseEntity<Boolean> checkStudentExists(@PathVariable Long id) {
        log.info("GET /students/{}/exists - Vérification existence", id);
        return ResponseEntity.ok(studentService.studentExists(id));
    }
}
