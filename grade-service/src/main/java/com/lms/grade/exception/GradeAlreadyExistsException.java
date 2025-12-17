package com.lms.grade.exception;

import com.lms.grade.entity.Grade;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class GradeAlreadyExistsException extends RuntimeException {

    public GradeAlreadyExistsException(Long enrollmentId, Grade.GradeType gradeType) {
        super("Une note de type " + gradeType + " existe déjà pour l'inscription " + enrollmentId);
    }
}
