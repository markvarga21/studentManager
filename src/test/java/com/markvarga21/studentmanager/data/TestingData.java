package com.markvarga21.studentmanager.data;

import com.markvarga21.studentmanager.dto.FaceApiResponse;
import com.markvarga21.studentmanager.dto.ReportMessage;
import com.markvarga21.studentmanager.dto.Role;
import com.markvarga21.studentmanager.dto.StudentDto;
import com.markvarga21.studentmanager.entity.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Set;

public final class TestingData {
    /**
     * The page for testing.
     */
    public static final Integer PAGE = 0;

    /**
     * The size for testing.
     */
    public static final Integer SIZE = 10;

    /**
     * The valid XML string for testing.
     */
    public static final String VALID_XML_STUDENT = """
    <?xml version="1.0" encoding="UTF-8"?>
    <students>
        <student id="1">
            <name>
                <firstName>Emily</firstName>
                <lastName>Johnson</lastName>
            </name>
            <dateOfBirth>
                <year>1995</year>
                <month>02</month>
                <day>12</day>
            </dateOfBirth>
            <placeOfBirth>New York</placeOfBirth>
            <countryOfCitizenship>American</countryOfCitizenship>
            <gender>female</gender>
            <passportNumber>US123456</passportNumber>
            <dateOfIssue>
                <year>2015</year>
                <month>03</month>
                <day>15</day>
            </dateOfIssue>
            <dateOfExpiry>
                <year>2025</year>
                <month>03</month>
                <day>15</day>
            </dateOfExpiry>
            <status>valid</status>
        </student>
    </students>
    """;

    /**
     * The invalid XML string for testing.
     */
    public static final String INVALID_XML_STUDENT = """
    <?xml version="1.0" encoding="UTF-8"?>
    <students>
        <student id="1">
            <dateOfBirth>
                <year>1995</year>
                <month>02</month>
                <day>12</day>
            </dateOfBirth>
            <placeOfBirth>New York</placeOfBirth>
            <countryOfCitizenship>American</countryOfCitizenship>
            <gender>female</gender>
            <passportNumber>US123456</passportNumber>
            <dateOfIssue>
                <year>2015</year>
                <month>03</month>
                <day>15</day>
            </dateOfIssue>
            <dateOfExpiry>
                <year>2025</year>
                <month>03</month>
                <day>15</day>
            </dateOfExpiry>
            <status>valid</status>
        </student>
    </students>
    """;

    /**
     * The valid JSON string for testing.
     */
    public static final String VALID_JSON_STUDENT = """
    [
      {
        "birthDate": "1992-11-01",
        "countryOfCitizenship": "South Korean",
        "dateOfExpiry": "2022-12-10",
        "dateOfIssue": "2012-12-10",
        "firstName": "Ava",
        "gender": "Female",
        "id": 3,
        "lastName": "Lee",
        "passportNumber": "KR345678",
        "placeOfBirth": "Seoul",
        "validity": false
      },
      {
        "birthDate": "1993-06-22",
        "countryOfCitizenship": "Chinese",
        "dateOfExpiry": "2023-07-20",
        "dateOfIssue": "2013-07-20",
        "firstName": "Logan",
        "gender": "Male",
        "id": 8,
        "lastName": "Brown",
        "passportNumber": "CN789012",
        "placeOfBirth": "Beijing",
        "validity": false
      }
    ]
    """;

    /**
     * The invalid JSON string for testing.
     */
    public static final String INVALID_JSON_STUDENT = """
    [
      {
        "birthDate": "1992-11-01",
        "countryOfCitizenship": "South Korean",
        "dateOfExpiry": "2022-1210",
        "dateOfIssue": "2012-12-10",
        "firstName": "Ava",
        "gender": "Female",
        "id": 3,
        "lastName": "Lee",
        "passportNumber": "KR345678",
        "placeOfBirth": "Seoul",
        "validity": false
      },
      {
        "birthDate": "1993-06-22",
        "countryOfCitizenship": "Chinese",
        "dateOfExpiry": "2023-07-20",
        "dateOfIssue": "2013-07-20",
        "firstName": "Logan",
        "gender": "Male",
        "id": 8,
        "lastName": "Brown",
        "passportNumber": "CN789012",
        "placeOfBirth": "Beijing",
        "validity": false
      }
    ]
    """;

    /**
     * A static user for mocking purposes.
     */
    public static final AppUser USER = AppUser.builder()
            .id(1L)
            .username("john.doe")
            .email("jdoe12@domain.com")
            .password("1234")
            .firstName("John")
            .lastName("Doe")
            .roles(Set.of(Role.USER))
            .build();

    /**
     * A face api response used for testing.
     */
    public static final FaceApiResponse FACE_API_RESPONSE = new FaceApiResponse(
            true,
            0.95
    );


    /**
     * The mock passport number.
     */
    public static final String PASSPORT_NUMBER = "123456";

    /**
     * A valid facial validation data used for testing.
     */
    public static final FacialValidationData VALID_FACIAL_VALIDATION_DATA =
            new FacialValidationData(1L, PASSPORT_NUMBER,true, 0.9);

