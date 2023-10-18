package com.markvarga21.studentmanager.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

/**
 * A util class for fetching files from the
 * resources folder into a {@code MultiPartFile}.
 */
@Slf4j
public final class FileFetcher {
    /**
     * Fetches a file from the applications tests resources folder.
     * 
     * @param fileName the name of the file.
     * @return the created and configured {@code MultiPartFile}.
     */
    public static MultipartFile getFileForName(final String fileName) {
        try (InputStream inputStream = FileFetcher.class
                .getClassLoader()
                .getResourceAsStream(fileName)
        ) {
            return new MockMultipartFile(fileName, inputStream);
        } catch (IOException exception) {
            String message = String.format(
                    "Something went wrong when reading '%s'",
                    fileName
            );
            log.error(message);
        }
        return null;
    }

    private FileFetcher() { }
}
