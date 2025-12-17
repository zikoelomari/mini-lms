package com.lms.course.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class CourseFullException extends RuntimeException {

    public CourseFullException(Long courseId) {
        super("Le cours avec l'ID " + courseId + " est complet. Aucune place disponible.");
    }

    public CourseFullException(String courseCode) {
        super("Le cours '" + courseCode + "' est complet. Aucune place disponible.");
    }
}
