package com.lms.enrollment.client;

import com.lms.enrollment.dto.CourseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "course-service", fallback = CourseClientFallback.class)
public interface CourseClient {

    @GetMapping("/courses/{id}")
    CourseDTO getCourseById(@PathVariable("id") Long id);

    @GetMapping("/courses/{id}/exists")
    Boolean courseExists(@PathVariable("id") Long id);

    @GetMapping("/courses/{id}/available")
    Boolean hasAvailableSpots(@PathVariable("id") Long id);

    @PostMapping("/courses/{id}/increment-enrollment")
    void incrementEnrollment(@PathVariable("id") Long id);

    @PostMapping("/courses/{id}/decrement-enrollment")
    void decrementEnrollment(@PathVariable("id") Long id);
}
