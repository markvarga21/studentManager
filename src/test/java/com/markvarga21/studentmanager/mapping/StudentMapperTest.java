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

import static com.markvarga21.studentmanager.data.TestingData.*;
import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    void shouldMapJsonToDtoTest() {
        // Given
        // When
        when(this.gson.fromJson(STUDENT_JSON, StudentDto.class))
                .thenReturn(STUDENT_DTO);
        StudentDto actual = this.studentMapper.mapJsonToDto(STUDENT_JSON);

        // Then
        assertEquals(STUDENT_DTO, actual);
    }
}