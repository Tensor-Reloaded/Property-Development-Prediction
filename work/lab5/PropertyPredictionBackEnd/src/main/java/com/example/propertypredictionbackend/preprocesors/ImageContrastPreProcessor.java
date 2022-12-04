package com.example.propertypredictionbackend.preprocesors;

import com.example.propertypredictionbackend.RequestImageGetter;
import com.example.propertypredictionbackend.utils.SingletonProvider;
import com.example.propertypredictionbackend.exceptions.ConvertBase64ImageToBufferedImageException;

import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;

public class ImageContrastPreProcessor implements ImagePreProcessor {

    private final float scaleFactor = 1.2f;
    private final int offset = 15;
    private final String FORMAT = "png";

    @Override
    public void preProcessImage(RequestImageGetter requestImageGetter) throws ConvertBase64ImageToBufferedImageException {

        BufferedImage convertedImageFromBase64 = SingletonProvider.getImageUtils().convertBase64ImageToBufferedImage(requestImageGetter.getImage());

        RescaleOp rescaleOp = new RescaleOp(scaleFactor, offset, null);

        BufferedImage contrastImage = rescaleOp.filter(convertedImageFromBase64, null);

        String convertedResizedImage = SingletonProvider.getImageUtils().convertBufferedImageToBase64Image(contrastImage, FORMAT);

        requestImageGetter.setImage(convertedResizedImage);
    }
}
