package com.lms.grade.client;

import com.lms.grade.dto.StudentDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class StudentClientFallback implements StudentClient {

    @Override
    public StudentDTO getStudentById(Long id) {
        log.warn("Fallback activé pour getStudentById({})", id);
        return StudentDTO.builder()
                .id(id)
                .firstName("Service")
                .lastName("Indisponible")
                .email("indisponible@lms.com")
                .status("UNKNOWN")
                .build();
    }
}
