package com.markvarga21.usermanager.util;

import com.markvarga21.usermanager.dto.AddressDto;
import com.markvarga21.usermanager.dto.AppUserDto;
import com.markvarga21.usermanager.entity.Address;
import com.markvarga21.usermanager.entity.AppUser;
import com.markvarga21.usermanager.entity.Gender;

import java.time.LocalDate;

public final class MockDataProvider {
    public static final int MOCK_YEAR = 1990;
    public static final int MOCK_MONTH = 5;
    public static final int MOCK_DAY = 10;
    public static final String MOCK_COUNTRY = "Hungary";
    public static final String MOCK_CITY = "Debrecen";
    public static final String MOCK_STREET = "Piac";
    public static final Integer MOCK_NUMBER = 10;
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
    private MockDataProvider() {

    }

    public static AppUser getStaticAppUser() {
        Address birthPlace = new Address(1L, MOCK_COUNTRY, MOCK_CITY, MOCK_STREET, MOCK_NUMBER);
        Address address = new Address(2L, MOCK_COUNTRY, MOCK_CITY, MOCK_STREET, MOCK_NUMBER);
        LocalDate birthDate = LocalDate.of(MOCK_YEAR, MOCK_MONTH, MOCK_DAY);
        String nationality = "american";
        String firstName = "John";
        long id = 1L;
        String lastName = "Doe";
        Gender gender = Gender.MALE;
        String email = "john.doe@gmail.com";
        String phoneNumber = "123456789";
        return new AppUser(
                id,
                firstName,
                lastName,
                birthDate,
                birthPlace,
                nationality,
                gender,
                address,
                email,
                phoneNumber
        );
    }

    public static AppUserDto getStaticAppUserDto() {
        AddressDto birthPlace = new AddressDto(1L, MOCK_COUNTRY, MOCK_CITY, MOCK_STREET, MOCK_NUMBER);
        AddressDto address = new AddressDto(2L, MOCK_COUNTRY, MOCK_CITY, MOCK_STREET, MOCK_NUMBER);
        LocalDate birthDate = LocalDate.of(MOCK_YEAR, MOCK_MONTH, MOCK_DAY);
        String nationality = "american";
        String firstName = "John";
        long id = 1L;
        String lastName = "Doe";
        Gender gender = Gender.MALE;
        String email = "john.doe@gmail.com";
        String phoneNumber = "123456789";
        return new AppUserDto(
                id,
                firstName,
                lastName,
                birthDate,
                birthPlace,
                nationality,
                gender,
                address,
                email,
                phoneNumber
        );
    }
}
