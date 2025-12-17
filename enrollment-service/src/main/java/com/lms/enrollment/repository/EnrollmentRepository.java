package com.lms.enrollment.repository;

import com.lms.enrollment.entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    List<Enrollment> findByStudentId(Long studentId);

    List<Enrollment> findByCourseId(Long courseId);

    Optional<Enrollment> findByStudentIdAndCourseId(Long studentId, Long courseId);

    boolean existsByStudentIdAndCourseId(Long studentId, Long courseId);

    List<Enrollment> findByStudentIdAndStatus(Long studentId, Enrollment.EnrollmentStatus status);

    List<Enrollment> findByCourseIdAndStatus(Long courseId, Enrollment.EnrollmentStatus status);

    long countByCourseIdAndStatus(Long courseId, Enrollment.EnrollmentStatus status);
}
