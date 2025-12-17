package com.lms.grade.client;

import com.lms.grade.dto.EnrollmentDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
@Slf4j
public class EnrollmentClientFallback implements EnrollmentClient {

    @Override
    public EnrollmentDTO getEnrollmentById(Long id) {
        log.warn("Fallback activé pour getEnrollmentById({})", id);
        return EnrollmentDTO.builder()
                .id(id)
                .status("UNKNOWN")
                .build();
    }

    @Override
    public List<EnrollmentDTO> getEnrollmentsByStudent(Long studentId) {
        log.warn("Fallback activé pour getEnrollmentsByStudent({})", studentId);
        return Collections.emptyList();
    }

    @Override
    public List<EnrollmentDTO> getEnrollmentsByCourse(Long courseId) {
        log.warn("Fallback activé pour getEnrollmentsByCourse({})", courseId);
        return Collections.emptyList();
    }

    @Override
    public Boolean enrollmentExists(Long studentId, Long courseId) {
        log.warn("Fallback activé pour enrollmentExists({}, {})", studentId, courseId);
        return true;
    }
}
