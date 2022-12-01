package com.example.propertypredictionbackend.dtos;

public class PredictionResponse {

    private final String image;
    private final float predictedPrice;

    private PredictionResponse(PredictionResponseBuilder builder) {
        this.image = builder.image;
        this.predictedPrice = builder.predictedPrice;
    }

    public String getImage() {
        return image;
    }

    public float getPredictedPrice() {
        return predictedPrice;
    }


    public static class PredictionResponseBuilder
    {
        private String image;
        private float predictedPrice;

        public PredictionResponseBuilder withImage(String image) {
            this.image = image;
            return this;
        }
        public PredictionResponseBuilder withPredictedPrice(float predictedPrice) {
            this.predictedPrice = predictedPrice;
            return this;
        }

        public PredictionResponse build() {
            return new PredictionResponse(this);
        }
    }

    @Override
    public String toString() {
        return "PredictionResponse{" +
                "image='" + image + '\'' +
                ", predictedPrice=" + predictedPrice +
                '}';
    }
}