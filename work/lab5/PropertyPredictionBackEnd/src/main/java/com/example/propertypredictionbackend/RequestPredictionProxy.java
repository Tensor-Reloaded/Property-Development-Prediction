package com.example.propertypredictionbackend;

import com.example.propertypredictionbackend.dtos.PredictionRequest;

import java.lang.reflect.Field;

public class RequestPredictionProxy implements RequestImageGetter {

    private PredictionRequest predictionRequest;

    public RequestPredictionProxy(PredictionRequest predictionRequest) {
        this.predictionRequest = predictionRequest;
    }

    @Override
    public String getImage() {
        return this.predictionRequest.getImage();
    }

    @Override
    public void setImage(String image) {
        try {
            Field fieldImage = predictionRequest.getClass().getDeclaredField("image");
            fieldImage.setAccessible(true);
            fieldImage.set(predictionRequest, image);
            fieldImage.setAccessible(false);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
