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
     * @return all to students stored in a {@code List}.
     */
    List<StudentDto> getAllStudents();

    /**
     * Validates and then persists a student in the database.
     *
     * @param studentJson the student itself with the new data.
     * @return the newly modified student.
     */
    StudentDto createStudent(String studentJson);

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
     * @param studentJson The student itself with the new data.
     * @param userId The id of the student we want to modify.
     * @return The newly modified student.
     */
    StudentDto modifyStudentById(String studentJson, Long userId);

    /**
     * Deletes a student by its ID.
     *
     * @param id The identifier used for deleting a student.
     * @return The recently deleted user's DTO.
     */
    StudentDto deleteStudentById(Long id);
}
