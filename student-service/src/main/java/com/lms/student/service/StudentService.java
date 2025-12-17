package com.lms.student.service;

import com.lms.student.dto.StudentRequest;
import com.lms.student.dto.StudentResponse;
import com.lms.student.entity.Student;
import com.lms.student.exception.EmailAlreadyExistsException;
import com.lms.student.exception.StudentNotFoundException;
import com.lms.student.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentResponse createStudent(StudentRequest request) {
        log.info("Création d'un nouvel étudiant: {} {}", request.getFirstName(), request.getLastName());

        // Vérifier si l'email existe déjà
        if (studentRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException(request.getEmail());
        }

        // Générer un numéro étudiant unique
        String studentNumber = generateStudentNumber();

        Student student = Student.builder()
                .studentNumber(studentNumber)
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .dateOfBirth(request.getDateOfBirth())
                .phoneNumber(request.getPhoneNumber())
                .address(request.getAddress())
                .status(Student.StudentStatus.ACTIVE)
                .build();

        Student savedStudent = studentRepository.save(student);
        log.info("Étudiant créé avec succès: {}", savedStudent.getStudentNumber());

        return StudentResponse.fromEntity(savedStudent);
    }

    @Transactional(readOnly = true)
    public StudentResponse getStudentById(Long id) {
        log.info("Recherche de l'étudiant avec l'ID: {}", id);
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(id));
        return StudentResponse.fromEntity(student);
    }

    @Transactional(readOnly = true)
    public StudentResponse getStudentByNumber(String studentNumber) {
        log.info("Recherche de l'étudiant avec le numéro: {}", studentNumber);
        Student student = studentRepository.findByStudentNumber(studentNumber)
                .orElseThrow(() -> new StudentNotFoundException(studentNumber));
        return StudentResponse.fromEntity(student);
    }

    @Transactional(readOnly = true)
    public List<StudentResponse> getAllStudents() {
        log.info("Récupération de tous les étudiants");
        return studentRepository.findAll().stream()
                .map(StudentResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public StudentResponse updateStudent(Long id, StudentRequest request) {
        log.info("Mise à jour de l'étudiant avec l'ID: {}", id);

        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(id));

        // Vérifier si le nouvel email existe déjà pour un autre étudiant
        if (!student.getEmail().equals(request.getEmail()) 
                && studentRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException(request.getEmail());
        }

        student.setFirstName(request.getFirstName());
        student.setLastName(request.getLastName());
        student.setEmail(request.getEmail());
        student.setDateOfBirth(request.getDateOfBirth());
        student.setPhoneNumber(request.getPhoneNumber());
        student.setAddress(request.getAddress());

        Student updatedStudent = studentRepository.save(student);
        log.info("Étudiant mis à jour avec succès: {}", updatedStudent.getStudentNumber());

        return StudentResponse.fromEntity(updatedStudent);
    }

    public void deleteStudent(Long id) {
        log.info("Suppression de l'étudiant avec l'ID: {}", id);

        if (!studentRepository.existsById(id)) {
            throw new StudentNotFoundException(id);
        }

        studentRepository.deleteById(id);
        log.info("Étudiant supprimé avec succès");
    }

    public StudentResponse updateStudentStatus(Long id, Student.StudentStatus status) {
        log.info("Mise à jour du statut de l'étudiant {} vers {}", id, status);

        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(id));

        student.setStatus(status);
        Student updatedStudent = studentRepository.save(student);

        return StudentResponse.fromEntity(updatedStudent);
    }

    @Transactional(readOnly = true)
    public boolean studentExists(Long id) {
        return studentRepository.existsById(id);
    }

    private String generateStudentNumber() {
        String number;
        do {
            number = "STU" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        } while (studentRepository.existsByStudentNumber(number));
        return number;
    }
}
