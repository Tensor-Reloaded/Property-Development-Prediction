package com.example.propertypredictionbackend.flows;

import com.example.propertypredictionbackend.RequestImageGetter;
import com.example.propertypredictionbackend.http_predictions.PredictionRequest;
import com.example.propertypredictionbackend.http_predictions.PredictionResponse;
import com.example.propertypredictionbackend.preprocesors.ImagePreProcessor;
import com.example.propertypredictionbackend.preprocesors.ImagePreProcessorFactory;
import com.example.propertypredictionbackend.preprocesors.ImagePreProcessorType;

import java.net.URL;
import java.util.UUID;

public class SimplePredictionFlow extends PredictionFlow {
    @Override
    public void adaptPredictionImage(RequestImageGetter predictionRequest) {

        ImagePreProcessor sizePreProcessor = ImagePreProcessorFactory.getInstance().
                getImagePreProcessor(ImagePreProcessorType.SIZE_PROCESSOR);

        ImagePreProcessor contrastPreProcessor = ImagePreProcessorFactory.getInstance().
                getImagePreProcessor(ImagePreProcessorType.CONTRAST_PROCESSOR);

        contrastPreProcessor.preProcessImage(predictionRequest);

        sizePreProcessor.preProcessImage(predictionRequest);

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
