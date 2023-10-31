package com.markvarga21.studentmanager.controller;

import com.markvarga21.studentmanager.dto.StudentDto;
import com.markvarga21.studentmanager.service.StudentService;
import com.markvarga21.studentmanager.service.file.FileUploadService;
import com.markvarga21.studentmanager.service.form.FormRecognizerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    private final StudentService studentService;

    /**
     * Form recognizer service.
     */
    private final FormRecognizerService formRecognizerService;

    /**
     * File upload service.
     */
    private final FileUploadService fileUploadService;

    /**
     * Retrieves all students which are present in the application.
     *
     * @return A {@code List} containing all the students.
     */
    @GetMapping
    public List<StudentDto> getAllStudents() {
        return this.studentService.getAllStudents();
    }

    /**
     * Saves and validates a student in the database and then returns it.
     *
     * @param student The student itself.
     * @return The saved {@code StudentDto}.
     */
    @PostMapping
    public ResponseEntity<StudentDto> createStudent(
            @RequestBody @Valid final StudentDto student
    ) {

        StudentDto createdStudent = this.studentService.createStudent(student);
        return new ResponseEntity<>(createdStudent, HttpStatus.CREATED);
    }

    /**
     * Retrieves the desired student using its ID,
     * then returns it.
     *
     * @param id The ID of the student which we want to retrieve.
     * @return The searched student if present.
     */
    @GetMapping("/{id}")
    public ResponseEntity<StudentDto> getStudentById(
            @PathVariable final Long id
    ) {
        StudentDto foundStudent = this.studentService.getStudentById(id);
        return new ResponseEntity<>(foundStudent, HttpStatus.OK);
    }

    /**
     * Updates a student and then retrieves it.
     *
     * @param student The student itself.
     * @param studentId The ID of the student which has to be updated.
     * @return The updated {@code StudentDto}.
     */
    @PutMapping("/{studentId}")
    public ResponseEntity<StudentDto> updateStudentById(
            @RequestBody @Valid final StudentDto student,
            @PathVariable("studentId") final Long studentId
    ) {
        StudentDto updatedStudent = this.studentService
                .modifyStudentById(student, studentId);
        return new ResponseEntity<>(updatedStudent, HttpStatus.OK);
    }

    /**
     * Deletes a student and then retrieves it.
     *
     * @param id The ID of the student which we want to delete.
     * @return The recently deleted student DTO object.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<StudentDto> deleteStudentById(
            @PathVariable final Long id
    ) {
        StudentDto deletedStudent = this.studentService.deleteStudentById(id);
        String passportNumber = deletedStudent.getPassportNumber();

        this.fileUploadService.deleteImage(passportNumber);
        this.formRecognizerService.deletePassportValidationByPassportNumber(
                passportNumber
        );

        return new ResponseEntity<>(deletedStudent, HttpStatus.OK);
    }
}
