package com.markvarga21.studentmanager.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class ImageCompressorTest {
    /**
     * The path to the image to be compressed.
     */
    static final String IMAGE_PATH = "images/test-image-for-compression.jpg";

    /**
     * The absolute path to the image to be compressed.
     */
    private String absolutePath = "";

    @BeforeEach
    void setUp() {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(IMAGE_PATH).getFile());
        this.absolutePath = file.getAbsolutePath();
    }

    @Test
    void shouldCompressImage() throws IOException {
        // Given
        byte[] imageBytes = Files.readAllBytes(Paths.get(absolutePath));
        MockMultipartFile image = new MockMultipartFile(
                "image",
                "test-image-for-compression.jpg",
                "image/jpeg",
                imageBytes
        );

        // When
        byte[] compressedImage = ImageCompressor.compressImage(image);

        // Then
        assertNotNull(compressedImage);
        assertTrue(compressedImage.length < ImageCompressor.DEFAULT_SIZE_LIMIT);
    }
}