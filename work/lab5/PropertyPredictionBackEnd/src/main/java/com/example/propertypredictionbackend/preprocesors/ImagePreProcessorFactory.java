package com.example.propertypredictionbackend.preprocesors;

import com.example.propertypredictionbackend.exceptions.NotFoundPreProcessorException;

import java.util.HashMap;
import java.util.Map;

public class ImagePreProcessorFactory {

    private static ImagePreProcessorFactory instance;

    private final Map<ImagePreProcessorType, ImagePreProcessor> imagePreProcessorMap;

    private ImagePreProcessorFactory() {

        imagePreProcessorMap = new HashMap<>();
        imagePreProcessorMap.put(ImagePreProcessorType.CONTRAST_PROCESSOR, new ImageContrastPreProcessor());
        imagePreProcessorMap.put(ImagePreProcessorType.SIZE_PROCESSOR, new ImageSizePreProcessor());
        imagePreProcessorMap.put(ImagePreProcessorType.CROP_PREPROCESSOR, new ImageCropPreProcessor());
    }

    public static ImagePreProcessorFactory getInstance() {
        if (instance == null) {
            instance = new ImagePreProcessorFactory();
        }
        return instance;
    }

    public ImagePreProcessor getImagePreProcessor(ImagePreProcessorType type) {
        if (imagePreProcessorMap.containsKey(type)) {
            return imagePreProcessorMap.get(type);
        }
        throw new NotFoundPreProcessorException(type);
    }
}
