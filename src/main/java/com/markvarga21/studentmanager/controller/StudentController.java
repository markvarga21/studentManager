package com.markvarga21.studentmanager.controller;

import com.markvarga21.studentmanager.dto.StudentDto;
import com.markvarga21.studentmanager.service.StudentService;
import com.markvarga21.studentmanager.service.file.FileUploadService;
import com.markvarga21.studentmanager.service.form.FormRecognizerService;
import com.markvarga21.studentmanager.service.validation.face.FacialValidationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
@Tag(name = "Student services", description = "The student related endpoints.")
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
     * Facial validation service.
     */
    private final FacialValidationService facialValidationService;

    /**
     * Retrieves all the students from the application.
     *
     * @param page The page number.
     * @param size The size of the page.
     * @return All the students stored in a {@code Page}.
     */
    @Operation(summary = "Get all students", description = "Retrieves all the students.")
    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Page<StudentDto> getAllStudents(
            @RequestParam(defaultValue = "0") final Integer page,
            @RequestParam(defaultValue = "10") final Integer size
    ) {
        return this.studentService.getAllStudents(page, size);
    }

    /**
     * Saves and validates a student in the database and then returns it.
     *
     * @param student The student itself.
     * @return The saved {@code StudentDto}.
     */
    @Operation(summary = "Create a student", description = "Creates a student.")
    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER')")
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
    @Operation(summary = "Get a student by ID", description = "Retrieves a student by ID.")
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
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
    @Operation(summary = "Update a student by ID", description = "Updates a student by ID.")
    @PutMapping("/{studentId}")
    @PreAuthorize("hasRole('ROLE_USER')")
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
    @Operation(summary = "Delete a student by ID", description = "Deletes a student by ID.")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<StudentDto> deleteStudentById(
            @PathVariable final Long id
    ) {
        StudentDto deletedStudent = this.studentService.deleteStudentById(id);
        String passportNumber = deletedStudent.getPassportNumber();

        this.fileUploadService.deleteImage(id);
        this.formRecognizerService.deletePassportValidationByPassportNumber(
                passportNumber
        );
        this.facialValidationService.deleteFacialValidationDataByPassportNumber(
                passportNumber
        );

        return new ResponseEntity<>(deletedStudent, HttpStatus.OK);
    }
}
