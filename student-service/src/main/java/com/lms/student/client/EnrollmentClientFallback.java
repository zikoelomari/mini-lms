package com.lms.student.client;

import com.lms.student.dto.EnrollmentDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
@Slf4j
public class EnrollmentClientFallback implements EnrollmentClient {

    @Override
    public List<EnrollmentDTO> getEnrollmentsByStudent(Long studentId) {
        log.warn("Fallback activé pour getEnrollmentsByStudent({})", studentId);
        return Collections.emptyList();
    }
}
