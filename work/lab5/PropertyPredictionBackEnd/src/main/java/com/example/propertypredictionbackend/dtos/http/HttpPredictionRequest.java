package com.example.propertypredictionbackend.dtos.http;

import com.example.propertypredictionbackend.dtos.Coordinate;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

public class HttpPredictionRequest implements Serializable {

    @Getter
    @Setter
    private String image;

    @Getter
    @Setter
    private HttpCoordinates coordinates;

    @Getter
    @Setter
    private int yearsInFuture;

    @Override
    public String toString() {
        return "HttpPredictionRequest:[" +
                "image: " + image + "," +
                "httpCoordinate: " + coordinates + "," +
                "yearsInFuture:" + yearsInFuture + "]";
    }
}
