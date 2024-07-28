package com.markvarga21.studentmanager.util;

import com.markvarga21.studentmanager.exception.PassportNotFoundException;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * A class which is used to compress images.
 */
@Slf4j
public final class ImageCompressor {
    private ImageCompressor() { }

    /**
     * The scale of which the image is decreased
     * in each step.
     */
    public static final float SCALE_DECREASE_STEP = 0.1F;

    /**
     * The maximum file size accepted by the database in bytes.
     */
    public static final int DEFAULT_SIZE_LIMIT = 4_194_304;

    /**
     * Converts the given image to a byte array.
     *
     * @param bi The image to be converted.
     * @param format The format of the image.
     * @return The byte array of the image.
     * @throws IOException If the image cannot be written.
     */
    @Generated
    public static byte[] toByteArray(
            final BufferedImage bi,
            final String format
    )
            throws IOException {

        ByteArrayOutputStream byteArrayOutputStream =
                new ByteArrayOutputStream();
        ImageIO.write(bi, format, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    /**
     * Compresses the given image.
     *
     * @param image The image to compress.
     * @return The compressed image.
     */
    public static byte[] compressImage(final MultipartFile image) {
        try {
            byte[] imageBytes = image.getBytes();
            long sizeInBytes = imageBytes.length;
            log.info(
                    "File size before compression: {}B",
                    getFileSize(imageBytes)
            );
            if (sizeInBytes <= DEFAULT_SIZE_LIMIT) {
                return imageBytes;
            }

            log.error("File size is too big, compressing...");
            float scale = 1.0F;
            while (sizeInBytes > DEFAULT_SIZE_LIMIT) {
                log.info(
                        "Current file size: {}MB",
                        getFileSize(imageBytes)
                );
                BufferedImage bufferedImage = Thumbnails
                        .of(image.getInputStream())
                        .scale(scale)
                        .asBufferedImage();
                imageBytes = toByteArray(bufferedImage, "jpg");
                sizeInBytes = imageBytes.length;
                scale -= SCALE_DECREASE_STEP;
            }

            log.info(
                    "File size after compression: {}B",
                    getFileSize(imageBytes)
            );

            return imageBytes;
        } catch (IOException e) {
            String message = "The file could not be found.";
            throw new PassportNotFoundException(message);
        }
    }

    /**
     * Returns the size of the given file in MB.
     *
     * @param file The file to get the size of.
     * @return The size of the given file in MB.
     */
    @Generated
    private static String getFileSize(final byte[] file) {
        return String.format(String.valueOf(file.length));
    }
}
