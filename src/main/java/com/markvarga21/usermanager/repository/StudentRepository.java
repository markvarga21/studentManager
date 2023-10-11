package com.markvarga21.usermanager.repository;

import com.markvarga21.usermanager.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * The repository holding the student in the application.
 */
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    /**
     * A method which is used to verify whether a student already exists
     * in the database with the same first name and last name or not.
     *
     * @param firstName The first name of the student.
     * @param lastName The last name of the student.
     * @return An optional {@code Student} object.
     */
    Optional<Student> findStudentByFirstNameAndLastName(
            String firstName,
            String lastName
    );
}
