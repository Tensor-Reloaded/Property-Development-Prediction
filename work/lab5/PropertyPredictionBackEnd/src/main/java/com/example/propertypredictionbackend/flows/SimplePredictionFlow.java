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

import static java.lang.System.out;

public class SimplePredictionFlow extends PredictionFlow {

    private final ServerManagerImpl serverManagerInstance = ServerManagerImpl.getInstance();

    @Override
    public void adaptPredictionImage(RequestImageGetter predictionRequest) {
//        Old code that increases the contrast and resizes the image
//        ImagePreProcessor sizePreProcessor = ImagePreProcessorFactory.getInstance().
//                getImagePreProcessor(ImagePreProcessorType.SIZE_PROCESSOR);
//
//        ImagePreProcessor contrastPreProcessor = ImagePreProcessorFactory.getInstance().
//                getImagePreProcessor(ImagePreProcessorType.CONTRAST_PROCESSOR);
//
//        contrastPreProcessor.preProcessImage(predictionRequest);
//
//        sizePreProcessor.preProcessImage(predictionRequest);

        ImagePreProcessor imageCropPreProcessor = ImagePreProcessorFactory.getInstance()
                .getImagePreProcessor(ImagePreProcessorType.CROP_PREPROCESSOR);

        imageCropPreProcessor.preProcessImage(predictionRequest);
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
            return new PredictionResponse.PredictionResponseBuilder()
                    .withImage(receivedResult.toString())
                    .withPredictedPrice(-1)
                    .build();
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
