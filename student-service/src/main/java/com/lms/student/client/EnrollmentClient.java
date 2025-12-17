package com.lms.student.client;

import com.lms.student.dto.EnrollmentDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "enrollment-service", fallback = EnrollmentClientFallback.class)
public interface EnrollmentClient {

    @GetMapping("/enrollments/student/{studentId}")
    List<EnrollmentDTO> getEnrollmentsByStudent(@PathVariable("studentId") Long studentId);
}
