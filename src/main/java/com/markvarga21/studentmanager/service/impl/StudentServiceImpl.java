package com.markvarga21.studentmanager.service.impl;

import com.markvarga21.studentmanager.dto.StudentDto;
import com.markvarga21.studentmanager.entity.Student;
import com.markvarga21.studentmanager.exception.InvalidStudentException;
import com.markvarga21.studentmanager.exception.OperationType;
import com.markvarga21.studentmanager.exception.StudentNotFoundException;
import com.markvarga21.studentmanager.repository.StudentRepository;
import com.markvarga21.studentmanager.service.StudentService;
import com.markvarga21.studentmanager.service.faceapi.FaceApiService;
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
     * A Form Recognizer service.
     */
    private final FormRecognizerService formRecognizerService;

    /**
     * Retrieves all the students from the application.
     *
     * @return All to students stored in a {@code List}.
     * @since 1.0
     */
    @Override
    public List<StudentDto> getAllStudents() {
        List<StudentDto> studentDtoList = studentRepository
                .findAll()
                .stream()
                .map(this.studentMapper::mapStudentEntityToDto)
                .toList();
        log.info(String.format("Listing %d students.", studentDtoList.size()));

        return studentDtoList;
    }

    /**
     * Validates and then persists the student
     * into the database.
     *
     * @param studentJson The student itself in a JSON string.
     * @return The updated {@code AppUserDto}.
     */
    @Override
    public StudentDto createStudent(final String studentJson) {
        StudentDto studentDto = this.studentMapper.mapJsonToDto(studentJson);

        String firstName = studentDto.getFirstName();
        String lastName = studentDto.getLastName();
        if (!validNames(firstName, lastName)) {
            String message = String.format(
                    "'%s' first name and '%s' last name is already in use!",
                    firstName,
                    lastName
            );
            log.error(message);
            throw new InvalidStudentException(message);
        }

        Student studentToSave = this.studentMapper.mapStudentDtoToEntity(studentDto);
        this.studentRepository.save(studentToSave);

        StudentDto studentDtoToSave = this.studentMapper
                .mapStudentEntityToDto(studentToSave);
        log.info(String.format("Saving student: %s", studentDtoToSave));

        return studentDtoToSave;
    }

    /**
     * Checks if the students first- and last names are available.
     *
     * @param firstName The first name of the student.
     * @param lastName The last name of the student.
     * @return {@code true} if there is no other student
     * with the same name, else {@code false}.
     */
    public boolean validNames(
            final String firstName,
            final String lastName
    ) {
        Optional<Student> student = this.studentRepository
                .findStudentByFirstNameAndLastName(firstName, lastName);
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
     * @param studentJson The user itself in a JSON string.
     * @return The updated {@code StudentDto}.
     * @since 1.0
     */
    @Override
    public StudentDto modifyStudentById(
            final String studentJson,
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
        StudentDto studentDto = this.studentMapper.mapJsonToDto(studentJson);

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
        this.formRecognizerService.deletePassportValidationByPassportNumber(
                deletedStudent.getPassportNumber()
        );
        log.info(String.format(
                "Student with id %d deleted successfully!",
                id
        ));

        return deletedStudent;
    }
}
