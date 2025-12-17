package com.lms.grade.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class GradeNotFoundException extends RuntimeException {

    public GradeNotFoundException(Long id) {
        super("Note non trouvée avec l'ID: " + id);
    }
}