    /**
     * The first passport validation data used for testing.
     */
    public static final PassportValidationData PASSPORT_VALIDATION_DATA1 = new PassportValidationData(
            1L,
            LocalDateTime.of(2000, Month.JANUARY, 1, 0, 0),
            "John",
            "Doe",
            LocalDate.of(2001, 2, 2),
            "New York",
            "USA",
            Gender.MALE,
            "123456789",
            LocalDate.of(2023, 4, 4),
            LocalDate.of(2022, 3, 3)
    );

    /**
     * The second passport validation data used for testing.
     */
    public static final PassportValidationData PASSPORT_VALIDATION_DATA2 = new PassportValidationData(
            2L,
            LocalDateTime.of(1980, Month.JANUARY, 1, 0, 0),
            "John",
            "Wick",
            LocalDate.of(2001, 2, 2),
            "Washington",
            "USA",
            Gender.MALE,
            "23456789",
            LocalDate.of(2022, 3, 3),
            LocalDate.of(2023, 4, 4)
    );

    /**
     * Static student data based on the information found in
     * {@code PASSPORT_VALIDATION_DATA1} passport.
     */
    public static final StudentDto STUDENT_DTO = StudentDto.builder()
            .id(1L)
            .firstName("John")
            .lastName("Doe")
            .birthDate("2001-02-02")
            .placeOfBirth("New York")
            .countryOfCitizenship("USA")
            .gender(Gender.MALE)
            .passportNumber("123456")
            .passportDateOfIssue("2020-01-01")
            .passportDateOfExpiry("2025-01-01")
            .build();


    /**
     * A static JSON string representing the student above.
     */
    public static final String STUDENT_JSON = """
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

    /**
     * A static student entity for testing purposes.
     */
    public static final Student STUDENT = new Student(
            1L,
            "John",
            "Doe",
            LocalDate.of(2001, 2, 2),
            "New York",
            "USA",
            Gender.MALE,
            "123456",
            LocalDate.of(2025, 1, 1),
            LocalDate.of(2020, 1, 1),
            false
    );


    /**
     * The invalidated student mock entity.
     */
    public static final Student INVALID_STUDENT = new Student(
            1L,
            "John",
            "Doe",
            LocalDate.of(1990, 1, 1),
            "New York",
            "USA",
            Gender.MALE,
            PASSPORT_NUMBER,
            LocalDate.of(2021, 1, 1),
            LocalDate.of(2031, 1, 1),
            false
    );

    /**
     * A validated student mock.
     */
    public static final Student VALID_STUDENT = new Student(
            1L,
            "John",
            "Doe",
            LocalDate.of(1990, 1, 1),
            "New York",
            "USA",
            Gender.MALE,
            PASSPORT_NUMBER,
            LocalDate.of(2021, 1, 1),
            LocalDate.of(2031, 1, 1),
            true
    );

    /**
     * The invalidated student mock dto.
     */
    public static final StudentDto INVALID_STUDENT_DTO = new StudentDto(
            1L,
            "John",
            "Doe",
            "1990-01-01",
            "New York",
            "USA",
            Gender.MALE,
            PASSPORT_NUMBER,
            "2021-01-01",
            "2031-01-01",
            false
    );

    /**
     * A sample report message.
     */
    public static final ReportMessage REPORT_MESSAGE = new ReportMessage(
            "JohnDoe",
            "Test subject",
            "Test description"
    );

    /**
     * A sample report.
     */
    public static final Report REPORT = Report.builder()
            .issuerUsername(REPORT_MESSAGE.getUsername())
            .subject(REPORT_MESSAGE.getSubject())
            .description(REPORT_MESSAGE.getDescription())
            .build();

    /**
     * An invalid static mock facial validation data.
     */
    public static final FacialValidationData INVALID_FACIAL_VALIDATION_DATA = FacialValidationData.builder()
            .passportNumber(PASSPORT_NUMBER)
            .isValid(false)
            .percentage(0.2)
            .build();

    /**
     * Static passport validation data for testing.
     */
    public static final PassportValidationData VALIDATION_DATA = PassportValidationData.builder()
            .id(1L)
            .timestamp(LocalDateTime.of(2021, 1, 1, 0, 0))
            .firstName("John")
            .lastName("Doe")
            .birthDate(LocalDate.of(2000, 1, 1))
            .placeOfBirth("New York")
            .countryOfCitizenship("USA")
            .gender(Gender.MALE)
            .passportNumber("123456")
            .passportDateOfIssue(LocalDate.of(2020, 1, 1))
            .passportDateOfExpiry(LocalDate.of(2030, 1, 1))
            .build();

    /**
     * Student dto object extracted from the passport validation data
     * above.
     */
    public static final StudentDto STUDENT_DTO_FROM_PASSPORT_VALIDATION = StudentDto.builder()
            .id(1L)
            .firstName("John")
            .lastName("Doe")
            .birthDate("2000-01-01")
            .placeOfBirth("New York")
            .countryOfCitizenship("USA")
            .gender(Gender.MALE)
            .passportNumber("123456")
            .passportDateOfIssue("2020-01-01")
            .passportDateOfExpiry("2030-01-01")
            .build();



    private TestingData() {
    }
}
