package com.markvarga21.usermanager.controller;

import com.markvarga21.usermanager.dto.StudentDto;
import com.markvarga21.usermanager.service.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * A controller which is used to make create, read,
 * update and delete students.
 */
@RestController
@RequestMapping("/api/v1/students")
@RequiredArgsConstructor
@CrossOrigin
public class StudentController {
    /**
     * Student service.
     */
    private final AppUserService userService;

    /**
     * Retrieves all students which are present in the application.
     *
     * @return a {@code List} containing all the students.
     */
    @GetMapping
    public List<StudentDto> getAllStudents() {
        return this.userService.getAllUsers();
    }

    /**
     * Saves and validates a student in the database and then returns it.
     *
     * @param studentJson the student itself in a JSON string.
     * @return the saved {@code StudentDto}.
     */
    @PostMapping
    public ResponseEntity<StudentDto> createUser(
            @RequestParam("studentJson") final String studentJson
    ) {

        StudentDto createdStudent = this.userService.createUser(studentJson);
        return new ResponseEntity<>(createdStudent, HttpStatus.CREATED);
    }

    /**
     * Retrieves the desired student using its ID,
     * then returns it.
     *
     * @param id the ID of the student which we want to retrieve.
     * @return the searched student if present.
     */
    @GetMapping("/{id}")
    public ResponseEntity<StudentDto> getStudentById(
            @PathVariable final Long id
    ) {
        StudentDto foundStudent = this.userService.getUserById(id);
        return new ResponseEntity<>(foundStudent, HttpStatus.OK);
    }

    /**
     * Updates a student and then retrieves it.
     *
     * @param studentJson the student itself in a JSON string.
     * @param userId the ID of the student which has to be updated.
     * @return the updated {@code StudentDto}.
     */
    @PutMapping("/{userId}")
    public ResponseEntity<StudentDto> updateStudentById(
            @RequestParam("studentJson") final String studentJson,
            @PathVariable("userId") final Long userId
            ) {
        StudentDto updatedStudent = this.userService
                .modifyUserById(studentJson, userId);
        return new ResponseEntity<>(updatedStudent, HttpStatus.OK);
    }

    /**
     * Deletes a student and then retrieves it.
     *
     * @param id the ID of the student which we want to delete.
     * @return the recently deleted student DTO object.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<StudentDto> deleteStudentById(
            @PathVariable final Long id
    ) {
        StudentDto deletedStudent = this.userService.deleteUserById(id);
        return new ResponseEntity<>(deletedStudent, HttpStatus.OK);
    }
}