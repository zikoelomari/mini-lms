package com.lms.student.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class StudentNotFoundException extends RuntimeException {

    public StudentNotFoundException(Long id) {
        super("Étudiant non trouvé avec l'ID: " + id);
    }

    public StudentNotFoundException(String studentNumber) {
        super("Étudiant non trouvé avec le numéro: " + studentNumber);
    }
}
