package com.markvarga21.usermanager.service;

import com.google.gson.Gson;
import com.markvarga21.usermanager.dto.StudentDto;
import com.markvarga21.usermanager.entity.Student;
import com.markvarga21.usermanager.exception.StudentNotFoundException;
import com.markvarga21.usermanager.repository.StudentRepository;
import com.markvarga21.usermanager.service.form.FormRecognizerService;
import com.markvarga21.usermanager.service.faceapi.FaceApiService;
import com.markvarga21.usermanager.service.impl.AppUserServiceImpl;
import com.markvarga21.usermanager.util.mapping.AppUserMapper;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;


import java.util.List;
import java.util.Optional;

/**
 * The app user service tester class.
 */
@ExtendWith(MockitoExtension.class)
class StudentServiceTest {
    /**
     * The app user service mock object.
     */
    @InjectMocks
    private AppUserServiceImpl appUserService;
    /**
     * The app user repository.
     */
    @Mock
    private StudentRepository studentRepository;
    /**
     * The app user mapper.
     */
    @Mock
    private AppUserMapper appUserMapper;

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
        Student student = getStaticAppUser();
        StudentDto studentDto = getStaticAppUserDto();
        List<StudentDto> expected = List.of(studentDto);

        // When
        when(this.studentRepository.findAll())
                .thenReturn(List.of(student));
        when(this.appUserMapper.mapAppUserEntityToDto(any()))
                .thenReturn(studentDto);
        List<StudentDto> actual = this.appUserService.getAllUsers();

