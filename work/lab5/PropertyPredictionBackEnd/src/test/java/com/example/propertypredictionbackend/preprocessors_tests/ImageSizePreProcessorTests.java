package com.example.propertypredictionbackend.preprocessors_tests;

import com.example.propertypredictionbackend.RequestImageGetter;
import com.example.propertypredictionbackend.RequestPredictionProxy;
import com.example.propertypredictionbackend.exceptions.ConvertBase64ImageToBufferedImageException;
import com.example.propertypredictionbackend.http_predictions.Coordinate;
import com.example.propertypredictionbackend.http_predictions.PredictionRequest;
import com.example.propertypredictionbackend.preprocesors.ImagePreProcessor;
import com.example.propertypredictionbackend.preprocesors.ImageSizePreProcessor;
import com.example.propertypredictionbackend.utils.ImageUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;
import java.io.*;

public class ImageSizePreProcessorTests {
    private static ImagePreProcessor preProcessor;
    private static final String PATH_VALID_IMAGE = "src/test/java/com/example/propertypredictionbackend/preprocessors_tests/textBase.txt";
    private static final String PATH_INVALID_IMAGE = "src/test/java/com/example/propertypredictionbackend/preprocessors_tests/invalidTextImage.txt";

    @BeforeAll
    public static void init() {
        preProcessor = new ImageSizePreProcessor();
    }

    @Test
    public void testSuccessfullyResizedImage() {
        // Arrange
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(PATH_VALID_IMAGE)))) {
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

            // Assert
            Assertions.assertEquals(240, imageBufferedTest.getHeight());
            Assertions.assertEquals(240, imageBufferedTest.getWidth());

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
