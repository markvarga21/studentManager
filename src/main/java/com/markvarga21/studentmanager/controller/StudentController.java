package com.markvarga21.studentmanager.controller;

import com.markvarga21.studentmanager.dto.StudentDto;
import com.markvarga21.studentmanager.exception.util.ApiError;
import com.markvarga21.studentmanager.exception.util.AuthError;
import com.markvarga21.studentmanager.service.StudentService;
import com.markvarga21.studentmanager.service.file.FileUploadService;
import com.markvarga21.studentmanager.service.form.FormRecognizerService;
import com.markvarga21.studentmanager.service.validation.face.FacialValidationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * A controller which is used to make- create-, read-,
 * update- and delete students.
 */
@RestController
@RequestMapping("/api/v1/students")
@RequiredArgsConstructor
@CrossOrigin
@Tag(
    name = "Student services",
    description = "The student related endpoints."
)
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
     * @param size The number of elements in a single page.
     * @return All the students stored in a {@code Page}.
     */
    @Operation(
        summary = "Retrieves all students from the database.",
        responses = {
            @ApiResponse(responseCode = "200", description = "A page of students.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))
            }),
            @ApiResponse(responseCode = "401", description = "User is not authorized.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = AuthError.class))
            }),
            @ApiResponse(responseCode = "500", description = "Internal server error.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
            })
        }
    )
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
     * @param username The username of the user who created the student.
     * @param roles The roles of the user who created the student.
     * @return The saved {@code StudentDto}.
     */
    @Operation(
        summary = "Creates a student.",
        responses = {
            @ApiResponse(responseCode = "200", description = "The created student.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = StudentDto.class))
            }),
            @ApiResponse(responseCode = "401", description = "User is not authorized.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = AuthError.class))
            }),
            @ApiResponse(responseCode = "500", description = "Internal server error.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
            })
        }
    )
    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<StudentDto> createStudent(
            @RequestBody @Valid final StudentDto student,
            @RequestParam(required = false) final String username,
            @RequestParam(required = false) final String roles
    ) {

        StudentDto createdStudent = this.studentService
                .createStudent(student, username, roles);
        return new ResponseEntity<>(createdStudent, HttpStatus.CREATED);
    }

    /**
     * Retrieves the desired student using its ID,
     * then returns it.
     *
     * @param id The ID of the student which we want to retrieve.
     * @return The wanted student if present.
     */
    @Operation(
        summary = "Fetches a student by ID.",
        responses = {
            @ApiResponse(responseCode = "200", description = "The retrieved student.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = StudentDto.class))
            }),
            @ApiResponse(responseCode = "401", description = "User is not authorized.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = AuthError.class))
            }),
            @ApiResponse(responseCode = "500", description = "Internal server error.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
            })
        }
    )
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
    @Operation(
        summary = "Update a student identified by it's ID.",
        responses = {
            @ApiResponse(responseCode = "200", description = "The updated student.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = StudentDto.class))
            }),
            @ApiResponse(responseCode = "401", description = "User is not authorized.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = AuthError.class))
            }),
            @ApiResponse(responseCode = "500", description = "Internal server error.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
            })
        }
    )
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
    @Operation(
        summary = "Delete a student by ID.",
        responses = {
            @ApiResponse(responseCode = "200", description = "The deleted student.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = StudentDto.class))
            }),
            @ApiResponse(responseCode = "401", description = "User is not authorized.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = AuthError.class))
            }),
            @ApiResponse(responseCode = "500", description = "Internal server error.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
            })
        }
    )
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
