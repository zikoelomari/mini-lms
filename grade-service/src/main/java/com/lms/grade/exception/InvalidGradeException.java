package com.lms.grade.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidGradeException extends RuntimeException {

    public InvalidGradeException(String message) {
        super(message);
    }

    public InvalidGradeException(Double value) {
        super("Note invalide: " + value + ". La note doit être comprise entre 0 et 20.");
    }
}
