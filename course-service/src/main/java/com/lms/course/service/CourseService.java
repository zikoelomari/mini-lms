package com.lms.course.service;

import com.lms.course.dto.CourseRequest;
import com.lms.course.dto.CourseResponse;
import com.lms.course.entity.Course;
import com.lms.course.exception.CourseCodeAlreadyExistsException;
import com.lms.course.exception.CourseFullException;
import com.lms.course.exception.CourseNotFoundException;
import com.lms.course.repository.CourseRepository;
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
public class CourseService {

    private final CourseRepository courseRepository;

    public CourseResponse createCourse(CourseRequest request) {
        log.info("Création d'un nouveau cours: {}", request.getTitle());

        if (courseRepository.existsByCourseCode(request.getCourseCode())) {
            throw new CourseCodeAlreadyExistsException(request.getCourseCode());
        }

        Course course = Course.builder()
                .courseCode(request.getCourseCode())
                .title(request.getTitle())
                .description(request.getDescription())
                .professorName(request.getProfessorName())
                .credits(request.getCredits())
                .maxCapacity(request.getMaxCapacity())
                .currentEnrollment(0)
                .status(Course.CourseStatus.ACTIVE)
                .build();

        Course savedCourse = courseRepository.save(course);
        log.info("Cours créé avec succès: {}", savedCourse.getCourseCode());

        return CourseResponse.fromEntity(savedCourse);
    }

    @Transactional(readOnly = true)
    public CourseResponse getCourseById(Long id) {
        log.info("Recherche du cours avec l'ID: {}", id);
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new CourseNotFoundException(id));
        return CourseResponse.fromEntity(course);
    }

    @Transactional(readOnly = true)
    public CourseResponse getCourseByCode(String courseCode) {
        log.info("Recherche du cours avec le code: {}", courseCode);
        Course course = courseRepository.findByCourseCode(courseCode)
                .orElseThrow(() -> new CourseNotFoundException(courseCode));
        return CourseResponse.fromEntity(course);
    }

    @Transactional(readOnly = true)
    public List<CourseResponse> getAllCourses() {
        log.info("Récupération de tous les cours");
        return courseRepository.findAll().stream()
                .map(CourseResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CourseResponse> getCoursesByProfessor(String professorName) {
        log.info("Récupération des cours du professeur: {}", professorName);
        return courseRepository.findByProfessorName(professorName).stream()
                .map(CourseResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CourseResponse> getActiveCourses() {
        log.info("Récupération des cours actifs");
        return courseRepository.findByStatus(Course.CourseStatus.ACTIVE).stream()
                .map(CourseResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public CourseResponse updateCourse(Long id, CourseRequest request) {
        log.info("Mise à jour du cours avec l'ID: {}", id);

        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new CourseNotFoundException(id));

        // Vérifier si le nouveau code existe déjà pour un autre cours
        if (!course.getCourseCode().equals(request.getCourseCode()) 
                && courseRepository.existsByCourseCode(request.getCourseCode())) {
            throw new CourseCodeAlreadyExistsException(request.getCourseCode());
        }

        course.setCourseCode(request.getCourseCode());
        course.setTitle(request.getTitle());
        course.setDescription(request.getDescription());
        course.setProfessorName(request.getProfessorName());
        course.setCredits(request.getCredits());
        course.setMaxCapacity(request.getMaxCapacity());

        Course updatedCourse = courseRepository.save(course);
        log.info("Cours mis à jour avec succès: {}", updatedCourse.getCourseCode());

        return CourseResponse.fromEntity(updatedCourse);
    }

    public void deleteCourse(Long id) {
        log.info("Suppression du cours avec l'ID: {}", id);

        if (!courseRepository.existsById(id)) {
            throw new CourseNotFoundException(id);
        }

        courseRepository.deleteById(id);
        log.info("Cours supprimé avec succès");
    }

    public CourseResponse updateCourseStatus(Long id, Course.CourseStatus status) {
        log.info("Mise à jour du statut du cours {} vers {}", id, status);

        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new CourseNotFoundException(id));

        course.setStatus(status);
        Course updatedCourse = courseRepository.save(course);

        return CourseResponse.fromEntity(updatedCourse);
    }

    @Transactional(readOnly = true)
    public boolean courseExists(Long id) {
        return courseRepository.existsById(id);
    }

    @Transactional(readOnly = true)
    public boolean hasAvailableSpots(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException(courseId));
        return course.hasAvailableSpots();
    }

    public void incrementEnrollment(Long courseId) {
        log.info("Incrémentation du nombre d'inscriptions pour le cours: {}", courseId);

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException(courseId));

        if (!course.hasAvailableSpots()) {
            throw new CourseFullException(courseId);
        }

        course.incrementEnrollment();
        courseRepository.save(course);
    }

    public void decrementEnrollment(Long courseId) {
        log.info("Décrémentation du nombre d'inscriptions pour le cours: {}", courseId);

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException(courseId));

        course.decrementEnrollment();
        courseRepository.save(course);
    }
}
