package com.markvarga21.studentmanager.service;

import com.markvarga21.studentmanager.dto.StudentDto;
import org.springframework.data.domain.Page;

import java.util.Optional;

/**
 * An interface which contains essential methods
 * for manipulating student information.
 */
public interface StudentService {
    /**
     * Retrieves all the students from the database.
     *
     * @param page The page number.
     * @param size The number of elements in a single page.
     * @return All the students stored in a {@code Page}.
     */
    Page<StudentDto> getAllStudents(Integer page, Integer size);

    /**
     * Validates and then persists a student in the database.
     *
     * @param studentDto The student itself.
     * @param username The username of the user who created the student.
     * @param roles The concatenated {@code String} roles of the user who created the student.
     * @return The newly modified student.
     */
    StudentDto createStudent(
            StudentDto studentDto,
            String username,
            String roles
    );

    /**
     * Retrieves a student from the database using its ID.
     *
     * @param id The identifier of the student we want to retrieve.
     * @return The found student.
     */
    StudentDto getStudentById(Long id);

    /**
     * Modifies the student given its id.
     *
     * @param studentDto The student itself.
     * @param userId The id of the student we want to modify.
     * @return The newly modified student.
     */
    StudentDto modifyStudentById(StudentDto studentDto, Long userId);

    /**
     * Deletes a student by its ID.
     *
     * @param id The identifier used for deleting a student.
     * @return The recently deleted student's DTO.
     */
    StudentDto deleteStudentById(Long id);

    /**
     * Sets the validity of a student.
     *
     * @param studentId The id of the student.
     * @param valid {@code true} if the passport is valid,
     * {@code false} otherwise.
     * @return A {@code String} containing a feedback of the operation.
     */
    String setValidity(
            Long studentId,
            boolean valid
    );

    /**
     * Checks if the passport number is valid or not.
     *
     * @param passportNumber The passport number to check.
     * @return {@code true} if the passport number is valid, {@code false} otherwise.
     */
    boolean validPassportNumber(String passportNumber);

    /**
     * Retrieves a student by its username.
     *
     * @param username The username of the student.
     * @return The student's DTO.
     */
    Optional<StudentDto> getStudentByUsername(String username);

    /**
     * Retrieves a student by its first and last name.
     *
     * @param firstName The first name of the student.
     * @param lastName The last name of the student.
     * @return The student's DTO.
     */
    Optional<StudentDto> getStudentByFirstAndLastName(String firstName, String lastName);
}
