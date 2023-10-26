package com.markvarga21.studentmanager.service;

import com.google.gson.Gson;
import com.markvarga21.studentmanager.dto.StudentDto;
import com.markvarga21.studentmanager.entity.Student;
import com.markvarga21.studentmanager.exception.StudentNotFoundException;
import com.markvarga21.studentmanager.repository.StudentRepository;
import com.markvarga21.studentmanager.service.form.FormRecognizerService;
import com.markvarga21.studentmanager.service.faceapi.FaceApiService;
import com.markvarga21.studentmanager.service.impl.StudentServiceImpl;
import com.markvarga21.studentmanager.util.mapping.StudentMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.markvarga21.studentmanager.util.MockDataProvider.getStaticAppUser;
import static com.markvarga21.studentmanager.util.MockDataProvider.getStaticAppUserDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;


import java.util.List;
import java.util.Optional;

/**
 * The app user service tester class.
 */
@ExtendWith(MockitoExtension.class)
class StudentServiceTest {
    /**
     * The app user service mock object.
     */
    @InjectMocks
    private StudentServiceImpl appUserService;
    /**
     * The app user repository.
     */
    @Mock
    private StudentRepository studentRepository;
    /**
     * The app user mapper.
     */
    @Mock
    private StudentMapper studentMapper;

    /**
     * The form recognizer service.
     */
    @Mock
    private FormRecognizerService formRecognizerService;
    /**
     * The GSON deserializer.
     */
    @Mock
    private Gson gson;
    /**
     * The Face API service.
     */
    @Mock
    private FaceApiService faceApiService;

    @Test
    void getAllUsersShouldReturnListOfUsersTest() {
        // Given
        Student student = getStaticAppUser();
        StudentDto studentDto = getStaticAppUserDto();
        List<StudentDto> expected = List.of(studentDto);

        // When
        when(this.studentRepository.findAll())
                .thenReturn(List.of(student));
        when(this.studentMapper.mapStudentEntityToDto(any()))
                .thenReturn(studentDto);
        List<StudentDto> actual = this.appUserService.getAllStudents();

        // Then
        assertEquals(expected, actual);
    }

    @Test
    void validNamesShouldReturnFalseIfNamesAreValidTest() {
        // Given
        String firstName = getStaticAppUser().getFirstName();
        String lastName = getStaticAppUser().getLastName();
        boolean expected = false;

        // When
        when(this.studentRepository
                .findStudentByFirstNameAndLastName(firstName, lastName))
                .thenReturn(Optional.of(getStaticAppUser()));
        boolean actual = this.appUserService.validNames(firstName, lastName);

        // Then
        assertEquals(expected, actual);
    }

    @Test
    void validNamesShouldReturnTrueIfNamesAreInvalidTest() {
        // Given
        String firstName = getStaticAppUser().getFirstName();
        String lastName = getStaticAppUser().getLastName();
        boolean expected = true;

        // When
        when(this.studentRepository.findStudentByFirstNameAndLastName(
                firstName, lastName
        ))
                .thenReturn(Optional.empty());
        boolean actual = this.appUserService.validNames(firstName, lastName);

        // Then
        assertEquals(expected, actual);
    }

    @Test
    void getUserByIdShouldReturnUserWhenIdIsValidTest() {
        // Given
        Student student = getStaticAppUser();
        StudentDto studentDto = getStaticAppUserDto();
        Long id = student.getId();

        // When
        when(this.studentRepository.findById(id))
                .thenReturn(Optional.of(student));
        when(this.studentMapper.mapStudentEntityToDto(student))
                .thenReturn(studentDto);
        StudentDto actual = this.appUserService.getStudentById(id);

        // Then
        assertSame(studentDto, actual);
    }

    @Test
    void getUserByIdShouldThrowExceptionWhenUserIsNotPresentTest() {
        // Given
        Student student = getStaticAppUser();
        Long id = student.getId();

        // When
        when(this.studentRepository.findById(id)).thenReturn(Optional.empty());

        // Then
        assertThrows(StudentNotFoundException.class,
                () -> this.appUserService.getStudentById(id)
        );
    }

    @Test
    void deleteUserByIdShouldReturnUserWhenIdExistsTest() {
        // Given
        Student student = getStaticAppUser();
        StudentDto expected = getStaticAppUserDto();
        Long id = student.getId();

        // When
        when(this.studentRepository.findById(id))
                .thenReturn(Optional.of(student));
        when(this.studentMapper.mapStudentEntityToDto(student))
                .thenReturn(expected);
        doNothing()
                .when(this.studentRepository)
                .deleteById(id);
        StudentDto actual = this.appUserService.deleteStudentById(id);

        // Then
        assertSame(expected, actual);
    }

    @Test
    void deleteUserByIdShouldThrowExceptionWhenIdNotExistsTest() {
        // Given
        Student student = getStaticAppUser();
        Long id = student.getId();

        // When
        when(this.studentRepository.findById(id)).thenReturn(Optional.empty());

        // Then
        assertThrows(StudentNotFoundException.class,
                () -> this.appUserService.deleteStudentById(id)
        );
    }
}
