package com.markvarga21.studentmanager.data;

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

    private TestingData() {
    }
}
