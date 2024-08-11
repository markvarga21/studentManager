package com.markvarga21.studentmanager.repository;

import com.markvarga21.studentmanager.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * The repository containing the students in the application.
 */
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    /**
     * Fetches a student by the passport number.
     *
     * @param passportNumber The students passport number.
     * @return The found student.
     */
    Optional<Student> findStudentByPassportNumber(String passportNumber);

    /**
     * Fetches a student by the first name and last name.
     *
     * @param firstName The first name of the student.
     * @param lastName The last name of the student.
     * @return The found student.
     */
    Optional<Student> findStudentByFirstNameAndLastName(String firstName, String lastName);
}
