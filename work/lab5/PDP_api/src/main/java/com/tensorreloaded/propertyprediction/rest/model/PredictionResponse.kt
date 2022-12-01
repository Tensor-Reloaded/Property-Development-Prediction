package com.tensorreloaded.propertyprediction.rest.model

data class PredictionResponse(
    val image: String,
    val predictedPrice: Float
)