package com.lms.enrollment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class EnrollmentNotFoundException extends RuntimeException {

    public EnrollmentNotFoundException(Long id) {
        super("Inscription non trouvée avec l'ID: " + id);
    }

    public EnrollmentNotFoundException(Long studentId, Long courseId) {
        super("Inscription non trouvée pour l'étudiant " + studentId + " au cours " + courseId);
    }
}
