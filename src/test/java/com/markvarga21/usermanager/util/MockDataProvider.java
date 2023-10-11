package com.markvarga21.usermanager.util;

import com.markvarga21.usermanager.dto.StudentDto;
import com.markvarga21.usermanager.entity.Student;
import com.markvarga21.usermanager.entity.Gender;

import java.time.LocalDate;

/**
 * A class which contains static mock data
 * for testing purposes.
 */
public final class MockDataProvider {
    /**
     * Static mock year for the birthdate.
     */
    public static final int MOCK_YEAR = 1990;
    /**
     * Static mock month for the birthdate.
     */
    public static final int MOCK_MONTH = 5;
    /**
     * Static mock day for the birthdate.
     */
    public static final int MOCK_DAY = 10;
    /**
     * Static mock country for the address.
     */
    public static final String MOCK_COUNTRY = "Hungary";
    /**
     * Static mock city for the address.
     */
    public static final String MOCK_CITY = "Debrecen";
    /**
     * Static mock street for the address.
     */
    public static final String MOCK_STREET = "Piac";
    /**
     * Static mock number for the address.
     */
    public static final Integer MOCK_NUMBER = 10;
    /**
     * Static mock user.
     */
    public static final String MOCK_USER_JSON = """
                {
                    "id": 2,
                    "firstName": "John",
                    "lastName": "Doe",
                    "birthDate": "1990-05-10",
                    "placeOfBirth": "Hungary"
                    "countryOfCitizenship": "USA",
                    "gender": "MALE",
                    "email": "john.doe@gmail.com",
                    "phoneNumber": "123456789"
                }""";
    private MockDataProvider() { }

    /**
     * Builds an app user entity and then returns it.
     * 
     * @return the statically built user entity.
     */
    public static Student getStaticAppUser() {
        String birthPlace = "Hungary";
        LocalDate birthDate = LocalDate.of(MOCK_YEAR, MOCK_MONTH, MOCK_DAY);
        String countryOfCitizenship = "american";
        String firstName = "John";
        long id = 1L;
        String lastName = "Doe";
        Gender gender = Gender.MALE;
        return Student.builder()
                .id(id)
                .firstName(firstName)
                .lastName(lastName)
                .birthDate(birthDate)
                .placeOfBirth(birthPlace)
                .gender(gender)
                .countryOfCitizenship(countryOfCitizenship)
                .build();
    }

    /**
     * Builds an app user DTO and then returns it.
     *
     * @return the statically built user DTO.
     */
    public static StudentDto getStaticAppUserDto() {
        String birthPlace = "Hungary";
        LocalDate birthDate = LocalDate.of(MOCK_YEAR, MOCK_MONTH, MOCK_DAY);
        String countryOfCitizenship = "american";
        String firstName = "John";
        long id = 1L;
        String lastName = "Doe";
        Gender gender = Gender.MALE;
        return StudentDto.builder()
                .id(id)
                .firstName(firstName)
                .lastName(lastName)
                .birthDate(birthDate)
                .placeOfBirth(birthPlace)
                .gender(gender)
                .countryOfCitizenship(countryOfCitizenship)
                .build();
    }
}
