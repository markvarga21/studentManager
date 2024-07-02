package com.markvarga21.studentmanager.service.file.impl;

import com.markvarga21.studentmanager.entity.StudentImage;
import com.markvarga21.studentmanager.exception.InvalidDocumentException;
import com.markvarga21.studentmanager.exception.InvalidImageTypeException;
import com.markvarga21.studentmanager.exception.StudentNotFoundException;
import com.markvarga21.studentmanager.repository.StudentImageRepository;
import com.markvarga21.studentmanager.util.StudentImageType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FileUploadServiceImplTest {
    /**
     * The file upload service under testing.
     */
    @InjectMocks
    private FileUploadServiceImpl service;

    /**
     * The repository for the student images.
     */
    @Spy
    private StudentImageRepository repository;

    /**
     * The passport mock image.
     */
    private MockMultipartFile passportImage;

    /**
     * The selfie mock image;
     */
    private MockMultipartFile selfieImage;

    /**
     * The path to the mock image.
     */
    static final String IMAGE_PATH = "images/test-image-for-compression.jpg";

    @BeforeEach
    void setUp() throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(IMAGE_PATH).getFile());
        byte[] fileBytes = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                "image",
                "test-image-for-compression.jpg",
                "image/jpeg",
                fileBytes
        );
        this.selfieImage = mockMultipartFile;
        this.passportImage = mockMultipartFile;
    }

    @Test
    void shouldThrowExceptionWhenPassportIsEmptyTest() {
        this.passportImage = new MockMultipartFile("image", new byte[0]);
        assertThrows(
                InvalidDocumentException.class,
                () -> service.uploadFile(1L, this.passportImage, this.selfieImage)
        );
    }

    @Test
    void shouldThrowExceptionWhenSelfieIsEmptyTest() {
        this.selfieImage = new MockMultipartFile("image", new byte[0]);
        assertThrows(
                InvalidDocumentException.class,
                () -> service.uploadFile(1L, this.passportImage, this.selfieImage)
        );
    }

    @Test
    void shouldUploadFileTest() {
        // Given
        String expected = "Images saved successfully for user '1'";

        // When
        String actual = this.service
                .uploadFile(1L, this.passportImage, this.selfieImage);

        // Then
        verify(this.repository, times(1))
                .save(any());
        assertEquals(expected, actual);
    }

    @Test
    void shouldFetchAllImagesTest() {
        // Given
        // When
        this.service.getAllImages();

        // Then
        verify(this.repository, times(1))
                .findAll();
    }

    @Test
    void shouldDeleteImageByIdIfPresentTest() {
        // Given
        Long studentId = 1L;
        String expected = "Images deleted successfully for user '1'";

        // When
        when(this.repository.findById(studentId))
                .thenReturn(Optional.of(new StudentImage()));
        String actual = this.service.deleteImage(studentId);

        // Then
        verify(this.repository, times(1))
                .deleteStudentImageByStudentId(studentId);
        assertEquals(expected, actual);
    }

    @Test
    void shouldThrowExceptionUponDeletionWhenIdNotPresentTest() {
        // Given
        Long studentId = 1L;

        // When
        when(this.repository.findById(studentId))
                .thenReturn(Optional.empty());

        // Then
        assertThrows(
                StudentNotFoundException.class,
                () -> this.service.deleteImage(studentId)
        );
    }

    @Test
    void shouldThrowExceptionUponGettingImageForTypeWhenTypeIsNullTest() {
        assertThrows(
                InvalidImageTypeException.class,
                () -> this.service.getImageForType(1L, null)
        );
    }

    @Test
    void shouldThrowExceptionUponGettingImageForTypeWhenTypeIsInvalid() {
        assertThrows(
                InvalidImageTypeException.class,
                () -> this.service.getImageForType(1L, StudentImageType.OTHER)
        );
    }

    @Test
    void shouldThrowExceptionUponGettingImageForTypeWhenNotFoundTest() {
        // Given
        Long studentId = 1L;

        // When
        when(this.repository.findById(studentId))
                .thenReturn(Optional.empty());

        // Then
        assertThrows(
                StudentNotFoundException.class,
                () -> this.service.getImageForType(studentId, StudentImageType.PASSPORT)
        );
    }

    @Test
    void shouldGetImageForTypePassportTest() {
        // Given
        Long studentId = 1L;
        StudentImage studentImage = new StudentImage();
        studentImage.setStudentId(studentId);
        studentImage.setPassportImage(new byte[]{1});
        studentImage.setSelfieImage(new byte[]{2});

        // When
        when(this.repository.findById(studentId))
                .thenReturn(Optional.of(studentImage));
        byte[] actual = this.service
                .getImageForType(studentId, StudentImageType.PASSPORT);

        // Then
        assertNotNull(actual);
        assertArrayEquals(studentImage.getPassportImage(), actual);
    }

    @Test
    void shouldGetImageForTypeSelfieTest() {
        // Given
        Long studentId = 1L;
        StudentImage studentImage = new StudentImage();
        studentImage.setStudentId(studentId);
        studentImage.setPassportImage(new byte[]{1});
        studentImage.setSelfieImage(new byte[]{2});

        // When
        when(this.repository.findById(studentId))
                .thenReturn(Optional.of(studentImage));
        byte[] actual = this.service
                .getImageForType(studentId, StudentImageType.SELFIE);

        // Then
        assertNotNull(actual);
        assertArrayEquals(studentImage.getSelfieImage(), actual);
    }

    @Test
    void shouldThrowErrorUponImageChangeIfNullTest() {
        assertThrows(
                InvalidDocumentException.class,
                () -> this.service.changeImage(1L, StudentImageType.PASSPORT, null)
        );
    }

    @Test
    void shouldThrowExceptionUponImageChangeIfTypeIsInvalidTest() {
        when(this.repository.findById(anyLong()))
                .thenReturn(Optional.of(new StudentImage()));
        assertThrows(
                InvalidImageTypeException.class,
                () -> this.service.changeImage(1L, StudentImageType.OTHER, this.passportImage)
        );
    }

    @Test
    void shouldThrowExceptionUponImageChangeIfTypeIsStudentNotPresentTest() {
        when(this.repository.findById(anyLong()))
                .thenReturn(Optional.empty());
        assertThrows(
                StudentNotFoundException.class,
                () -> this.service.changeImage(1L, StudentImageType.PASSPORT, this.passportImage)
        );
    }

    @Test
    void shouldChangeImageForPassportTest() {
        // Given
        Long studentId = 1L;
        StudentImage studentImage = new StudentImage();
        studentImage.setStudentId(studentId);
        studentImage.setPassportImage(new byte[]{1});
        studentImage.setSelfieImage(new byte[]{2});
        String expected = "Passport image changed successfully for user '1'";

        // When
        when(this.repository.findById(studentId))
                .thenReturn(Optional.of(studentImage));
        String actual = this.service
                .changeImage(studentId, StudentImageType.PASSPORT, this.passportImage);

        // Then
        verify(this.repository, times(1))
                .save(studentImage);
        assertEquals(expected, actual);
    }

    @Test
    void shouldChangeImageForSelfieTest() {
        // Given
        Long studentId = 1L;
        StudentImage studentImage = new StudentImage();
        studentImage.setStudentId(studentId);
        studentImage.setPassportImage(new byte[]{1});
        studentImage.setSelfieImage(new byte[]{2});
        String expected = "Selfie image changed successfully for user '1'";

        // When
        when(this.repository.findById(studentId))
                .thenReturn(Optional.of(studentImage));
        String actual = this.service
                .changeImage(studentId, StudentImageType.SELFIE, this.selfieImage);

        // Then
        verify(this.repository, times(1))
                .save(studentImage);
        assertEquals(expected, actual);
    }
}