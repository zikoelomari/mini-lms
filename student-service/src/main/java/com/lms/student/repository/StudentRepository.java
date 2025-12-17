package com.lms.student.repository;

import com.lms.student.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByStudentNumber(String studentNumber);

    Optional<Student> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByStudentNumber(String studentNumber);
}
