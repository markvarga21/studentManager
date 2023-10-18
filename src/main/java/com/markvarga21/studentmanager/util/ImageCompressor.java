package com.markvarga21.studentmanager.util;

import com.markvarga21.studentmanager.exception.PassportNotFoundException;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
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
     * The size of the compressed image in bytes.
     */
    @Value("${size.limit.bytes}")
    private String sizeLimit;

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
     * @return The compressed image.
     */
    public byte[] compressImage(final MultipartFile image) {
        int sizeLimitInt = Integer.parseInt(this.sizeLimit);
        try {
            byte[] imageBytes = image.getBytes();
            long sizeInBytes = imageBytes.length;
            log.info(
                    "File size before compression: {}MB",
                    getFileSize(imageBytes)
            );
            if (sizeInBytes <= sizeLimitInt) {
                return imageBytes;
            }

            log.error("File size is too big, compressing...");
            float scale = 1.0F;
            while (sizeInBytes > sizeLimitInt) {
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
                    "File size after compression: {}MB",
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
    private String getFileSize(final byte[] file) {
        double sizeInBytes = file.length;
        double sizeInMb = sizeInBytes / Integer.parseInt(this.sizeLimit);
        return String.format("%.2f", sizeInMb);
    }
}
