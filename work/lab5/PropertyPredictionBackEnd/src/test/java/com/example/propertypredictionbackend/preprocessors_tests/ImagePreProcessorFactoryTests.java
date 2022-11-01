package com.example.propertypredictionbackend.preprocessors_tests;

import com.example.propertypredictionbackend.exceptions.NotFoundPreProcessorException;
import com.example.propertypredictionbackend.preprocesors.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ImagePreProcessorFactoryTests {

    @Test
    public void testGetInstanceOfImagePreProcessorsFactory() {
        // Arrange

        // Act
        ImagePreProcessorFactory firstInstance = ImagePreProcessorFactory.getInstance();
        ImagePreProcessorFactory secondInstance = ImagePreProcessorFactory.getInstance();

        // Assert
        Assertions.assertNotEquals(null, firstInstance);
        Assertions.assertNotEquals(null, secondInstance);
        Assertions.assertEquals(firstInstance, secondInstance);
    }

    @Test
    public void testGetContrastPreProcessorBasedOnTheTypeContrastPreprocessor() {
        // Arrange
        ImagePreProcessorFactory factory = ImagePreProcessorFactory.getInstance();

        // Act
        ImagePreProcessor contrastPreProcessor = factory.getImagePreProcessor(ImagePreProcessorType.CONTRAST_PROCESSOR);

        // Assert
        Assertions.assertEquals(ImageContrastPreProcessor.class, contrastPreProcessor.getClass());
    }

    @Test
    public void testGetSizePreProcessorBasedOnTheTypeSizePreprocessor() {
        // Arrange
        ImagePreProcessorFactory factory = ImagePreProcessorFactory.getInstance();

        // Act
        ImagePreProcessor sizePreProcessor = factory.getImagePreProcessor(ImagePreProcessorType.SIZE_PROCESSOR);

        // Assert
        Assertions.assertEquals(ImageSizePreProcessor.class, sizePreProcessor.getClass());
    }

    @Test
    public void testGetPreProcessorGivenNullType() {
        // Arrange
        ImagePreProcessorFactory factory = ImagePreProcessorFactory.getInstance();

        // Act

        // Assert
        Assertions.assertThrows(NotFoundPreProcessorException.class, () -> factory.getImagePreProcessor(null));
    }
}
