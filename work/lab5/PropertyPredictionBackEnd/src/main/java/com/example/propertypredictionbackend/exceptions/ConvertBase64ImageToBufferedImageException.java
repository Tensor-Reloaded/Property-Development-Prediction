package com.example.propertypredictionbackend.exceptions;

public class ConvertBase64ImageToBufferedImageException extends RuntimeException {
    public ConvertBase64ImageToBufferedImageException(String message) {
        super(String.format("Error when converting a base64 representation of image to BufferedImage. Actual exception: %s", message));
    }
}
