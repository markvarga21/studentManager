package com.markvarga21.usermanager.service;

import com.google.gson.Gson;
import com.markvarga21.usermanager.dto.AddressDto;
import com.markvarga21.usermanager.dto.AppUserDto;
import com.markvarga21.usermanager.entity.Address;
import com.markvarga21.usermanager.entity.AppUser;
import com.markvarga21.usermanager.entity.Gender;
import com.markvarga21.usermanager.exception.InvalidUserException;
import com.markvarga21.usermanager.exception.UserNotFoundException;
import com.markvarga21.usermanager.repository.AppUserRepository;
import com.markvarga21.usermanager.service.azure.FormRecognizerService;
import com.markvarga21.usermanager.service.faceapi.FaceApiService;
import com.markvarga21.usermanager.service.impl.AppUserServiceImpl;
import com.markvarga21.usermanager.util.mapping.AddressMapper;
import com.markvarga21.usermanager.util.mapping.AppUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@Slf4j
public class AppUserServiceTest {
    @InjectMocks
    private AppUserServiceImpl appUserService;
    @Mock
    private AppUserRepository appUserRepository;
    @Mock
    private AppUserMapper appUserMapper;
    @Mock
    private AddressMapper addressMapper;
    @Mock
    private FormRecognizerService formRecognizerService;
    @Mock
    private Gson gson;
    @Mock
    private FaceApiService faceApiService;
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

