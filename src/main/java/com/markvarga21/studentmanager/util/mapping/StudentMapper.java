package com.markvarga21.studentmanager.util.mapping;

import com.google.gson.Gson;
import com.markvarga21.studentmanager.dto.StudentDto;
import com.markvarga21.studentmanager.entity.Student;
import com.markvarga21.studentmanager.util.DateDeserializer;
import com.markvarga21.studentmanager.util.DateValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * A utility class which is used for mapping between
 * the application's student entities and DTOs and backwards.
 */
@Component
@RequiredArgsConstructor
public class StudentMapper {
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
        LocalDate birthDate = DateDeserializer
                .mapDateStringToLocalDate(studentDto.getBirthDate());
        LocalDate passportDateOfIssue = DateDeserializer
                .mapDateStringToLocalDate(studentDto.getPassportDateOfIssue());
        LocalDate passportDateOfExpiry = DateDeserializer
                .mapDateStringToLocalDate(studentDto.getPassportDateOfExpiry());

        DateValidator.validateBirthdate(birthDate);
        DateValidator.validatePassportIssueDate(passportDateOfIssue);
        DateValidator.validatePassportExpiryDate(passportDateOfExpiry);

        return Student.builder()
                .id(studentDto.getId())
                .firstName(studentDto.getFirstName())
                .lastName(studentDto.getLastName())
                .birthDate(birthDate)
                .passportDateOfIssue(passportDateOfIssue)
                .passportDateOfExpiry(passportDateOfExpiry)
                .gender(studentDto.getGender())
                .passportNumber(studentDto.getPassportNumber())
                .placeOfBirth(studentDto.getPlaceOfBirth())
                .countryOfCitizenship(studentDto.getCountryOfCitizenship())
                .valid(studentDto.isValid())
                .build();
    }

    /**
     * Maps an {@code Student} entity to an {@code StudentDto}.
     *
     * @param student The entity object to be mapped to a DTO class.
     * @return The converted {@code StudentDto}.
     */
    public StudentDto mapStudentEntityToDto(final Student student) {
        LocalDate birthDate = student.getBirthDate();
        LocalDate passportDateOfIssue = student.getPassportDateOfIssue();
        LocalDate passportDateOfExpiry = student.getPassportDateOfExpiry();

        DateValidator.validateBirthdate(birthDate);
        DateValidator.validatePassportIssueDate(passportDateOfIssue);
        DateValidator.validatePassportExpiryDate(passportDateOfExpiry);

        return StudentDto.builder()
                .id(student.getId())
                .firstName(student.getFirstName())
                .lastName(student.getLastName())
                .birthDate(DateDeserializer.mapLocalDateToDateString(birthDate))
                .passportDateOfIssue(DateDeserializer.mapLocalDateToDateString(passportDateOfIssue))
                .passportDateOfExpiry(DateDeserializer.mapLocalDateToDateString(passportDateOfExpiry))
                .passportNumber(student.getPassportNumber())
                .gender(student.getGender())
                .placeOfBirth(student.getPlaceOfBirth())
                .countryOfCitizenship(student.getCountryOfCitizenship())
                .valid(student.isValid())
                .build();
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
