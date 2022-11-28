package com.tensorreloaded.propertyprediction.rest.model;

import java.awt.image.BufferedImage;

public record PredictionRequest(
        BufferedImage image,
        Coordinate coordinates,
        int yearsInFuture
) {
}
