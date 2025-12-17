package com.lms.enrollment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class EnrollmentAlreadyExistsException extends RuntimeException {

    public EnrollmentAlreadyExistsException(Long studentId, Long courseId) {
        super("L'étudiant " + studentId + " est déjà inscrit au cours " + courseId);
    }
}
