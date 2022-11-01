package com.example.propertypredictionbackend.preprocesors;

import com.example.propertypredictionbackend.RequestImageGetter;
import com.example.propertypredictionbackend.exceptions.ConvertBase64ImageToBufferedImageException;

public interface ImagePreProcessor {

    void preProcessImage(RequestImageGetter requestImageGetter) throws ConvertBase64ImageToBufferedImageException;
}
