package com.example.propertypredictionbackend.dtos.http;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

public class HttpPredictionResponse implements Serializable {

    @Setter
    @Getter
    private String image;

    @Setter
    @Getter
    private float predictedPrice;
}
