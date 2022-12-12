package com.example.propertypredictionbackend.preprocesors;

import com.example.propertypredictionbackend.RequestImageGetter;
import com.example.propertypredictionbackend.exceptions.ConvertBase64ImageToBufferedImageException;
import com.example.propertypredictionbackend.utils.SingletonProvider;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageCropPreProcessor implements ImagePreProcessor {
    private static int CROP_PIXELS = 25;
    private final String FORMAT = "png";

    @Override
    public void preProcessImage(RequestImageGetter requestImageGetter) throws ConvertBase64ImageToBufferedImageException {
        BufferedImage convertedImageFromBase64 = SingletonProvider.getImageUtils().convertBase64ImageToBufferedImage(requestImageGetter.getImage());

        convertedImageFromBase64 = cropLowerPart(convertedImageFromBase64, new Rectangle(convertedImageFromBase64.getWidth()
                , convertedImageFromBase64.getHeight() - CROP_PIXELS));

        String croppedImage = SingletonProvider.getImageUtils().convertBufferedImageToBase64Image(convertedImageFromBase64, FORMAT);

        requestImageGetter.setImage(croppedImage);
    }

    private BufferedImage cropLowerPart(BufferedImage src, Rectangle rect) {
        BufferedImage dest = src.getSubimage(0, 0, rect.width, rect.height);
        return dest;
    }
}
