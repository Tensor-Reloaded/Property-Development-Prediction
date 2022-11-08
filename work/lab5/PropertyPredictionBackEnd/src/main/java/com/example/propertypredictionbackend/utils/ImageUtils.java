package com.example.propertypredictionbackend.utils;

import com.example.propertypredictionbackend.exceptions.ConvertBase64ImageToBufferedImageException;
import com.example.propertypredictionbackend.exceptions.ConvertBufferedImageToBase64ImageException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class ImageUtils {
    public static String convertBufferedImageToBase64Image(BufferedImage bi, String format) {
        try (ByteArrayOutputStream imageByteArray = new ByteArrayOutputStream()) {
            ImageIO.write(bi, format, imageByteArray);
            return new String(Base64.getEncoder().encode(imageByteArray.toByteArray()), StandardCharsets.UTF_8);
        } catch (IOException | IllegalArgumentException e) {
            throw new ConvertBufferedImageToBase64ImageException(e.getMessage());
        }
    }

    public static BufferedImage convertBase64ImageToBufferedImage(String base64Image) {
        BufferedImage image;
        byte[] imageByte = javax.xml.bind.DatatypeConverter.parseBase64Binary(base64Image);
        try (ByteArrayInputStream imageByteStream = new ByteArrayInputStream(imageByte)) {
            image = ImageIO.read(imageByteStream);
            if (image == null) {
                throw new ConvertBase64ImageToBufferedImageException("Null image.");
            }
            return image;
        } catch (IOException | IllegalArgumentException e) {
            throw new ConvertBase64ImageToBufferedImageException(e.getMessage());
        }
    }
}
