package com.lms.course.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CourseNotFoundException extends RuntimeException {

    public CourseNotFoundException(Long id) {
        super("Cours non trouvé avec l'ID: " + id);
    }

    public CourseNotFoundException(String courseCode) {
        super("Cours non trouvé avec le code: " + courseCode);
    }
}
