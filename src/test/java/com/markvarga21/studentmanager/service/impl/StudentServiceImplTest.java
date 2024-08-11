package com.markvarga21.studentmanager.service.impl;

import com.markvarga21.studentmanager.dto.StudentDto;
import com.markvarga21.studentmanager.entity.AppUser;
import com.markvarga21.studentmanager.entity.Gender;
import com.markvarga21.studentmanager.entity.Student;
import com.markvarga21.studentmanager.entity.StudentAppUser;
import com.markvarga21.studentmanager.exception.InvalidStudentException;
import com.markvarga21.studentmanager.exception.StudentNotFoundException;
import com.markvarga21.studentmanager.mapping.StudentMapper;
import com.markvarga21.studentmanager.repository.AppUserRepository;
import com.markvarga21.studentmanager.repository.StudentAppUserRepository;
import com.markvarga21.studentmanager.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static com.markvarga21.studentmanager.data.TestingData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
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
     * Repository for student app users.
     */
    @Mock
    private StudentAppUserRepository studentAppUserRepository;

    /**
     * The user repository.
     */
    @Spy
    private AppUserRepository appUserRepository;

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
    void shouldCreateStudentByUserWhenExistsTest() {
        // Given
        String username = "john12";
        String roles = "ROLE_USER";
        StudentAppUser studentAppUser = new StudentAppUser();
        studentAppUser.setStudentId(INVALID_STUDENT.getId());
        studentAppUser.setUsername(username);

        // When
        when(this.studentRepository.findStudentByPassportNumber(anyString()))
                .thenReturn(Optional.empty());
        when(this.studentMapper.mapStudentDtoToEntity(INVALID_STUDENT_DTO))
                .thenReturn(INVALID_STUDENT);
        when(this.studentMapper.mapStudentEntityToDto(INVALID_STUDENT))
                .thenReturn(INVALID_STUDENT_DTO);
        when(this.studentRepository.save(INVALID_STUDENT))
                .thenReturn(INVALID_STUDENT);
        StudentDto actual = this.studentService
                .createStudent(INVALID_STUDENT_DTO, username, roles);

        // Then
        verify(this.studentAppUserRepository)
                .save(studentAppUser);
        assertEquals(INVALID_STUDENT_DTO, actual);
    }

    @Test
    void shouldCreateAdminByUserWhenExistsTest() {
        // Given
        String username = "john12";
        String roles = "ROLE_ADMIN";
        StudentAppUser studentAppUser = new StudentAppUser();
        studentAppUser.setStudentId(INVALID_STUDENT.getId());
        studentAppUser.setUsername(username);
        AppUser appUser = AppUser.builder()
                .username(username)
                .build();

        // When
        when(this.studentRepository.findStudentByPassportNumber(anyString()))
                .thenReturn(Optional.empty());
        when(this.studentMapper.mapStudentDtoToEntity(INVALID_STUDENT_DTO))
                .thenReturn(INVALID_STUDENT);
        when(this.studentMapper.mapStudentEntityToDto(INVALID_STUDENT))
                .thenReturn(INVALID_STUDENT_DTO);
        when(this.studentRepository.save(INVALID_STUDENT))
                .thenReturn(INVALID_STUDENT);
        when(this.appUserRepository.findByFirstNameAndLastName(anyString(), anyString()))
                .thenReturn(Optional.of(appUser));
        StudentDto actual = this.studentService
                .createStudent(INVALID_STUDENT_DTO, username, roles);

        // Then
        verify(this.studentAppUserRepository)
                .save(studentAppUser);
        assertEquals(INVALID_STUDENT_DTO, actual);
    }

    @Test
    void shouldThrowExceptionUponCreateStudentByAdminIfNotExistsTest() {
        // Given
        String username = "john12";
        String roles = "ROLE_ADMIN";

        // When
        when(this.studentRepository.findStudentByPassportNumber(anyString()))
                .thenReturn(Optional.empty());
        when(this.studentMapper.mapStudentDtoToEntity(INVALID_STUDENT_DTO))
                .thenReturn(INVALID_STUDENT);
        when(this.studentRepository.save(INVALID_STUDENT))
                .thenReturn(INVALID_STUDENT);

        // Then
        assertThrows(
                StudentNotFoundException.class,
                () -> this.studentService
                        .createStudent(INVALID_STUDENT_DTO, username, roles)
        );
    }

    @Test
    void shouldThrowExceptionUponStudentCreationTest() {
        // Given
        String username = "john12";
        String roles = "ROLE_USER";

        // When
        when(this.studentRepository.findStudentByPassportNumber(PASSPORT_NUMBER))
                .thenReturn(Optional.of(INVALID_STUDENT));

        // Then
        assertThrows(
                InvalidStudentException.class,
                () -> this.studentService.createStudent(INVALID_STUDENT_DTO, username, roles)
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

    @Test
    void shouldThrowExceptionUponGetStudentByUsernameIfNotStudentUserPresentTest() {
        // Given
        String username = "john12";

        // When
        when(this.studentAppUserRepository.findByUsername(anyString()))
                .thenReturn(Optional.empty());

        // Then
        assertThrows(
                StudentNotFoundException.class,
                () -> this.studentService
                        .getStudentByUsername(username)
        );
    }

    @Test
    void shouldThrowExceptionUponGetStudentByUsernameTest() {
        // Given
        StudentAppUser appUser = new StudentAppUser();
        appUser.setUsername("john12");
        
        // When
        when(this.studentAppUserRepository.findByUsername(appUser.getUsername()))
                .thenReturn(Optional.of(appUser));

        // Then
        assertThrows(
                StudentNotFoundException.class,
                () -> this.studentService.getStudentByUsername(appUser.getUsername())
        );
    }

    @Test
    void shouldGetStudentByUsernameTest() {
        // Given
        StudentAppUser studentAppUser = new StudentAppUser();
        studentAppUser.setUsername("john12");
        studentAppUser.setStudentId(STUDENT.getId());
        Optional<StudentDto> expected = Optional.of(STUDENT_DTO);

        // When
        when(this.studentAppUserRepository.findByUsername(studentAppUser.getUsername()))
                .thenReturn(Optional.of(studentAppUser));
        when(this.studentRepository.findById(studentAppUser.getStudentId()))
                .thenReturn(Optional.of(STUDENT));
        when(this.studentMapper.mapStudentEntityToDto(any(Student.class)))
                .thenReturn(STUDENT_DTO);
        Optional<StudentDto> actual = this.studentService
                .getStudentByUsername(studentAppUser.getUsername());
        
        // Then
        assertEquals(expected, actual);
    }

    @Test
    void shouldFetchStudentByNameIfPresentTest() {
        // Given
        String firstName = STUDENT_DTO.getFirstName();
        String lastName = STUDENT_DTO.getLastName();
        Optional<StudentDto> expected = Optional.of(STUDENT_DTO);

        // When
        when(this.studentRepository.findStudentByFirstNameAndLastName(firstName, lastName))
                .thenReturn(Optional.of(STUDENT));
        when(this.studentMapper.mapStudentEntityToDto(STUDENT))
                .thenReturn(STUDENT_DTO);
        Optional<StudentDto> actual = this.studentService
                .getStudentByFirstAndLastName(firstName, lastName);

        // Then
        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnEmptyUponGetStudentByNameIfStudentIsNotPresentTest() {
        // Given
        String firstName = STUDENT_DTO.getFirstName();
        String lastName = STUDENT_DTO.getLastName();

        // When
        when(this.studentRepository.findStudentByFirstNameAndLastName(firstName, lastName))
                .thenReturn(Optional.empty());

        // Then
        assertEquals(Optional.empty(), this.studentService
                .getStudentByFirstAndLastName(firstName, lastName));
    }
}