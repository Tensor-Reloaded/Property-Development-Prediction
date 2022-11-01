package com.example.propertypredictionbackend.exceptions;

public class NotFoundPreProcessorException extends RuntimeException {

    public NotFoundPreProcessorException(Object object) {
        super(String.format("There is no implementation for ImagePreProcessor associated with the given value." +
                " Received value: %s", object));
    }
}
