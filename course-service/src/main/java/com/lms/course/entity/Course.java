package com.lms.course.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "courses")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    @NotBlank(message = "Le code du cours est obligatoire")
    private String courseCode;

    @NotBlank(message = "Le titre est obligatoire")
    private String title;

    @Column(length = 2000)
    private String description;

    @NotBlank(message = "Le nom du professeur est obligatoire")
    private String professorName;

    @Positive(message = "Le nombre de crédits doit être positif")
    @Min(value = 1, message = "Le nombre de crédits minimum est 1")
    @Max(value = 10, message = "Le nombre de crédits maximum est 10")
    private Integer credits;

    @Positive(message = "La capacité maximale doit être positive")
    private Integer maxCapacity;

    private Integer currentEnrollment;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private CourseStatus status = CourseStatus.ACTIVE;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (currentEnrollment == null) {
            currentEnrollment = 0;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum CourseStatus {
        ACTIVE, INACTIVE, FULL, CANCELLED
    }

    public boolean hasAvailableSpots() {
        return maxCapacity == null || currentEnrollment < maxCapacity;
    }

    public void incrementEnrollment() {
        this.currentEnrollment++;
        if (maxCapacity != null && currentEnrollment >= maxCapacity) {
            this.status = CourseStatus.FULL;
        }
    }

    public void decrementEnrollment() {
        if (this.currentEnrollment > 0) {
            this.currentEnrollment--;
            if (this.status == CourseStatus.FULL) {
                this.status = CourseStatus.ACTIVE;
            }
        }
    }
}
