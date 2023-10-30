package com.markvarga21.studentmanager.util;

import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

/**
 * A class which is used to compress images.
 */
@Slf4j
public final class ImageCompressor {
    private ImageCompressor() { }

    /**
     * Compresses the given image.
     *
     * @param data The image to be compressed.
     * @return The compressed image.
     */
    public static byte[] compressImage(final byte[] data) {
        log.info("Compressing image...");
        Deflater deflater = new Deflater();
        deflater.setLevel(Deflater.BEST_COMPRESSION);
        deflater.setInput(data);
        deflater.finish();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] tmp = new byte[1024];
        while (!deflater.finished()) {
            int size = deflater.deflate(tmp);
            outputStream.write(tmp, 0, size);
        }
        try {
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return outputStream.toByteArray();
    }

    /**
     * Decompresses the given image.
     *
     * @param data The image to be decompressed.
     * @return The decompressed image.
     */
    public static byte[] decompressImage(final byte[] data) {
        log.info("Decompressing image...");
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] tmp = new byte[1024];
        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(tmp);
                outputStream.write(tmp, 0, count);
            }
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return outputStream.toByteArray();
    }
}
