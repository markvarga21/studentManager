package com.markvarga21.studentmanager.service.impl;

import com.markvarga21.studentmanager.dto.StudentDto;
import com.markvarga21.studentmanager.entity.Student;
import com.markvarga21.studentmanager.exception.InvalidStudentException;
import com.markvarga21.studentmanager.exception.OperationType;
import com.markvarga21.studentmanager.exception.StudentNotFoundException;
import com.markvarga21.studentmanager.repository.StudentRepository;
import com.markvarga21.studentmanager.service.StudentService;
import com.markvarga21.studentmanager.service.file.FileUploadService;
import com.markvarga21.studentmanager.service.form.FormRecognizerService;
import com.markvarga21.studentmanager.util.mapping.StudentMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * The service class which contains the core logic of the application.
 */
@Transactional
@Slf4j
@RequiredArgsConstructor
@Service
public class StudentServiceImpl implements StudentService {
    /**
     * Repository for students.
     */
    private final StudentRepository studentRepository;

    /**
     * A student mapper.
     */
    private final StudentMapper studentMapper;

    /**
     * Retrieves all the students from the application.
     *
     * @return All to students stored in a {@code List}.
     * @since 1.0
     */
    @Override
    public List<StudentDto> getAllStudents() {
        return studentRepository
                .findAll()
                .stream()
                .map(this.studentMapper::mapStudentEntityToDto)
                .toList();
    }

    /**
     * Validates and then persists the student
     * into the database.
     *
     * @param studentDto The student itself.
     * @return The updated {@code AppUserDto}.
     */
    @Override
    @Transactional
    public StudentDto createStudent(final StudentDto studentDto) {
        String passportNumber = studentDto.getPassportNumber();
        if (!validPassportNumber(passportNumber)) {
            String message = String.format(
                    "Passport number '%s' is already in use!",
                    passportNumber
            );
            log.error(message);
            throw new InvalidStudentException(message);
        }

        Student studentToSave = this.studentMapper.mapStudentDtoToEntity(studentDto);
        studentToSave.setValid(false);
        this.studentRepository.save(studentToSave);

        StudentDto studentDtoToSave = this.studentMapper
                .mapStudentEntityToDto(studentToSave);
        log.info(String.format("Saving student: %s", studentDtoToSave));

        return studentDtoToSave;
    }

    /**
     * Checks if the passport number is already in use.
     *
     * @param passportNumber The passport number to check.
     * @return {@code true} if the passport number is valid,
     * {@code false} otherwise.
     */
    public boolean validPassportNumber(
            final String passportNumber
    ) {
        Optional<Student> student = this.studentRepository
                .findStudentByPassportNumber(passportNumber);
        return student.isEmpty();
    }

    /**
     * Retrieves a student from the application using its id.
     *
     * @param id The identifier of the student we want to retrieve.
     * @return The searched student.
     * @since 1.0
     */
    @Override
    public StudentDto getStudentById(final Long id) {
        Optional<Student> studentOptional = this.studentRepository.findById(id);
        if (studentOptional.isEmpty()) {
            String message = String.format(
                "Student cant be retrieved! Cause: user not found with id: %d",
                id
            );
            log.error(message);
            throw new StudentNotFoundException(message, OperationType.READ);
        }
        log.info(String.format(
                "Student with id %d retrieved successfully!",
                id
        ));

        return this.studentMapper.mapStudentEntityToDto(studentOptional.get());
    }

    /**
     * Validates and then modifies the student's information.
     *
     * @param studentDto The user itself.
     * @return The updated {@code StudentDto}.
     * @since 1.0
     */
    @Override
    @Transactional
    public StudentDto modifyStudentById(
            final StudentDto studentDto,
            final Long studentId
    ) {
        Optional<Student> studentOptional = this.studentRepository
                .findById(studentId);
        if (studentOptional.isEmpty()) {
            String message = String.format(
                "Student cant be modified! Cause: Student not found with id: %d",
                studentId
            );
            log.error(message);
            throw new StudentNotFoundException(message, OperationType.UPDATE);
        }
        Student student = studentOptional.get();

        student.setGender(studentDto.getGender());
        student.setFirstName(studentDto.getFirstName());
        student.setLastName(studentDto.getLastName());
        student
                .setCountryOfCitizenship(studentDto.getCountryOfCitizenship());
        student.setPlaceOfBirth(studentDto.getPlaceOfBirth());
        student.setBirthDate(studentDto.getBirthDate());
        student.setPassportNumber(studentDto.getPassportNumber());
        student.setPassportDateOfExpiry(studentDto.getPassportDateOfExpiry());
        student.setPassportDateOfIssue(studentDto.getPassportDateOfIssue());
        student.setValid(false);
        Student updatedUser = this.studentRepository.save(student);

        log.info(String.format(
                "Student with id %d modified successfully!", studentId)
        );
        return this.studentMapper.mapStudentEntityToDto(updatedUser);
    }

    /**
     * Deletes a student by its ID.
     *
     * @param id The identifier used for deleting a student.
     * @return The recently deleted student's DTO.
     * @since 1.0
     */
    @Override
    @Transactional
    public StudentDto deleteStudentById(final Long id) {
        Optional<Student> studentOptional = this.studentRepository.findById(id);
        if (studentOptional.isEmpty()) {
            String message = String.format(
                "Student cannot be deleted! Cause: student not found with id: %d",
                id
            );
            log.error(message);
            throw new StudentNotFoundException(message, OperationType.DELETE);
        }
        StudentDto deletedStudent = this.studentMapper
                .mapStudentEntityToDto(studentOptional.get());
        this.studentRepository.deleteById(id);
        log.info(String.format(
                "Student with id %d deleted successfully!",
                id
        ));

        return deletedStudent;
    }

    /**
     * Validates the passport manually (usually by an admin).
     *
     * @param passportNumber The passport number.
     * @param valid The validity of the passport.
     */
    @Override
    public void setValidity(
            final String passportNumber,
            final boolean valid
    ) {
        Optional<Student> studentOptional = this.studentRepository
                .findStudentByPassportNumber(passportNumber);
        if (studentOptional.isEmpty()) {
            String message = String.format(
                "Student validity cannot be set! Cause: Student not found with passport number: %s",
                passportNumber
            );
            log.error(message);
            return;
        }
        Student student = studentOptional.get();
        student.setValid(valid);
        this.studentRepository.save(student);
    }
}
