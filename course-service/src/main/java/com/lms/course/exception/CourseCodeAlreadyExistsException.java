package com.lms.course.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class CourseCodeAlreadyExistsException extends RuntimeException {

    public CourseCodeAlreadyExistsException(String courseCode) {
        super("Un cours avec le code '" + courseCode + "' existe déjà");
    }
}