    static AppUser getStaticAppUser() {
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

    static AppUserDto getStaticAppUserDto() {
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

    static MultipartFile getFileForName(final String fileName) {
        try (InputStream inputStream = AppUserServiceTest.class.getClassLoader().getResourceAsStream(fileName)) {
            return new MockMultipartFile(fileName, inputStream);
        } catch (IOException exception) {
            String message = String.format("Something went wrong when reading '%s'", fileName);
            log.error(message);
        }
        return null;
    }

    @Test
    void getAllUsersShouldReturnListOfUsersTest() {
        // Given
        AppUser appUser = getStaticAppUser();
        AppUserDto appUserDto = getStaticAppUserDto();
        List<AppUserDto> expected = List.of(appUserDto);

        // When
        when(this.appUserRepository.findAll()).thenReturn(List.of(appUser));
        when(this.appUserMapper.mapAppUserEntityToDto(any())).thenReturn(appUserDto);
        List<AppUserDto> actual = this.appUserService.getAllUsers();

        // Then
        assertEquals(expected, actual);
    }

    @Test
    void createUserShouldReturnCreatedUsersWhenDataIsValidTest() {
        // Given
        AppUserDto appUserDto = getStaticAppUserDto();
        String userJson = MOCK_USER_JSON;
        AppUser appUser = getStaticAppUser();
        MultipartFile idDocument = getFileForName("huId.jpg");
        MultipartFile selfiePhoto = getFileForName("huFace.png");
        String idType = "idDocument";

        // When
        when(this.gson.fromJson(userJson, AppUserDto.class)).thenReturn(appUserDto);
        when(this.appUserMapper.mapAppUserDtoToEntity(appUserDto)).thenReturn(appUser);
        when(this.appUserMapper.mapAppUserEntityToDto(appUser)).thenReturn(appUserDto);
        doNothing().when(this.formRecognizerService).validateUser(eq(appUserDto), any(), anyString());
        doNothing().when(this.faceApiService).facesAreMatching(any(), any());
        when(this.appUserRepository.save(appUser)).thenReturn(appUser);
        AppUserDto actual = this.appUserService.createUser(idDocument, selfiePhoto, userJson, idType);

        // Then
        assertEquals(appUserDto, actual);
    }

    @Test
    void createUserShouldThrowExceptionWhenNamesAreInvalid() {
        // Given
        AppUserDto appUserDto = getStaticAppUserDto();
        String userJson = MOCK_USER_JSON;
        AppUser appUser = getStaticAppUser();
        MultipartFile idDocument = getFileForName("huId.jpg");
        MultipartFile selfiePhoto = getFileForName("huFace.png");
        String idType = "idDocument";
        String firstName = appUserDto.getFirstName();
        String lastName = appUserDto.getLastName();

        // When
        when(this.gson.fromJson(userJson, AppUserDto.class)).thenReturn(appUserDto);
        when(this.appUserRepository.findAppUserByFirstNameAndLastName(firstName, lastName)).thenReturn(Optional.of(appUser));

        // Then
        assertThrows(InvalidUserException.class,
                () -> this.appUserService.createUser(idDocument, selfiePhoto, userJson, idType)
        );
    }

    @Test
    void validNamesShouldReturnFalseIfNamesAreValidTest() {
        // Given
        String firstName = getStaticAppUser().getFirstName();
        String lastName = getStaticAppUser().getLastName();
        boolean expected = false;

        // When
        when(this.appUserRepository.findAppUserByFirstNameAndLastName(firstName, lastName)).thenReturn(Optional.of(getStaticAppUser()));
        boolean actual = this.appUserService.validNames(firstName, lastName);

        // Then
        assertEquals(expected, actual);
    }

    @Test
    void validNamesShouldReturnTrueIfNamesAreInvalidTest() {
        // Given
        String firstName = getStaticAppUser().getFirstName();
        String lastName = getStaticAppUser().getLastName();
        boolean expected = true;

        // When
        when(this.appUserRepository.findAppUserByFirstNameAndLastName(firstName, lastName)).thenReturn(Optional.empty());
        boolean actual = this.appUserService.validNames(firstName, lastName);

        // Then
        assertEquals(expected, actual);
    }

    @Test
    void getUserByIdShouldReturnUserWhenIdIsValidTest() {
        // Given
        AppUser appUser = getStaticAppUser();
        AppUserDto appUserDto = getStaticAppUserDto();
        Long id = appUser.getId();

        // When
        when(this.appUserRepository.findById(id)).thenReturn(Optional.of(appUser));
        when(this.appUserMapper.mapAppUserEntityToDto(appUser)).thenReturn(appUserDto);
        AppUserDto actual = this.appUserService.getUserById(id);

        // Then
        assertSame(appUserDto, actual);
    }

    @Test
    void getUserByIdShouldThrowExceptionWhenUserIsNotPresentTest() {
        // Given
        AppUser appUser = getStaticAppUser();
        AppUserDto appUserDto = getStaticAppUserDto();
        Long id = appUser.getId();

        // When
        when(this.appUserRepository.findById(id)).thenReturn(Optional.empty());

        // Then
        assertThrows(UserNotFoundException.class,
                () -> this.appUserService.getUserById(id)
        );
    }

    @Test
    void modifyUserByIdShouldReturnUserIfIdExistsTest() {
        // Given
        AppUser appUser = getStaticAppUser();
        Address address = appUser.getAddress();
        Address birthAddress = appUser.getPlaceOfBirth();
        AppUserDto expected = getStaticAppUserDto();
        Long id = appUser.getId();
        MultipartFile idDocument = getFileForName("huId.jpg");
        MultipartFile selfiePhoto = getFileForName("huFace.png");
        String idType = "idDocument";
        String userJson = MOCK_USER_JSON;

        // When
        when(this.appUserRepository.findById(id)).thenReturn(Optional.of(appUser));
        when(this.gson.fromJson(userJson, AppUserDto.class)).thenReturn(expected);
        doNothing().when(this.formRecognizerService).validateUser(eq(expected), any(), anyString());
        doNothing().when(this.faceApiService).facesAreMatching(any(), any());
        when(this.addressMapper.mapAddressDtoToEntity(expected.getAddress())).thenReturn(address);
        when(this.addressMapper.mapAddressDtoToEntity(expected.getPlaceOfBirth())).thenReturn(birthAddress);
        when(this.appUserRepository.save(appUser)).thenReturn(appUser);
        when(this.appUserMapper.mapAppUserEntityToDto(appUser)).thenReturn(expected);
        AppUserDto actual = this.appUserService.modifyUserById(idDocument, selfiePhoto, userJson, id, idType);

        // Then
        assertSame(expected, actual);
    }

    @Test
    void modifyUserByIdShouldThrowExceptionWhenIdIsNotPresentTest() {
        // Given
        AppUser appUser = getStaticAppUser();
        Long id = appUser.getId();
        MultipartFile idDocument = getFileForName("huId.jpg");
        MultipartFile selfiePhoto = getFileForName("huFace.png");
        String idType = "idDocument";
        String userJson = MOCK_USER_JSON;

        // When
        when(this.appUserRepository.findById(id)).thenReturn(Optional.empty());

        // Then
        assertThrows(UserNotFoundException.class,
                () -> this.appUserService.modifyUserById(idDocument, selfiePhoto, userJson, id, idType)
        );
    }

    @Test
    void deleteUserByIdShouldReturnUserWhenIdExistsTest() {
        // Given
        AppUser appUser = getStaticAppUser();
        AppUserDto expected = getStaticAppUserDto();
        Long id = appUser.getId();

        // When
        when(this.appUserRepository.findById(id)).thenReturn(Optional.of(appUser));
        when(this.appUserMapper.mapAppUserEntityToDto(appUser)).thenReturn(expected);
        doNothing().when(this.appUserRepository).deleteById(id);
        AppUserDto actual = this.appUserService.deleteUserById(id);

        // Then
        assertSame(expected, actual);
    }

    @Test
    void deleteUserByIdShouldThrowExceptionWhenIdNotExistsTest() {
        // Given
        AppUser appUser = getStaticAppUser();
        Long id = appUser.getId();

        // When
        when(this.appUserRepository.findById(id)).thenReturn(Optional.empty());

        // Then
        assertThrows(UserNotFoundException.class,
                () -> this.appUserService.deleteUserById(id)
        );
    }
}
