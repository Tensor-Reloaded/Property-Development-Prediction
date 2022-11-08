package com.example.propertypredictionbackend.requests_tests;


import com.example.propertypredictionbackend.RequestImageGetter;
import com.example.propertypredictionbackend.RequestPredictionProxy;
import com.example.propertypredictionbackend.dtos.Coordinate;
import com.example.propertypredictionbackend.dtos.PredictionRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

public class RequestImageGetterTests {

    @Test
    public void testGetImageFromAdapterDoNotModifiesRestOfTheDataAndReturnsTheSameImageFromRequest() {
        // Arrange
        Coordinate coordinate = new Coordinate.CoordinateBuilder()
                .withLatitude(43.464868f)
                .withLongitude(54.3161f)
                .build();
        PredictionRequest request = new PredictionRequest.PredictionRequestBuilder()
                .withImage("d93408wj8e849r0jceo8r9sf0ucdjerfs980usjer0fc9e8prsjc09w34uf=")
                .withYearsInFuture(5)
                .withCoordinate(coordinate).build();

        RequestImageGetter adapter = new RequestPredictionProxy(request);

        // Act
        String imageFromAdaptorBase64 = adapter.getImage();

        // Assert
        Assertions.assertEquals("d93408wj8e849r0jceo8r9sf0ucdjerfs980usjer0fc9e8prsjc09w34uf=", imageFromAdaptorBase64);
        Assertions.assertEquals("d93408wj8e849r0jceo8r9sf0ucdjerfs980usjer0fc9e8prsjc09w34uf=", request.getImage());
        Assertions.assertEquals(5, request.getYearsInFuture());
        Assertions.assertEquals(coordinate, request.getCoordinates());
        Assertions.assertEquals(43.464868f, request.getCoordinates().getLatitude());
        Assertions.assertEquals(54.3161f, request.getCoordinates().getLongitude());
    }

    @Test
    public void testSetImageFromAdapterDoNotModifiesRestOfTheDataAndReturnsTheSameImageFromRequest() {
        // Arrange
        String newImageBase64 = "cap9uw8no9cpe8hwo8fehswnocr89ps3od2qe=";
        Coordinate coordinate = new Coordinate.CoordinateBuilder()
                .withLatitude(43.464868f)
                .withLongitude(54.3161f)
                .build();
        PredictionRequest request = new PredictionRequest.PredictionRequestBuilder()
                .withImage("d93408wj8e849r0jceo8r9sf0ucdjerfs980usjer0fc9e8prsjc09w34uf=")
                .withYearsInFuture(5)
                .withCoordinate(coordinate).build();

        RequestImageGetter adapter = new RequestPredictionProxy(request);

        // Act
        adapter.setImage(newImageBase64);
        String imageFromAdaptorBase64 = adapter.getImage();

        // Assert
        Assertions.assertEquals("cap9uw8no9cpe8hwo8fehswnocr89ps3od2qe=", imageFromAdaptorBase64);
        Assertions.assertEquals("cap9uw8no9cpe8hwo8fehswnocr89ps3od2qe=", request.getImage());
        Assertions.assertEquals(5, request.getYearsInFuture());
        Assertions.assertEquals(coordinate, request.getCoordinates());
        Assertions.assertEquals(43.464868f, request.getCoordinates().getLatitude());
        Assertions.assertEquals(54.3161f, request.getCoordinates().getLongitude());
    }
}
