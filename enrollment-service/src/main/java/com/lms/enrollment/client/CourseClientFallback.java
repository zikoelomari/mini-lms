package com.lms.enrollment.client;

import com.lms.enrollment.dto.CourseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CourseClientFallback implements CourseClient {

    @Override
    public CourseDTO getCourseById(Long id) {
        log.warn("Fallback activé pour getCourseById({})", id);
        return CourseDTO.builder()
                .id(id)
                .courseCode("UNKNOWN")
                .title("Service Indisponible")
                .professorName("N/A")
                .status("UNKNOWN")
                .build();
    }

    @Override
    public Boolean courseExists(Long id) {
        log.warn("Fallback activé pour courseExists({})", id);
        return true;
    }

    @Override
    public Boolean hasAvailableSpots(Long id) {
        log.warn("Fallback activé pour hasAvailableSpots({})", id);
        return true;
    }

    @Override
    public void incrementEnrollment(Long id) {
        log.warn("Fallback activé pour incrementEnrollment({})", id);
        // Opération ignorée en cas de fallback
    }

    @Override
    public void decrementEnrollment(Long id) {
        log.warn("Fallback activé pour decrementEnrollment({})", id);
        // Opération ignorée en cas de fallback
    }
}
