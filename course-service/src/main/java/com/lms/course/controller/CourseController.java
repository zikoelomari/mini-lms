package com.lms.course.controller;

import com.lms.course.dto.CourseRequest;
import com.lms.course.dto.CourseResponse;
import com.lms.course.entity.Course;
import com.lms.course.service.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
@Slf4j
public class CourseController {

    private final CourseService courseService;

    @PostMapping
    public ResponseEntity<CourseResponse> createCourse(@Valid @RequestBody CourseRequest request) {
        log.info("POST /courses - Création d'un cours");
        CourseResponse response = courseService.createCourse(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseResponse> getCourseById(@PathVariable Long id) {
        log.info("GET /courses/{} - Récupération par ID", id);
        return ResponseEntity.ok(courseService.getCourseById(id));
    }

    @GetMapping("/code/{courseCode}")
    public ResponseEntity<CourseResponse> getCourseByCode(@PathVariable String courseCode) {
        log.info("GET /courses/code/{} - Récupération par code", courseCode);
        return ResponseEntity.ok(courseService.getCourseByCode(courseCode));
    }

    @GetMapping
    public ResponseEntity<List<CourseResponse>> getAllCourses() {
        log.info("GET /courses - Récupération de tous les cours");
        return ResponseEntity.ok(courseService.getAllCourses());
    }

    @GetMapping("/professor/{professorName}")
    public ResponseEntity<List<CourseResponse>> getCoursesByProfessor(@PathVariable String professorName) {
        log.info("GET /courses/professor/{} - Récupération par professeur", professorName);
        return ResponseEntity.ok(courseService.getCoursesByProfessor(professorName));
    }

    @GetMapping("/active")
    public ResponseEntity<List<CourseResponse>> getActiveCourses() {
        log.info("GET /courses/active - Récupération des cours actifs");
        return ResponseEntity.ok(courseService.getActiveCourses());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseResponse> updateCourse(
            @PathVariable Long id,
            @Valid @RequestBody CourseRequest request) {
        log.info("PUT /courses/{} - Mise à jour", id);
        return ResponseEntity.ok(courseService.updateCourse(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        log.info("DELETE /courses/{} - Suppression", id);
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<CourseResponse> updateStatus(
            @PathVariable Long id,
            @RequestParam Course.CourseStatus status) {
        log.info("PATCH /courses/{}/status - Mise à jour statut vers {}", id, status);
        return ResponseEntity.ok(courseService.updateCourseStatus(id, status));
    }

    @GetMapping("/{id}/exists")
    public ResponseEntity<Boolean> checkCourseExists(@PathVariable Long id) {
        log.info("GET /courses/{}/exists - Vérification existence", id);
        return ResponseEntity.ok(courseService.courseExists(id));
    }

    @GetMapping("/{id}/available")
    public ResponseEntity<Boolean> checkAvailableSpots(@PathVariable Long id) {
        log.info("GET /courses/{}/available - Vérification places disponibles", id);
        return ResponseEntity.ok(courseService.hasAvailableSpots(id));
    }

    @PostMapping("/{id}/increment-enrollment")
    public ResponseEntity<Void> incrementEnrollment(@PathVariable Long id) {
        log.info("POST /courses/{}/increment-enrollment", id);
        courseService.incrementEnrollment(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/decrement-enrollment")
    public ResponseEntity<Void> decrementEnrollment(@PathVariable Long id) {
        log.info("POST /courses/{}/decrement-enrollment", id);
        courseService.decrementEnrollment(id);
        return ResponseEntity.ok().build();
    }
}
