package com.example.propertypredictionbackend.network.model;

import lombok.Getter;

/**
 * Erroneous server result
 */
public class ResultError<T> implements Result<T> {

    /**
     * Exception or error
     */
    @Getter
    private final Throwable throwable;

    public ResultError(Throwable t) {
        throwable = t;
    }

    @Override
    public String toString() {
        return "ResultError[" + throwable.getMessage() + "]";
    }
}
