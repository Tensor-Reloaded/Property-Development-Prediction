package com.example.propertypredictionbackend.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SingletonProviderTests {

    @Test
    public void testReturnsSameInstanceWhenGetInstance() {
        // Arrange
        ImageUtils firstImageUtils = SingletonProvider.getImageUtils();
        ImageUtils secondImageUtils = SingletonProvider.getImageUtils();

        // Act + Assert
        Assertions.assertEquals(firstImageUtils, secondImageUtils);
    }
}
