package com.lms.student.client;

import com.lms.student.dto.GradeDTO;
import com.lms.student.dto.GradeStatisticsDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "grade-service", fallback = GradeClientFallback.class)
public interface GradeClient {

    @GetMapping("/grades/student/{studentId}")
    List<GradeDTO> getGradesByStudent(@PathVariable("studentId") Long studentId);

    @GetMapping("/grades/student/{studentId}/statistics")
    GradeStatisticsDTO getStudentStatistics(@PathVariable("studentId") Long studentId);

    @GetMapping("/grades/student/{studentId}/average")
    Double getStudentAverage(@PathVariable("studentId") Long studentId);
}
