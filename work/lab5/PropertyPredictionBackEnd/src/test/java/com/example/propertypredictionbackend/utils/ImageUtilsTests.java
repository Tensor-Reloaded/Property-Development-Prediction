package com.example.propertypredictionbackend.utils;

import com.example.propertypredictionbackend.exceptions.ConvertBase64ImageToBufferedImageException;
import com.example.propertypredictionbackend.exceptions.NotBase64ImageException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class ImageUtilsTests {

    private static ApplicationContext context;

    @BeforeAll
    public static void init() {
        context = new ClassPathXmlApplicationContext("spring.xml");
    }

    @Test
    public void testMonitorThrowsNotBase64ImageExceptionWhenConvertBase64ImageToBufferedImageGivenInvalidImage() {
        // Arrange
        ImageUtils imageUtils = (ImageUtils) context.getBean("imageUtils");
        String invalidImagePath = "src/test/java/com/example/propertypredictionbackend/preprocessors_tests/test_images/invalidTextImage.txt";

        String image = null;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(invalidImagePath)))) {
            image = reader.readLine();
        } catch (IOException e) {
            Assertions.fail();
        }
        // Act + Assert
        String finalImage = image;
        Assertions.assertThrows(NotBase64ImageException.class, () -> {
            imageUtils.convertBase64ImageToBufferedImage(finalImage);
        });

        // Assert
    }

    @Test
    public void testImageUtilsThrowsConvertBase64ImageToBufferedImageExceptionWhenConvertBase64ImageToBufferedImageGivenNullImage() {

        // Arrange
        ImageUtils imageUtils = SingletonProvider.getImageUtils();

        String imageBase64 = null;

        try {
            imageUtils.convertBase64ImageToBufferedImage(imageBase64);
        } catch (ConvertBase64ImageToBufferedImageException exception) {
            Assertions.assertEquals("Error when converting a base64 representation of image to BufferedImage. Actual exception: Null base64.", exception.getMessage());
            return;
        }
        // Assert
        Assertions.fail("Should have thrown ConvertBase64ImageToBufferedImageException!");
    }
}
