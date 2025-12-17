package com.lms.grade.client;

import com.lms.grade.dto.EnrollmentDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "enrollment-service", fallback = EnrollmentClientFallback.class)
public interface EnrollmentClient {

    @GetMapping("/enrollments/{id}")
    EnrollmentDTO getEnrollmentById(@PathVariable("id") Long id);

    @GetMapping("/enrollments/student/{studentId}")
    List<EnrollmentDTO> getEnrollmentsByStudent(@PathVariable("studentId") Long studentId);

    @GetMapping("/enrollments/course/{courseId}")
    List<EnrollmentDTO> getEnrollmentsByCourse(@PathVariable("courseId") Long courseId);

    @GetMapping("/enrollments/exists")
    Boolean enrollmentExists(@RequestParam("studentId") Long studentId, 
                            @RequestParam("courseId") Long courseId);
}
