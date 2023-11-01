package com.markvarga21.studentmanager.repository;

import com.markvarga21.studentmanager.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * The repository holding the student in the application.
 */
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    /**
     * Finds a student by passport number.
     *
     * @param passportNumber The students passport number.
     * @return The student.
     */
    Optional<Student> findStudentByPassportNumber(String passportNumber);
}
