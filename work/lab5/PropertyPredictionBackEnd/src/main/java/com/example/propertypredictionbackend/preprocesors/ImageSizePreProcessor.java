package com.example.propertypredictionbackend.preprocesors;

import com.example.propertypredictionbackend.RequestImageGetter;
import com.example.propertypredictionbackend.exceptions.ConvertBase64ImageToBufferedImageException;
import com.example.propertypredictionbackend.utils.ImageUtils;
import org.imgscalr.Scalr;

import java.awt.image.BufferedImage;

public class ImageSizePreProcessor implements ImagePreProcessor {
    private final int HEIGHT = 240; // to be read from properties
    private final int WIDTH = 240; // to be read from properties
    private final String FORMAT = "png";

    private BufferedImage resizeImage(BufferedImage originalImage) {
        return Scalr.resize(originalImage, Scalr.Method.AUTOMATIC, Scalr.Mode.AUTOMATIC, HEIGHT, WIDTH, Scalr.OP_ANTIALIAS);
    }

    @Override
    public void preProcessImage(RequestImageGetter requestImageGetter) throws ConvertBase64ImageToBufferedImageException {
        BufferedImage convertedImageFromBase64 = ImageUtils.convertBase64ImageToBufferedImage(requestImageGetter.getImage());

        BufferedImage resizedImage = resizeImage(convertedImageFromBase64);

        String convertedResizedImage = ImageUtils.convertBufferedImageToBase64Image(resizedImage, FORMAT);

        requestImageGetter.setImage(convertedResizedImage);
    }
}
