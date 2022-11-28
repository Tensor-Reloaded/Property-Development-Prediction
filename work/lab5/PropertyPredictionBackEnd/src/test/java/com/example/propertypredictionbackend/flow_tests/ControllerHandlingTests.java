package com.example.propertypredictionbackend.flow_tests;

import com.example.propertypredictionbackend.RequestController;
import com.example.propertypredictionbackend.dtos.http.HttpCoordinates;
import com.example.propertypredictionbackend.dtos.http.HttpPredictionRequest;
import com.example.propertypredictionbackend.exceptions.ConvertBase64ImageToBufferedImageException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.*;

public class ControllerHandlingTests {

    private ApplicationContext context;
    private static final String PATH_IMAGE = "src/test/java/com/example/propertypredictionbackend/preprocessors_tests/test_images/contrastImage.txt";

    @BeforeEach
    public void init() {
        context = new ClassPathXmlApplicationContext("spring.xml");
    }

    @Test
    public void testInvalidDataForFlow() {
        // Arrange
        HttpPredictionRequest request = new HttpPredictionRequest();
        HttpCoordinates coordinates = new HttpCoordinates();
        RequestController controller = (RequestController) context.getBean("controller");
        coordinates.setLatitude(10);
        coordinates.setLongitude(10);
        request.setImage("dsadsad");
        request.setYearsInFuture(-1);
        request.setCoordinates(coordinates);

        // Act + Assert
        Assertions.assertThrows(ConvertBase64ImageToBufferedImageException.class, () -> controller.handleImagePredictionRequest(request));
    }

    @Test
    public void testChangeInvalidYearByMonitor() {
        // Arrange
        HttpPredictionRequest request = new HttpPredictionRequest();
        HttpCoordinates coordinates = new HttpCoordinates();
        RequestController controller = (RequestController) context.getBean("controller");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(PATH_IMAGE)))) {

            coordinates.setLatitude(10);
            coordinates.setLongitude(10);
            request.setYearsInFuture(-1);
            request.setCoordinates(coordinates);
            request.setImage(reader.readLine());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Act
        try {
            controller.handleImagePredictionRequest(request);
        } catch (Exception e) {
            //do nothing
        }
        // Assert
        Assertions.assertEquals(1, request.getYearsInFuture());
    }
}
