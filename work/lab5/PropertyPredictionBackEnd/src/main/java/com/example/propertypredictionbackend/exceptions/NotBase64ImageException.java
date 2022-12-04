package com.example.propertypredictionbackend.exceptions;

public class NotBase64ImageException extends RuntimeException{

    public NotBase64ImageException(Object object) {
        super(String.format("Not a base64 image! \n%s", object));
    }
}
