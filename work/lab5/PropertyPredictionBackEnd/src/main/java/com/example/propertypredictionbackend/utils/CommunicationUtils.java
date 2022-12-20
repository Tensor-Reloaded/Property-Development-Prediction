package com.example.propertypredictionbackend.utils;

import com.example.propertypredictionbackend.dtos.Coordinate;
import com.example.propertypredictionbackend.dtos.http.HttpPredictionRequest;
import com.example.propertypredictionbackend.dtos.http.HttpPredictionResponse;
import com.example.propertypredictionbackend.dtos.PredictionRequest;
import com.example.propertypredictionbackend.dtos.PredictionResponse;

public class CommunicationUtils {
    public static PredictionRequest mapHttpPredictionRequestToPredictionRequest(HttpPredictionRequest request) {
        return new PredictionRequest.PredictionRequestBuilder()
                .withCoordinate(new Coordinate.CoordinateBuilder()
                        .withLatitude(request.getCoordinates().getLatitude())
                        .withLongitude(request.getCoordinates().getLongitude())
                        .build())
                .withImage(request.getImage())
                .withYearsInFuture(request.getYearsInFuture())
                .build();
    }

    public static HttpPredictionResponse mapPredictionResponseToHttpPredictionResponse(PredictionResponse response) {
        HttpPredictionResponse httpPredictionResponse = new HttpPredictionResponse();
        httpPredictionResponse.setImage(response.getImage());
        httpPredictionResponse.setPredictedPrice(response.getPredictedPrice());
        return httpPredictionResponse;
    }

    public static PredictionResponse mapHttpPredictionResponseToPredictionResponse(HttpPredictionResponse response) {
        return new PredictionResponse.PredictionResponseBuilder()
                .withPredictedPrice(response.getPredictedPrice())
                .withImage(response.getImage())
                .build();
    }
}