package com.lms.grade.repository;

import com.lms.grade.entity.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Long> {

    List<Grade> findByEnrollmentId(Long enrollmentId);

    List<Grade> findByStudentId(Long studentId);

    List<Grade> findByCourseId(Long courseId);

    Optional<Grade> findByEnrollmentIdAndGradeType(Long enrollmentId, Grade.GradeType gradeType);

    boolean existsByEnrollmentIdAndGradeType(Long enrollmentId, Grade.GradeType gradeType);

    // Statistiques pour un étudiant
    @Query("SELECT AVG(g.value) FROM Grade g WHERE g.studentId = :studentId")
    Double calculateAverageByStudent(@Param("studentId") Long studentId);

    @Query("SELECT MIN(g.value) FROM Grade g WHERE g.studentId = :studentId")
    Double findMinByStudent(@Param("studentId") Long studentId);

    @Query("SELECT MAX(g.value) FROM Grade g WHERE g.studentId = :studentId")
    Double findMaxByStudent(@Param("studentId") Long studentId);

    @Query("SELECT COUNT(g) FROM Grade g WHERE g.studentId = :studentId AND g.value >= 10")
    Long countPassingByStudent(@Param("studentId") Long studentId);

    // Statistiques pour un cours
    @Query("SELECT AVG(g.value) FROM Grade g WHERE g.courseId = :courseId")
    Double calculateAverageByCourse(@Param("courseId") Long courseId);

    @Query("SELECT MIN(g.value) FROM Grade g WHERE g.courseId = :courseId")
    Double findMinByCourse(@Param("courseId") Long courseId);

    @Query("SELECT MAX(g.value) FROM Grade g WHERE g.courseId = :courseId")
    Double findMaxByCourse(@Param("courseId") Long courseId);

    @Query("SELECT COUNT(g) FROM Grade g WHERE g.courseId = :courseId AND g.value >= 10")
    Long countPassingByCourse(@Param("courseId") Long courseId);

    // Moyenne pondérée pour un étudiant
    @Query("SELECT SUM(g.value * g.coefficient) / SUM(g.coefficient) FROM Grade g WHERE g.studentId = :studentId")
    Double calculateWeightedAverageByStudent(@Param("studentId") Long studentId);

    // Moyenne pondérée pour un cours
    @Query("SELECT SUM(g.value * g.coefficient) / SUM(g.coefficient) FROM Grade g WHERE g.courseId = :courseId")
    Double calculateWeightedAverageByCourse(@Param("courseId") Long courseId);
}
