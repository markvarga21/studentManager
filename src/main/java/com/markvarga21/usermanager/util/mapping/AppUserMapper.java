package com.markvarga21.usermanager.util.mapping;

import com.google.gson.Gson;
import com.markvarga21.usermanager.dto.StudentDto;
import com.markvarga21.usermanager.entity.Student;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/**
 * A utility class which is used for mapping between
 * the application's user entities and DTOs and backwards.
 */
@Component
@RequiredArgsConstructor
public class AppUserMapper {
    /**
     * A model mapper.
     */
    private final ModelMapper mapper;

    /**
     * A GSON converter.
     */
    private final Gson gson;

    /**
     * Maps an {@code AppUserDto} to an {@code AppUser} entity.
     *
     * @param studentDto the DTO object to be mapped to an entity class.
     * @return the converted {@code AppUser} entity.
     */
    public Student mapAppUserDtoToEntity(final StudentDto studentDto) {
        return this.mapper.map(studentDto, Student.class);
    }

    /**
     * Maps an {@code AppUser} entity to an {@code AppUserDto}.
     *
     * @param student the entity object to be mapped to a DTO class.
     * @return the converted {@code AppUserDto}.
     */
    public StudentDto mapAppUserEntityToDto(final Student student) {
        return this.mapper.map(student, StudentDto.class);
    }

    /**
     * Maps a JSON string to an {@code AppUserDto}.
     *
     * @param appUserJson the JSON string to be mapped to a DTO class.
     * @return the converted {@code AppUserDto}.
     */
    public StudentDto mapJsonToDto(final String appUserJson) {
        return this.gson.fromJson(appUserJson, StudentDto.class);
    }
}