        // Then
        assertEquals(expected, actual);
    }

    @Test
    void createUserShouldReturnCreatedUsersWhenDataIsValidTest() {
        // Given
        StudentDto studentDto = getStaticAppUserDto();
        String userJson = MOCK_USER_JSON;
        Student student = getStaticAppUser();
        MultipartFile idDocument = getFileForName("huId.jpg");
        MultipartFile selfiePhoto = getFileForName("huFace.png");

        // When
        when(this.gson.fromJson(userJson, StudentDto.class))
                .thenReturn(studentDto);
        when(this.appUserMapper.mapAppUserDtoToEntity(studentDto))
                .thenReturn(student);
        when(this.appUserMapper.mapAppUserEntityToDto(student))
                .thenReturn(studentDto);
//        doNothing()
//                .when(this.formRecognizerService)
//                .validateUser(eq(appUserDto), any(), anyString());
        doNothing()
                .when(this.faceApiService)
                .facesAreMatching(any(), any());
        when(this.studentRepository.save(student))
                .thenReturn(student);
//        AppUserDto actual = this.appUserService
//                .createUser(idDocument, selfiePhoto, userJson);

        // Then
//        assertEquals(appUserDto, actual);
    }

    @Test
    void createUserShouldThrowExceptionWhenNamesAreInvalid() {
        // Given
        StudentDto studentDto = getStaticAppUserDto();
        String userJson = MOCK_USER_JSON;
        Student student = getStaticAppUser();
        MultipartFile idDocument = getFileForName("huId.jpg");
        MultipartFile selfiePhoto = getFileForName("huFace.png");
        String firstName = studentDto.getFirstName();
        String lastName = studentDto.getLastName();

        // When
        when(this.gson.fromJson(userJson, StudentDto.class))
                .thenReturn(studentDto);
        when(this.studentRepository
                .findStudentByFirstNameAndLastName(firstName, lastName))
                .thenReturn(Optional.of(student));

        // Then
//        assertThrows(InvalidUserException.class,
//                () -> this.appUserService
//                        .createUser(idDocument, selfiePhoto, userJson)
//        );
    }

    @Test
    void validNamesShouldReturnFalseIfNamesAreValidTest() {
        // Given
        String firstName = getStaticAppUser().getFirstName();
        String lastName = getStaticAppUser().getLastName();
        boolean expected = false;

        // When
        when(this.studentRepository
                .findStudentByFirstNameAndLastName(firstName, lastName))
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
        when(this.studentRepository.findStudentByFirstNameAndLastName(
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
        Student student = getStaticAppUser();
        StudentDto studentDto = getStaticAppUserDto();
        Long id = student.getId();

        // When
        when(this.studentRepository.findById(id))
                .thenReturn(Optional.of(student));
        when(this.appUserMapper.mapAppUserEntityToDto(student))
                .thenReturn(studentDto);
        StudentDto actual = this.appUserService.getUserById(id);

        // Then
        assertSame(studentDto, actual);
    }

    @Test
    void getUserByIdShouldThrowExceptionWhenUserIsNotPresentTest() {
        // Given
        Student student = getStaticAppUser();
        Long id = student.getId();

        // When
        when(this.studentRepository.findById(id)).thenReturn(Optional.empty());

        // Then
        assertThrows(StudentNotFoundException.class,
                () -> this.appUserService.getUserById(id)
        );
    }

    @Test
    void modifyUserByIdShouldReturnUserIfIdExistsTest() {
        // Given
        Student student = getStaticAppUser();
        String birthAddress = student.getPlaceOfBirth();
        StudentDto expected = getStaticAppUserDto();
        Long id = student.getId();
        MultipartFile idDocument = getFileForName("huId.jpg");
        MultipartFile selfiePhoto = getFileForName("huFace.png");
        String userJson = MOCK_USER_JSON;

        // When
        when(this.studentRepository.findById(id))
                .thenReturn(Optional.of(student));
        when(this.gson.fromJson(userJson, StudentDto.class))
                .thenReturn(expected);
//        doNothing()
//                .when(this.formRecognizerService)
//                .validateUser(eq(expected), any(), anyString());
//        doNothing()
//                .when(this.faceApiService)
//                .facesAreMatching(any(), any());
//        when(this.addressMapper.mapAddressDtoToEntity(
//                expected.getPlaceOfBirth())
//        )
//                .thenReturn(birthAddress);
        when(this.studentRepository.save(student))
                .thenReturn(student);
        when(this.appUserMapper.mapAppUserEntityToDto(student))
                .thenReturn(expected);
//        AppUserDto actual = this.appUserService
//                .modifyUserById(idDocument, selfiePhoto, userJson, id);
//
//        // Then
//        assertSame(expected, actual);
    }

    @Test
    void modifyUserByIdShouldThrowExceptionWhenIdIsNotPresentTest() {
        // Given
        Student student = getStaticAppUser();
        Long id = student.getId();
        MultipartFile idDocument = getFileForName("huId.jpg");
        MultipartFile selfiePhoto = getFileForName("huFace.png");

        // When
        when(this.studentRepository.findById(id)).thenReturn(Optional.empty());

        // Then
//        assertThrows(UserNotFoundException.class,
//                () -> this.appUserService.modifyUserById(
//                        idDocument,
//                        selfiePhoto,
//                        MOCK_USER_JSON,
//                        id
//                )
//        );
    }

    @Test
    void deleteUserByIdShouldReturnUserWhenIdExistsTest() {
        // Given
        Student student = getStaticAppUser();
        StudentDto expected = getStaticAppUserDto();
        Long id = student.getId();

        // When
        when(this.studentRepository.findById(id))
                .thenReturn(Optional.of(student));
        when(this.appUserMapper.mapAppUserEntityToDto(student))
                .thenReturn(expected);
        doNothing()
                .when(this.studentRepository)
                .deleteById(id);
        StudentDto actual = this.appUserService.deleteUserById(id);

        // Then
        assertSame(expected, actual);
    }

    @Test
    void deleteUserByIdShouldThrowExceptionWhenIdNotExistsTest() {
        // Given
        Student student = getStaticAppUser();
        Long id = student.getId();

        // When
        when(this.studentRepository.findById(id)).thenReturn(Optional.empty());

        // Then
        assertThrows(StudentNotFoundException.class,
                () -> this.appUserService.deleteUserById(id)
        );
    }
}
