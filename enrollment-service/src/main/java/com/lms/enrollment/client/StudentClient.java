package com.lms.enrollment.client;

import com.lms.enrollment.dto.StudentDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "student-service", fallback = StudentClientFallback.class)
public interface StudentClient {

    @GetMapping("/students/{id}")
    StudentDTO getStudentById(@PathVariable("id") Long id);

    @GetMapping("/students/{id}/exists")
    Boolean studentExists(@PathVariable("id") Long id);
}
