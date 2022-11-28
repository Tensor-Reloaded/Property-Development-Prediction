package com.example.propertypredictionbackend.dtos.http;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

public class HttpCoordinates implements Serializable {

    @Setter
    @Getter
    private float latitude;

    @Setter
    @Getter
    private float longitude;
}
