package com.lms.grade.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "grades",
        uniqueConstraints = @UniqueConstraint(columnNames = {"enrollment_id", "grade_type"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Grade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "enrollment_id", nullable = false)
    @NotNull(message = "L'ID de l'inscription est obligatoire")
    private Long enrollmentId;

    @Column(name = "student_id", nullable = false)
    private Long studentId;

    @Column(name = "course_id", nullable = false)
    private Long courseId;

    // RG-02-NOTE: Note entre 0 et 20
    @DecimalMin(value = "0.0", message = "La note doit être supérieure ou égale à 0")
    @DecimalMax(value = "20.0", message = "La note doit être inférieure ou égale à 20")
    @NotNull(message = "La note est obligatoire")
    @Column(name = "grade_value")  // ← AJOUTEZ CETTE LIGNE (value est un mot réservé SQL)
    private Double value;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private GradeType gradeType = GradeType.EXAM;

    private Double coefficient;

    @Column(name = "grade_comment")  // ← AJOUTEZ AUSSI (comment est aussi réservé)
    private String comment;

    @Column(name = "graded_by")
    private String gradedBy;

    @Column(name = "graded_at")
    private LocalDateTime gradedAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        gradedAt = LocalDateTime.now();
        if (coefficient == null) {
            coefficient = 1.0;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum GradeType {
        EXAM,
        MIDTERM,
        QUIZ,
        HOMEWORK,
        PROJECT,
        PARTICIPATION,
        LAB
    }

    public String getLetterGrade() {
        if (value >= 16) return "A";
        if (value >= 14) return "B";
        if (value >= 12) return "C";
        if (value >= 10) return "D";
        return "F";
    }

    public boolean isPassing() {
        return value >= 10.0;
    }
}