package com.markvarga21.studentmanager.service.impl;

import com.markvarga21.studentmanager.dto.StudentDto;
import com.markvarga21.studentmanager.entity.Gender;
import com.markvarga21.studentmanager.entity.Student;
import com.markvarga21.studentmanager.exception.InvalidStudentException;
import com.markvarga21.studentmanager.exception.StudentNotFoundException;
import com.markvarga21.studentmanager.mapping.StudentMapper;
import com.markvarga21.studentmanager.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.markvarga21.studentmanager.data.TestingData.PAGE;
import static com.markvarga21.studentmanager.data.TestingData.SIZE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentServiceImplTest {
    /**
     * The service class which contains the core logic of the application.
     */
    @InjectMocks
    private StudentServiceImpl studentService;

    /**
     * Repository for students.
     */
    @Mock
    private StudentRepository studentRepository;

    /**
     * A student mapper.
     */
    @Mock
    private StudentMapper studentMapper;

    /**
     * The mock passport number.
     */
    static final String PASSPORT_NUMBER = "123456";

    /**
     * The invalidated student mock entity.
     */
    static final Student INVALID_STUDENT = new Student(
            1L,
            "John",
            "Doe",
            LocalDate.of(1990, 1, 1),
            "New York",
            "USA",
            Gender.MALE,
            PASSPORT_NUMBER,
            LocalDate.of(2021, 1, 1),
            LocalDate.of(2031, 1, 1),
            false
    );

    /**
     * A validated student mock.
     */
    static final Student VALID_STUDENT = new Student(
            1L,
            "John",
            "Doe",
            LocalDate.of(1990, 1, 1),
            "New York",
            "USA",
            Gender.MALE,
            PASSPORT_NUMBER,
            LocalDate.of(2021, 1, 1),
            LocalDate.of(2031, 1, 1),
            true
    );

    /**
     * The invalidated student mock dto.
     */
    static final StudentDto INVALID_STUDENT_DTO = new StudentDto(
            1L,
            "John",
            "Doe",
            "1990-01-01",
            "New York",
            "USA",
            Gender.MALE,
            PASSPORT_NUMBER,
            "2021-01-01",
            "2031-01-01",
            false
    );

    @Test
    void shouldReturnAllStudentsTest() {
        // Given
        List<Student> students = List.of(INVALID_STUDENT);
        Page<Student> studentPage = new PageImpl<>(students);

        // When
        when(studentRepository.findAll(any(Pageable.class)))
                .thenReturn(studentPage);
        when(studentMapper.mapStudentEntityToDto(any(Student.class)))
                .thenReturn(INVALID_STUDENT_DTO);
        List<StudentDto> result = studentService
                .getAllStudents(PAGE, SIZE)
                .getContent();

        // Then
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals("John", result.get(0).getFirstName());
        assertEquals("Doe", result.get(0).getLastName());
        assertEquals("1990-01-01", result.get(0).getBirthDate());
        assertEquals("New York", result.get(0).getPlaceOfBirth());
        assertEquals("USA", result.get(0).getCountryOfCitizenship());
        assertEquals(Gender.MALE, result.get(0).getGender());
        assertEquals("123456", result.get(0).getPassportNumber());
        assertEquals("2021-01-01", result.get(0).getPassportDateOfExpiry());
        assertEquals("2031-01-01", result.get(0).getPassportDateOfIssue());
    }

    @Test
    void shouldCreateStudentWhenExistsTest() {
        // Given
        // When
        when(this.studentRepository.findStudentByPassportNumber(anyString()))
                .thenReturn(Optional.empty());
        when(this.studentMapper.mapStudentDtoToEntity(INVALID_STUDENT_DTO))
                .thenReturn(INVALID_STUDENT);
        when(this.studentMapper.mapStudentEntityToDto(INVALID_STUDENT))
                .thenReturn(INVALID_STUDENT_DTO);
        StudentDto actual = this.studentService.createStudent(INVALID_STUDENT_DTO);

        // Then
        assertEquals(INVALID_STUDENT_DTO, actual);
    }

    @Test
    void shouldThrowExceptionUponStudentCreationTest() {
        // Given
        // When
        when(this.studentRepository.findStudentByPassportNumber(PASSPORT_NUMBER))
                .thenReturn(Optional.of(INVALID_STUDENT));

        // Then
        assertThrows(
                InvalidStudentException.class,
                () -> this.studentService.createStudent(INVALID_STUDENT_DTO)
        );
    }

    @Test
    void shouldGetStudentByIdIfExistsTest() {
        // Given
        Long studentId = INVALID_STUDENT.getId();

        // When
        when(this.studentRepository.findById(studentId))
                .thenReturn(Optional.of(INVALID_STUDENT));
        when(this.studentMapper.mapStudentEntityToDto(INVALID_STUDENT))
                .thenReturn(INVALID_STUDENT_DTO);
        StudentDto actual = this.studentService.getStudentById(studentId);

        // Then
        assertEquals(INVALID_STUDENT_DTO, actual);
    }

    @Test
    void shouldThrowExceptionWhenStudentIsFetchedByIdAndMissingTest() {
        // Given
        Long studentId = INVALID_STUDENT.getId();

        // When
        when(this.studentRepository.findById(studentId))
                .thenReturn(Optional.empty());

        // Then
        assertThrows(
                StudentNotFoundException.class,
                () -> this.studentService.getStudentById(studentId)
        );
    }

    @Test
    void shouldModifyStudentIfExistsTest() {
        // Given
        Long studentId = INVALID_STUDENT.getId();

        // When
        when(this.studentRepository.findById(studentId))
                .thenReturn(Optional.of(INVALID_STUDENT));
        when(this.studentRepository.save(INVALID_STUDENT))
                .thenReturn(INVALID_STUDENT);
        when(this.studentMapper.mapStudentEntityToDto(INVALID_STUDENT))
                .thenReturn(INVALID_STUDENT_DTO);
        StudentDto actual = this.studentService.modifyStudentById(INVALID_STUDENT_DTO, studentId);

        // Then
        assertEquals(INVALID_STUDENT_DTO, actual);
    }

    @Test
    void shouldThrowExceptionWhenModifyByIdIfNotPresentTest() {
        // Given
        Long studentId = INVALID_STUDENT.getId();

        // When
        when(this.studentRepository.findById(studentId))
                .thenReturn(Optional.empty());

        // Then
        assertThrows(
                StudentNotFoundException.class,
                () -> this.studentService
                        .modifyStudentById(INVALID_STUDENT_DTO, studentId)
        );
    }

    @Test
    void shouldDeleteStudentByIdIfPresentTest() {
        // Given
        Long studentId = INVALID_STUDENT.getId();

        // When
        when(this.studentRepository.findById(studentId))
                .thenReturn(Optional.of(INVALID_STUDENT));
        when(this.studentMapper.mapStudentEntityToDto(INVALID_STUDENT))
                .thenReturn(INVALID_STUDENT_DTO);
        doNothing()
                .when(this.studentRepository)
                .deleteById(studentId);
        StudentDto actual = this.studentService.deleteStudentById(studentId);

        // Then
        assertEquals(INVALID_STUDENT_DTO, actual);
    }

    @Test
    void shouldThrowExceptionUponStudentDeleteWhenNotPresent() {
        // Given
        Long studentId = INVALID_STUDENT.getId();

        // When
        when(this.studentRepository.findById(studentId))
                .thenReturn(Optional.empty());

        // Then
        assertThrows(
                StudentNotFoundException.class,
                () -> this.studentService.deleteStudentById(studentId)
        );
    }

    @Test
    void shouldSetValidityOfStudentIfPresentTest() {
        // Given
        Long studentId = INVALID_STUDENT.getId();
        String expected =  String
                .format("Student with ID '%s' validity set to 'valid'", studentId);

        // When
        when(this.studentRepository.findById(studentId))
                .thenReturn(Optional.of(INVALID_STUDENT));
        when(this.studentRepository.save(any(Student.class)))
                .thenReturn(VALID_STUDENT);
        String actual = this.studentService.setValidity(studentId, true);

        // Then
        assertEquals(expected, actual);
    }

    @Test
    void shouldSetInvalidityOfStudentIfPresentTest() {
        // Given
        Long studentId = VALID_STUDENT.getId();
        String expected =  String
                .format("Student with ID '%s' validity set to 'invalid'", studentId);

        // When
        when(this.studentRepository.findById(studentId))
                .thenReturn(Optional.of(VALID_STUDENT));
        when(this.studentRepository.save(INVALID_STUDENT))
                .thenReturn(INVALID_STUDENT);
        String actual = this.studentService.setValidity(studentId, false);

        // Then
        assertEquals(expected, actual);
    }

    @Test
    void shouldThrowExceptionUponSetValidityIfStudentNotPresent() {
        // Given
        Long studentId = INVALID_STUDENT.getId();

        // When
        when(this.studentRepository.findById(studentId))
                .thenReturn(Optional.empty());

        // Then
        assertThrows(
                StudentNotFoundException.class,
                () -> this.studentService.setValidity(studentId, true)
        );
    }
}