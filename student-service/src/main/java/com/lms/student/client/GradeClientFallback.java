package com.lms.student.client;

import com.lms.student.dto.GradeDTO;
import com.lms.student.dto.GradeStatisticsDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
@Slf4j
public class GradeClientFallback implements GradeClient {

    @Override
    public List<GradeDTO> getGradesByStudent(Long studentId) {
        log.warn("Fallback activé pour getGradesByStudent({}) - Service de notes indisponible", studentId);
        // RG-03-AGREG: Retourner une liste vide si le service est HS
        return Collections.emptyList();
    }

    @Override
    public GradeStatisticsDTO getStudentStatistics(Long studentId) {
        log.warn("Fallback activé pour getStudentStatistics({}) - Service de notes indisponible", studentId);
        return GradeStatisticsDTO.builder()
                .entityId(studentId)
                .entityType("STUDENT")
                .average(null)
                .totalGrades(0L)
                .passingGrades(0L)
                .failingGrades(0L)
                .passRate(0.0)
                .overallGrade("N/A")
                .build();
    }

    @Override
    public Double getStudentAverage(Long studentId) {
        log.warn("Fallback activé pour getStudentAverage({}) - Service de notes indisponible", studentId);
        return null;
    }
}
