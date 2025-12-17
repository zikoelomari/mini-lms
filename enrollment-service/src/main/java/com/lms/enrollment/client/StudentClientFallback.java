package com.lms.enrollment.client;

import com.lms.enrollment.dto.StudentDTO;
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
                .email("service-indisponible@lms.com")
                .status("UNKNOWN")
                .build();
    }

    @Override
    public Boolean studentExists(Long id) {
        log.warn("Fallback activé pour studentExists({})", id);
        // En cas de fallback, on assume que l'étudiant existe pour ne pas bloquer
        // Dans un vrai système, on pourrait avoir une cache locale
        return true;
    }
}
