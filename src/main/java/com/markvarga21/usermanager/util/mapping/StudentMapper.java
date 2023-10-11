package com.markvarga21.usermanager.util.mapping;

import com.google.gson.Gson;
import com.markvarga21.usermanager.dto.StudentDto;
import com.markvarga21.usermanager.entity.Student;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/**
 * A utility class which is used for mapping between
 * the application's student entities and DTOs and backwards.
 */
@Component
@RequiredArgsConstructor
public class StudentMapper {
    /**
     * A model mapper.
     */
    private final ModelMapper mapper;

    /**
     * A GSON converter.
     */
    private final Gson gson;

    /**
     * Maps an {@code StudentDto} to an {@code Student} entity.
     *
     * @param studentDto The DTO object to be mapped to an entity class.
     * @return The converted {@code Student} entity.
     */
    public Student mapStudentDtoToEntity(final StudentDto studentDto) {
        return this.mapper.map(studentDto, Student.class);
    }

    /**
     * Maps an {@code Student} entity to an {@code StudentDto}.
     *
     * @param student The entity object to be mapped to a DTO class.
     * @return The converted {@code StudentDto}.
     */
    public StudentDto mapStudentEntityToDto(final Student student) {
        return this.mapper.map(student, StudentDto.class);
    }

    /**
     * Maps a JSON string to an {@code StudentDto}.
     *
     * @param studentJson the JSON string to be mapped to a DTO class.
     * @return the converted {@code StudentDto}.
     */
    public StudentDto mapJsonToDto(final String studentJson) {
        return this.gson.fromJson(studentJson, StudentDto.class);
    }
}
