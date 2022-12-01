package com.example.propertypredictionbackend.network.model;

import lombok.Getter;

/**
 * Successful server request response
 * @param <T> Type of data returned by the request
 */
public class ResultSuccess<T> implements Result<T> {
    @Getter
    private final T resultData;

    public ResultSuccess(T data) {
        resultData = data;
    }

    @Override
    public String toString() {
        return "ResultSuccess[" + resultData.toString() + "]";
    }
}
