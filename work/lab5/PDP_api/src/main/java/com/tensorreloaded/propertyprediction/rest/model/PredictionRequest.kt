package com.tensorreloaded.propertyprediction.rest.model

data class PredictionRequest(
    val image: String,
    val coordinates: Coordinate,
    val yearsInFuture: Int
)