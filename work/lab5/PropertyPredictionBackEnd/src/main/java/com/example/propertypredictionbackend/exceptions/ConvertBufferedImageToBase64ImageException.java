package com.example.propertypredictionbackend.exceptions;

public class ConvertBufferedImageToBase64ImageException extends RuntimeException {

    public ConvertBufferedImageToBase64ImageException(String message) {
        super(String.format("Error when converting a BufferedImage to base64 representation. Actual exception: %s", message));
    }
}
