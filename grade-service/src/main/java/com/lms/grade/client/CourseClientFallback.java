package com.lms.grade.client;

import com.lms.grade.dto.CourseDTO;
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
}
