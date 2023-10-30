package com.markvarga21.studentmanager.util;

import com.markvarga21.studentmanager.exception.PassportNotFoundException;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * A class which is used to compress images.
 */
@Component
@Slf4j
public class ImageCompressor {
    /**
     * The scale of which the scale of the image is decreased.
     */
    public static final float SCALE_DECREASE_STEP = 0.1F;

    /**
     * Converts the given image to a byte array.
     *
     * @param bi The image to convert.
     * @param format The format of the image.
     * @return The byte array of the image.
     * @throws IOException If the image could not be converted.
     */
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
     * @param sizeLimitInBytes The size limit of the image.
     * @return The compressed image.
     */
    public byte[] compressImage(
            final MultipartFile image,
            final long sizeLimitInBytes
    ) {
        try {
            byte[] imageBytes = image.getBytes();
            long sizeInBytes = imageBytes.length;
            log.info(
                    "File size before compression: {}B",
                    getFileSizeInBytes(imageBytes)
            );
            if (sizeInBytes <= sizeLimitInBytes) {
                return imageBytes;
            }

            log.error("File size is too big, compressing...");
            float scale = 1.0F;
            while (sizeInBytes > sizeLimitInBytes) {
                log.info(
                        "Current file size: {}B",
                        getFileSizeInBytes(imageBytes)
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
                    getFileSizeInBytes(imageBytes)
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
    private String getFileSizeInBytes(final byte[] file) {
        double sizeInBytes = file.length;
        return String.valueOf(sizeInBytes);
    }
}
