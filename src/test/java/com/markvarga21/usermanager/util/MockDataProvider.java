package com.markvarga21.usermanager.util;

import com.markvarga21.usermanager.dto.AddressDto;
import com.markvarga21.usermanager.dto.AppUserDto;
import com.markvarga21.usermanager.entity.Address;
import com.markvarga21.usermanager.entity.AppUser;
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
                    "placeOfBirth": {
                        "id": 1,
                        "country": "Hungary",
                        "city": "Debrecen",
                        "street": "Piac",
                        "number": 10
                    },
                    "nationality": "american",
                    "gender": "MALE",
                    "address": {
                        "id": 2,
                        "country": "Hungary",
                        "city": "Debrecen",
                        "street": "Piac",
                        "number": 10
                    },
                    "email": "john.doe@gmail.com",
                    "phoneNumber": "123456789"
                }""";
    private MockDataProvider() { }

    /**
     * Builds an app user entity and then returns it.
     * @return the statically built user entity.
     */
    public static AppUser getStaticAppUser() {
        Address birthPlace = Address.builder()
                .id(1L)
                .country(MOCK_COUNTRY)
                .city(MOCK_CITY)
                .street(MOCK_STREET)
                .number(MOCK_NUMBER).build();
        Address address = Address.builder()
                .id(2L)
                .country(MOCK_COUNTRY)
                .city(MOCK_CITY)
                .street(MOCK_STREET)
                .number(MOCK_NUMBER).build();
        LocalDate birthDate = LocalDate.of(MOCK_YEAR, MOCK_MONTH, MOCK_DAY);
        String nationality = "american";
        String firstName = "John";
        long id = 1L;
        String lastName = "Doe";
        Gender gender = Gender.MALE;
        String email = "john.doe@gmail.com";
        String phoneNumber = "123456789";
        return AppUser.builder()
                .id(id)
                .firstName(firstName)
                .lastName(lastName)
                .birthDate(birthDate)
                .placeOfBirth(birthPlace)
                .address(address)
                .gender(gender)
                .nationality(nationality)
                .email(email)
                .phoneNumber(phoneNumber).build();
    }

    /**
     * Builds an app user DTO and then returns it.
     * @return the statically built user DTO.
     */
    public static AppUserDto getStaticAppUserDto() {
        AddressDto birthPlace = AddressDto.builder()
                .id(1L)
                .country(MOCK_COUNTRY)
                .city(MOCK_CITY)
                .street(MOCK_STREET)
                .number(MOCK_NUMBER).build();
        AddressDto address = AddressDto.builder()
                .id(2L)
                .country(MOCK_COUNTRY)
                .city(MOCK_CITY)
                .street(MOCK_STREET)
                .number(MOCK_NUMBER).build();
        LocalDate birthDate = LocalDate.of(MOCK_YEAR, MOCK_MONTH, MOCK_DAY);
        String nationality = "american";
        String firstName = "John";
        long id = 1L;
        String lastName = "Doe";
        Gender gender = Gender.MALE;
        String email = "john.doe@gmail.com";
        String phoneNumber = "123456789";
        return AppUserDto.builder()
                .id(id)
                .firstName(firstName)
                .lastName(lastName)
                .birthDate(birthDate)
                .placeOfBirth(birthPlace)
                .address(address)
                .gender(gender)
                .nationality(nationality)
                .email(email)
                .phoneNumber(phoneNumber).build();
    }
}
