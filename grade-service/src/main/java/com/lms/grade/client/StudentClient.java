package com.lms.grade.client;

import com.lms.grade.dto.StudentDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "student-service", fallback = StudentClientFallback.class)
public interface StudentClient {

    @GetMapping("/students/{id}")
    StudentDTO getStudentById(@PathVariable("id") Long id);
}
