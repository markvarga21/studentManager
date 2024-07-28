package com.markvarga21.studentmanager.service.impl;

import com.markvarga21.studentmanager.dto.StudentDto;
import com.markvarga21.studentmanager.entity.AppUser;
import com.markvarga21.studentmanager.entity.Student;
import com.markvarga21.studentmanager.entity.StudentAppUser;
import com.markvarga21.studentmanager.exception.InvalidStudentException;
import com.markvarga21.studentmanager.exception.OperationType;
import com.markvarga21.studentmanager.exception.StudentNotFoundException;
import com.markvarga21.studentmanager.repository.AppUserRepository;
import com.markvarga21.studentmanager.repository.StudentAppUserRepository;
import com.markvarga21.studentmanager.repository.StudentRepository;
import com.markvarga21.studentmanager.service.StudentService;
import com.markvarga21.studentmanager.util.DateDeserializer;
import com.markvarga21.studentmanager.mapping.StudentMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Arrays;
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
     * Repository for student application users.
     */
    private final StudentAppUserRepository studentAppUserRepository;

    /**
     * Repository for application users.
     */
    private final AppUserRepository userRepository;

    /**
     * Retrieves all the students from the application.
     *
     * @param page The actual page.
     * @param size The number of elements in a single page.
     * @return A subset of students inside a {@code Page} object.
     * @since 1.0
     */
    @Override
    public Page<StudentDto> getAllStudents(
            final Integer page,
            final Integer size
    ) {
        return studentRepository
                .findAll(PageRequest.of(page, size))
                .map(studentMapper::mapStudentEntityToDto);
    }

    /**
     * Validates-, and then persists the student
     * into the database.
     *
     * @param studentDto The student itself.
     * @param username The username of the user who created the student.
     * @param roles The roles of the user who created the student.
     * @return The updated {@code AppUserDto}.
     */
    @Override
    @Transactional
    public StudentDto createStudent(
            final StudentDto studentDto,
            final String username,
            final String roles
    ) {
        String passportNumber = studentDto.getPassportNumber();
        if (!validPassportNumber(passportNumber)) {
            String message = String.format(
                    "Passport number '%s' is already in use!",
                    passportNumber
            );
            log.error(message);
            throw new InvalidStudentException(message);
        }

        Student studentToSave = this.studentMapper
                .mapStudentDtoToEntity(studentDto);
        studentToSave.setValid(false);
        Student savedStudent = this.studentRepository.save(studentToSave);
        if (roles != null && username != null) {
            List<String> roleArray = Arrays.asList(roles.split(","));
            if (!roleArray.contains("ROLE_ADMIN")) {
                StudentAppUser studentAppUser = new StudentAppUser();
                studentAppUser.setStudentId(savedStudent.getId());
                studentAppUser.setUsername(username);
                this.studentAppUserRepository.save(studentAppUser);
            } else {
                String studentFirstName = studentToSave.getFirstName();
                String studentLastName = studentToSave.getLastName();
                Optional<AppUser> studentUser = this.userRepository
                        .findByFirstNameAndLastName(studentFirstName, studentLastName);
                if (studentUser.isEmpty()) {
                    String message = String.format(
                            "Student user not found with first name: %s and last name: %s",
                            studentFirstName,
                            studentLastName
                    );
                    log.error(message);
                    throw new StudentNotFoundException(message, OperationType.CREATE);
                }
                AppUser studentAppUser = studentUser.get();
                StudentAppUser studentAppUserEntity = new StudentAppUser();
                studentAppUserEntity.setStudentId(savedStudent.getId());
                studentAppUserEntity.setUsername(studentAppUser.getUsername());
                this.studentAppUserRepository.save(studentAppUserEntity);
            }
        }

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
    @Override
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
     * Retrieves a student by its username.
     *
     * @param username The username of the student.
     * @return The student's DTO.
     */
    @Override
    public Optional<StudentDto> getStudentByUsername(final String username) {
        Optional<StudentAppUser> studentAppUser = this.studentAppUserRepository
                .findByUsername(username);
        if (studentAppUser.isEmpty()) {
            String message = String.format(
                "Student cant be retrieved! Cause: user not found with username: %s",
                username
            );
            log.error(message);
            throw new StudentNotFoundException(message, OperationType.READ);
        }
        StudentAppUser studentUser = studentAppUser.get();
        Optional<Student> student = this.studentRepository
                .findById(studentUser.getStudentId());
        if (student.isEmpty()) {
            String message = String.format(
                "Student cant be retrieved! Cause: user not found with username: %s",
                username
            );
            log.error(message);
            throw new StudentNotFoundException(message, OperationType.READ);
        }
        return Optional.of(
                this.studentMapper
                        .mapStudentEntityToDto(student.get()));
    }

    /**
     * Validates and then modifies the student's information.
     *
     * @param studentDto The user itself.
     * @param studentId The is of the student.
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

        String updatedStudentPassport = studentDto.getPassportNumber();

        student.setGender(studentDto.getGender());
        student.setFirstName(studentDto.getFirstName());
        student.setLastName(studentDto.getLastName());
        student.setCountryOfCitizenship(studentDto.getCountryOfCitizenship());
        student.setPlaceOfBirth(studentDto.getPlaceOfBirth());
        student.setBirthDate(DateDeserializer.mapDateStringToLocalDate(studentDto.getBirthDate()));
        student.setPassportNumber(updatedStudentPassport);
        student.setPassportDateOfExpiry(DateDeserializer.mapDateStringToLocalDate(studentDto.getPassportDateOfExpiry()));
        student.setPassportDateOfIssue(DateDeserializer.mapDateStringToLocalDate(studentDto.getPassportDateOfIssue()));
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
        this.studentAppUserRepository.deleteByStudentId(id);
        log.info(String.format(
                "Student with id %d deleted successfully!",
                id
        ));

        return deletedStudent;
    }

    /**
     * Validates the passport manually.
     *
     * @param studentId The id of the student.
     * @param valid The validity of the passport.
     */
    @Override
    public String setValidity(
            final Long studentId,
            final boolean valid
    ) {
        Optional<Student> studentOptional = this.studentRepository
                .findById(studentId);
        if (studentOptional.isEmpty()) {
            String message = String.format(
                "Student validity cannot be set! Cause: Student not found with ID '%s'",
                studentId
            );
            log.error(message);
            throw new StudentNotFoundException(message, OperationType.UPDATE);
        }
        Student student = studentOptional.get();
        student.setValid(valid);
        this.studentRepository.save(student);
        return String.format("Student with ID '%s' validity set to '%s'",
                studentId,
                valid ? "valid" : "invalid"
        );
    }
}
