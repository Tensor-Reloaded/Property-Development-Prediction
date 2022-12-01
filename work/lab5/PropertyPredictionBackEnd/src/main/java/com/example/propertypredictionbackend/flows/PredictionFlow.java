package com.example.propertypredictionbackend.flows;

import com.example.propertypredictionbackend.RequestImageGetter;
import com.example.propertypredictionbackend.dtos.PredictionRequest;
import com.example.propertypredictionbackend.dtos.PredictionResponse;
import com.example.propertypredictionbackend.preprocesors.ImagePreProcessorFactory;

import java.net.URL;
import java.util.UUID;

public abstract class PredictionFlow {

    private final ImagePreProcessorFactory imagePreProcessorFactory = ImagePreProcessorFactory.getInstance();

    public abstract void adaptPredictionImage(RequestImageGetter predictionRequest);

    public abstract UUID sendRequestToModel(URL imagePredictionModelURL, PredictionRequest predictionRequest);

    public abstract PredictionResponse getResponseFromModel(URL imagePredictionModelURL, UUID id);

    public abstract PredictionResponse getDirectResponseFromModel(URL imagePredictionModelURL, PredictionRequest predictionRequest);
}
