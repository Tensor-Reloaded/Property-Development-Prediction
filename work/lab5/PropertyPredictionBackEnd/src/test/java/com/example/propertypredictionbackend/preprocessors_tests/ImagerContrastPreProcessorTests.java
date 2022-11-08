package com.example.propertypredictionbackend.preprocessors_tests;

import com.example.propertypredictionbackend.RequestImageGetter;
import com.example.propertypredictionbackend.RequestPredictionProxy;
import com.example.propertypredictionbackend.exceptions.ConvertBase64ImageToBufferedImageException;
import com.example.propertypredictionbackend.dtos.Coordinate;
import com.example.propertypredictionbackend.dtos.PredictionRequest;
import com.example.propertypredictionbackend.preprocesors.ImageContrastPreProcessor;
import com.example.propertypredictionbackend.preprocesors.ImagePreProcessor;
import com.example.propertypredictionbackend.utils.ImageUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.awt.image.BufferedImage;
import java.io.*;

public class ImagerContrastPreProcessorTests {

    private static ImagePreProcessor preProcessor;
    private static final String PATH_IMAGE = "src/test/java/com/example/propertypredictionbackend/preprocessors_tests/test_images/contrastImage.txt";
    private static final String PATH_INVALID_IMAGE = "src/test/java/com/example/propertypredictionbackend/preprocessors_tests/test_images/invalidTextImage.txt";

    @BeforeAll
    public static void init() {
        preProcessor = new ImageContrastPreProcessor();
    }

    @Test
    public void testContrastPreProcessorAspect() {
        // Arrange
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(PATH_IMAGE)))) {
            ApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");
            ImagePreProcessor preProcessorLocal = (ImagePreProcessor) context.getBean("contrastPreProcessor");
            String image = reader.readLine();
            Coordinate coordinate = new Coordinate.CoordinateBuilder()
                    .withLatitude(43.464868f)
                    .withLongitude(54.3161f)
                    .build();
            PredictionRequest request = new PredictionRequest.PredictionRequestBuilder()
                    .withImage(image)
                    .withYearsInFuture(5)
                    .withCoordinate(coordinate).build();
            RequestImageGetter adapter = new RequestPredictionProxy(request);

            // Act
            preProcessorLocal.preProcessImage(adapter);

            // Assert
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    public void testContrastSuccessful() {
        //Arrange
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(PATH_IMAGE)))) {
            String image = reader.readLine();
            Coordinate coordinate = new Coordinate.CoordinateBuilder()
                    .withLatitude(43.464868f)
                    .withLongitude(54.3161f)
                    .build();
            PredictionRequest request = new PredictionRequest.PredictionRequestBuilder()
                    .withImage(image)
                    .withYearsInFuture(5)
                    .withCoordinate(coordinate).build();
            RequestImageGetter adapter = new RequestPredictionProxy(request);

            // Act
            preProcessor.preProcessImage(adapter);
            BufferedImage imageBufferedTest = ImageUtils.convertBase64ImageToBufferedImage(adapter.getImage());
            BufferedImage oldImageBufferedTest = ImageUtils.convertBase64ImageToBufferedImage(adapter.getImage());

            //Just for visual checking
//            File fileOutput = new File("src/test/java/com/example/propertypredictionbackend/preprocessors_tests/test_images/new.jpg");
//            ImageIO.write(imageBufferedTest, "jpeg", fileOutput);

            // Assert
            Assertions.assertEquals(imageBufferedTest.getHeight(), oldImageBufferedTest.getHeight());
            Assertions.assertEquals(imageBufferedTest.getWidth(), oldImageBufferedTest.getWidth());
            for (int length = 0; length < imageBufferedTest.getWidth(); length++) {
                for (int heigth = 0; heigth < imageBufferedTest.getHeight(); heigth++) {
                    Assertions.assertTrue(oldImageBufferedTest.getRGB(length, heigth) <= imageBufferedTest.getRGB(length, heigth));
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testExceptionThrownOnInvalidBase64() {
        // Arrange
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(PATH_INVALID_IMAGE)))) {
            String invalidImage = reader.readLine();
            Coordinate coordinate = new Coordinate.CoordinateBuilder()
                    .withLatitude(43.464868f)
                    .withLongitude(54.3161f)
                    .build();
            PredictionRequest request = new PredictionRequest.PredictionRequestBuilder()
                    .withImage(invalidImage)
                    .withYearsInFuture(5)
                    .withCoordinate(coordinate).build();
            RequestImageGetter adapter = new RequestPredictionProxy(request);

            // Act

            // Assert
            Assertions.assertThrows(ConvertBase64ImageToBufferedImageException.class,
                    () -> preProcessor.preProcessImage(adapter));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
