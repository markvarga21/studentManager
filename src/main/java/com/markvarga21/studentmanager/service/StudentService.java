package com.markvarga21.studentmanager.service;

import com.markvarga21.studentmanager.dto.StudentDto;

import java.util.List;

/**
 * An interface which contains essential methods
 * for manipulating student information.
 */
public interface StudentService {
    /**
     * A method used for retrieving all the students from the application.
     *
     * @return All to students stored in a {@code List}.
     */
    List<StudentDto> getAllStudents();

    /**
     * Validates and then persists a student in the database.
     *
     * @param studentDto The student itself.
     * @return The newly modified student.
     */
    StudentDto createStudent(StudentDto studentDto);

    /**
     * Retrieves a student from the application using its ID.
     *
     * @param id The identifier of the student we want to retrieve.
     * @return The searched student.
     */
    StudentDto getStudentById(Long id);

    /**
     * Modifies the student identified by its id.
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
     * @return The recently deleted user's DTO.
     */
    StudentDto deleteStudentById(Long id);

    /**
     * Sets the validity of a passport.
     *
     * @param studentId The id of the student.
     * @param valid {@code true} if the passport is valid,
     * {@code false} otherwise.
     */
    void setValidity(
            Long studentId,
            boolean valid
    );
}
