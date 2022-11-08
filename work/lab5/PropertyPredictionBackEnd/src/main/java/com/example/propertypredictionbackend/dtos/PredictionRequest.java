package com.example.propertypredictionbackend.dtos;

import java.io.Serializable;

public class PredictionRequest implements Serializable {

    private String image;

    private Coordinate coordinates;

    private int yearsInFuture;

    private PredictionRequest(PredictionRequestBuilder builder) {
        this.image = builder.image;
        this.coordinates = builder.coordinates;
        this.yearsInFuture = builder.yearsInFuture;
    }

    public String getImage() {
        return image;
    }

    public Coordinate getCoordinates() {
        return coordinates;
    }

    public int getYearsInFuture() {
        return yearsInFuture;
    }

    public static class PredictionRequestBuilder {

        private String image;

        private Coordinate coordinates;

        private int yearsInFuture;

        public PredictionRequestBuilder withImage(String image) {
            if (image.startsWith("data:")) {
                image = image.substring(image.indexOf(",") + 1);
            }
            this.image = image;
            return this;
        }

        public PredictionRequestBuilder withYearsInFuture(int yearsInFuture) {
            this.yearsInFuture = yearsInFuture;
            return this;
        }

        public PredictionRequestBuilder withCoordinate(Coordinate coordinates) {
            this.coordinates = coordinates;
            return this;
        }

        public PredictionRequest build() {
            return new PredictionRequest(this);
        }
    }
}

