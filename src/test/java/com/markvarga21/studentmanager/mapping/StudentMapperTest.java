package com.markvarga21.studentmanager.mapping;

import com.google.gson.Gson;
import com.markvarga21.studentmanager.dto.StudentDto;
import com.markvarga21.studentmanager.entity.Gender;
import com.markvarga21.studentmanager.entity.Student;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentMapperTest {
    /**
     * The student mapper under test.
     */
    @InjectMocks
    private StudentMapper studentMapper;

    /**
     * A GSON converter.
     */
    @Mock
    private Gson gson;

    /**
     * A static student entity for testing purposes.
     */
    static final Student STUDENT = new Student(
            1L,
            "John",
            "Doe",
            LocalDate.of(1990, 1, 1),
            "New York",
            "USA",
            Gender.MALE,
            "123456",
            LocalDate.of(2031, 1, 1),
            LocalDate.of(2021, 1, 1),
            false
    );

    /**
     * A static student DTO for testing purposes.
     */
    static final StudentDto STUDENT_DTO = new StudentDto(
            1L,
            "John",
            "Doe",
            "1990-01-01",
            "New York",
            "USA",
            Gender.MALE,
            "123456",
            "2031-01-01",
            "2021-01-01",
            false
    );

    /**
     * A static JSON string representing the student above.
     */
    static final String STUDENT_JSON = """
            {
                "id": 1,
                "firstName": "John",
                "lastName": "Doe",
                "birthDate": "1990-01-01",
                "placeOfBirth": "New York",
                "countryOfCitizenship": "USA",
                "gender": "MALE",
                "passportNumber": "123456",
                "passportDateOfIssue": "2021-01-01",
                "passportDateOfExpiry": "2031-01-01"
            }
            """;

    @Test
    void shouldMapStudentDtoToEntity() {
        // Given
        // When
        Student actual = this.studentMapper.mapStudentDtoToEntity(STUDENT_DTO);

        // Then
        assertEquals(STUDENT, actual);
    }

    @Test
    void shouldMapStudentEntityToDto() {
        // Given
        // When
        StudentDto actual = this.studentMapper.mapStudentEntityToDto(STUDENT);

        // Then
        assertEquals(STUDENT_DTO, actual);
    }

    @Test
    void shouldMapJsonToDto() {
        // Given
        // When
        when(this.gson.fromJson(STUDENT_JSON, StudentDto.class))
                .thenReturn(STUDENT_DTO);
        StudentDto actual = this.studentMapper.mapJsonToDto(STUDENT_JSON);

        // Then
        assertEquals(STUDENT_DTO, actual);
    }
}