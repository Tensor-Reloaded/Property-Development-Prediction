package com.example.propertypredictionbackend.network_tests;

import com.example.propertypredictionbackend.RequestController;
import com.example.propertypredictionbackend.dtos.http.HttpCoordinates;
import com.example.propertypredictionbackend.dtos.http.HttpPredictionRequest;
import com.example.propertypredictionbackend.dtos.http.HttpPredictionResponse;
import com.example.propertypredictionbackend.exceptions.ConvertBase64ImageToBufferedImageException;
import com.example.propertypredictionbackend.network.ServerManager;
import com.example.propertypredictionbackend.network.model.Result;
import com.example.propertypredictionbackend.network.model.ResultError;
import com.example.propertypredictionbackend.network.model.ResultSuccess;
import com.example.propertypredictionbackend.utils.ImageUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.*;
import java.net.URL;
import java.util.Objects;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

public class ServerTests {
    private ServerManager server;
    private URL imagePredictionModelURL;
    private ApplicationContext context;
    private static final String PATH_IMAGE = "src/test/java/com/example/propertypredictionbackend/preprocessors_tests/test_images/test_image.txt";
    private String image;

    @BeforeEach
    public void init() {

        try (InputStream fileStream = getClass().getClassLoader().getResourceAsStream("static/string_constants.properties")) {
            ResourceBundle bundle = new PropertyResourceBundle(new InputStreamReader(Objects.requireNonNull(fileStream)));
            String baseURL = bundle.getString("url.backend.model");
            this.imagePredictionModelURL = new URL(baseURL);
            context = new ClassPathXmlApplicationContext("spring.xml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(PATH_IMAGE)))) {
            image = reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This test MUST be executed when both the API for the model and this back-end application runs
     */
    @Test
    public void testSuccessRequestToTheServer() {
        // Arrange
        HttpPredictionRequest request = new HttpPredictionRequest();
        HttpCoordinates coordinates = new HttpCoordinates();
        server = (ServerManager) context.getBean("serverBean");
        coordinates.setLatitude(10);
        coordinates.setLongitude(10);
        request.setImage(image);
        request.setYearsInFuture(1);
        request.setCoordinates(coordinates);

        // Act
        Result<String> result = server.sendRequest(imagePredictionModelURL, request);
//        HttpPredictionResponse response = controller.handleImagePredictionRequest(request);

        // Assert
        Assertions.assertInstanceOf(ResultSuccess.class, result);
    }

    @Test
    public void testFailedRequestToTheServer() {
        // Arrange
        HttpPredictionRequest request = new HttpPredictionRequest();
        HttpCoordinates coordinates = new HttpCoordinates();
        server = (ServerManager) context.getBean("serverBean");
        coordinates.setLatitude(10);
        coordinates.setLongitude(10);
        request.setImage(image);
        request.setYearsInFuture(1);
        request.setCoordinates(coordinates);

        // Act
        Result<String> result = server.sendRequest(imagePredictionModelURL, request);

        // Assert
        Assertions.assertInstanceOf(ResultError.class, result);
        Assertions.assertTrue(((ResultError<?>) result).getThrowable().getMessage().contains("java.net.ConnectException"));
    }

    @Test
    public void testErrorReturnedFromModelAPI() {
        // Arrange
        HttpPredictionRequest request = new HttpPredictionRequest();
        HttpCoordinates coordinates = new HttpCoordinates();
        RequestController controller = (RequestController) context.getBean("controller");
        coordinates.setLatitude(10);
        coordinates.setLongitude(10);
        request.setImage(image);
        request.setYearsInFuture(1);
        request.setCoordinates(coordinates);

        // Act
        HttpPredictionResponse response = controller.handleImagePredictionRequest(request);

        // Assert
        Assertions.assertTrue(0 > response.getPredictedPrice());
        Assertions.assertThrows(ConvertBase64ImageToBufferedImageException.class,
                () -> (new ImageUtils()).convertBase64ImageToBufferedImage(response.getImage()));
    }
}
