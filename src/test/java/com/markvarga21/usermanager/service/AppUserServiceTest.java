package com.markvarga21.usermanager.service;

import com.google.gson.Gson;
import com.markvarga21.usermanager.dto.AppUserDto;
import com.markvarga21.usermanager.entity.Address;
import com.markvarga21.usermanager.entity.AppUser;
import com.markvarga21.usermanager.exception.InvalidUserException;
import com.markvarga21.usermanager.exception.UserNotFoundException;
import com.markvarga21.usermanager.repository.AppUserRepository;
import com.markvarga21.usermanager.service.azure.FormRecognizerService;
import com.markvarga21.usermanager.service.faceapi.FaceApiService;
import com.markvarga21.usermanager.service.impl.AppUserServiceImpl;
import com.markvarga21.usermanager.util.mapping.AddressMapper;
import com.markvarga21.usermanager.util.mapping.AppUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import static com.markvarga21.usermanager.util.FileFetcher.getFileForName;
import static com.markvarga21.usermanager.util.MockDataProvider.MOCK_USER_JSON;
import static com.markvarga21.usermanager.util.MockDataProvider.getStaticAppUser;
import static com.markvarga21.usermanager.util.MockDataProvider.getStaticAppUserDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;


import java.util.List;
import java.util.Optional;

/**
 * The app user service tester class.
 */
@ExtendWith(MockitoExtension.class)
class AppUserServiceTest {
    /**
     * The app user service mock object.
     */
    @InjectMocks
    private AppUserServiceImpl appUserService;
    /**
     * The app user repository.
     */
    @Mock
    private AppUserRepository appUserRepository;
    /**
     * The app user mapper.
     */
    @Mock
    private AppUserMapper appUserMapper;
    /**
     * The address mapper.
     */
    @Mock
    private AddressMapper addressMapper;
    /**
     * The form recognizer service.
     */
    @Mock
    private FormRecognizerService formRecognizerService;
    /**
     * The GSON deserializer.
     */
    @Mock
    private Gson gson;
    /**
     * The Face API service.
     */
    @Mock
    private FaceApiService faceApiService;

    @Test
    void getAllUsersShouldReturnListOfUsersTest() {
        // Given
        AppUser appUser = getStaticAppUser();
        AppUserDto appUserDto = getStaticAppUserDto();
        List<AppUserDto> expected = List.of(appUserDto);

        // When
        when(this.appUserRepository.findAll())
                .thenReturn(List.of(appUser));
        when(this.appUserMapper.mapAppUserEntityToDto(any()))
                .thenReturn(appUserDto);
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
        when(this.gson.fromJson(userJson, AppUserDto.class))
                .thenReturn(appUserDto);
        when(this.appUserMapper.mapAppUserDtoToEntity(appUserDto))
                .thenReturn(appUser);
        when(this.appUserMapper.mapAppUserEntityToDto(appUser))
                .thenReturn(appUserDto);
        doNothing()
                .when(this.formRecognizerService)
                .validateUser(eq(appUserDto), any(), anyString());
        doNothing()
                .when(this.faceApiService)
                .facesAreMatching(any(), any());
        when(this.appUserRepository.save(appUser))
                .thenReturn(appUser);
        AppUserDto actual = this.appUserService
                .createUser(idDocument, selfiePhoto, userJson, idType);

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
        when(this.gson.fromJson(userJson, AppUserDto.class))
                .thenReturn(appUserDto);
        when(this.appUserRepository
                .findAppUserByFirstNameAndLastName(firstName, lastName))
                .thenReturn(Optional.of(appUser));

        // Then
        assertThrows(InvalidUserException.class,
                () -> this.appUserService
                        .createUser(idDocument, selfiePhoto, userJson, idType)
        );
    }

    @Test
    void validNamesShouldReturnFalseIfNamesAreValidTest() {
        // Given
        String firstName = getStaticAppUser().getFirstName();
        String lastName = getStaticAppUser().getLastName();
        boolean expected = false;

        // When
        when(this.appUserRepository
                .findAppUserByFirstNameAndLastName(firstName, lastName))
                .thenReturn(Optional.of(getStaticAppUser()));
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
        when(this.appUserRepository.findAppUserByFirstNameAndLastName(
                firstName, lastName
        ))
                .thenReturn(Optional.empty());
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
        when(this.appUserRepository.findById(id))
                .thenReturn(Optional.of(appUser));
        when(this.appUserMapper.mapAppUserEntityToDto(appUser))
                .thenReturn(appUserDto);
        AppUserDto actual = this.appUserService.getUserById(id);

        // Then
        assertSame(appUserDto, actual);
    }

    @Test
    void getUserByIdShouldThrowExceptionWhenUserIsNotPresentTest() {
        // Given
        AppUser appUser = getStaticAppUser();
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
        when(this.appUserRepository.findById(id))
                .thenReturn(Optional.of(appUser));
        when(this.gson.fromJson(userJson, AppUserDto.class))
                .thenReturn(expected);
        doNothing()
                .when(this.formRecognizerService)
                .validateUser(eq(expected), any(), anyString());
        doNothing()
                .when(this.faceApiService)
                .facesAreMatching(any(), any());
        when(this.addressMapper.mapAddressDtoToEntity(expected.getAddress()))
                .thenReturn(address);
        when(this.addressMapper.mapAddressDtoToEntity(
                expected.getPlaceOfBirth())
        )
                .thenReturn(birthAddress);
        when(this.appUserRepository.save(appUser))
                .thenReturn(appUser);
        when(this.appUserMapper.mapAppUserEntityToDto(appUser))
                .thenReturn(expected);
        AppUserDto actual = this.appUserService
                .modifyUserById(idDocument, selfiePhoto, userJson, id, idType);

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

        // When
        when(this.appUserRepository.findById(id)).thenReturn(Optional.empty());

        // Then
        assertThrows(UserNotFoundException.class,
                () -> this.appUserService.modifyUserById(
                        idDocument,
                        selfiePhoto,
                        MOCK_USER_JSON,
                        id,
                        idType
                )
        );
    }

    @Test
    void deleteUserByIdShouldReturnUserWhenIdExistsTest() {
        // Given
        AppUser appUser = getStaticAppUser();
        AppUserDto expected = getStaticAppUserDto();
        Long id = appUser.getId();

        // When
        when(this.appUserRepository.findById(id))
                .thenReturn(Optional.of(appUser));
        when(this.appUserMapper.mapAppUserEntityToDto(appUser))
                .thenReturn(expected);
        doNothing()
                .when(this.appUserRepository)
                .deleteById(id);
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
