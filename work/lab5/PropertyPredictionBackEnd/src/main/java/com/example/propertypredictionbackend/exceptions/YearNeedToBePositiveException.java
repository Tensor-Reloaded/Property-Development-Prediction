package com.example.propertypredictionbackend.exceptions;

public class YearNeedToBePositiveException  extends RuntimeException{

    public YearNeedToBePositiveException(int year) {
        super(String.format("The argument needs to be a positive number. Value received is [%d]", year));
    }
}
