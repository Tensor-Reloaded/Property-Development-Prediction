package com.example.propertypredictionbackend.flows;

import com.example.propertypredictionbackend.RequestImageGetter;
import com.example.propertypredictionbackend.dtos.PredictionRequest;
import com.example.propertypredictionbackend.dtos.PredictionResponse;
import com.example.propertypredictionbackend.dtos.http.HttpPredictionResponse;
import com.example.propertypredictionbackend.network.ServerManagerImpl;
import com.example.propertypredictionbackend.network.model.Result;
import com.example.propertypredictionbackend.network.model.ResultError;
import com.example.propertypredictionbackend.network.model.ResultSuccess;
import com.example.propertypredictionbackend.preprocesors.ImagePreProcessor;
import com.example.propertypredictionbackend.preprocesors.ImagePreProcessorFactory;
import com.example.propertypredictionbackend.preprocesors.ImagePreProcessorType;
import com.example.propertypredictionbackend.utils.CommunicationUtils;
import com.google.gson.Gson;

import java.net.URL;
import java.util.UUID;

import static java.lang.System.out;

public class SimplePredictionFlow extends PredictionFlow {

    private final ServerManagerImpl serverManagerInstance = ServerManagerImpl.getInstance();

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

    @Override
    public PredictionResponse getDirectResponseFromModel(URL imagePredictionModelURL, PredictionRequest predictionRequest) {
        Result<String> receivedResult = serverManagerInstance.sendRequest(
                imagePredictionModelURL,
                predictionRequest
        );

        out.println("Received answer from server: " + receivedResult.toString());

        if (receivedResult instanceof ResultError<String>) {
            out.println("Failed request");
//            throw ((ResultError<PredictionResponse>) receivedResult).getException();
            return null;
        } else if (receivedResult instanceof ResultSuccess<String>) {
            out.println("Succeeded request");
            return CommunicationUtils.mapHttpPredictionResponseToPredictionResponse(
                    new Gson().fromJson(((ResultSuccess<String>) receivedResult).getResultData(), HttpPredictionResponse.class));
        }

        out.println("Unknown status");
        //should be thrown an exception
        return null;
    }
}
