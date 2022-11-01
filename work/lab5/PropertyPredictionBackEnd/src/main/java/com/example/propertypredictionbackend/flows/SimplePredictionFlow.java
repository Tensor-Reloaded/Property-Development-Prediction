package com.example.propertypredictionbackend.flows;

import com.example.propertypredictionbackend.RequestImageGetter;
import com.example.propertypredictionbackend.http_predictions.PredictionRequest;
import com.example.propertypredictionbackend.http_predictions.PredictionResponse;

import java.net.URL;
import java.util.UUID;

public class SimplePredictionFlow extends PredictionFlow {
    @Override
    public void adaptPredictionImage(RequestImageGetter predictionRequest) {

    }

    @Override
    public UUID sendRequestToModel(URL imagePredictionModelURL, PredictionRequest predictionRequest) {
        return null;
    }

    @Override
    public PredictionResponse getResponseFromModel(URL imagePredictionModelURL, UUID id) {
        return null;
    }
}
