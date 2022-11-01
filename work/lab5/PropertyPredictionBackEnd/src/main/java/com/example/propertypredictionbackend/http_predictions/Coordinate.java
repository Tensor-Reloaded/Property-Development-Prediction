package com.example.propertypredictionbackend.http_predictions;

public class Coordinate {

    private final float latitude;
    private final float longitude;

    private Coordinate(CoordinateBuilder builder) {
        this.latitude = builder.latitude;
        this.longitude = builder.longitude;
    }

    public float getLatitude() {
        return latitude;
    }
    public float getLongitude() {
        return longitude;
    }

    public static class CoordinateBuilder
    {
        private float latitude;
        private float longitude;

        public CoordinateBuilder withLatitude(float latitude) {
            this.latitude = latitude;
            return this;
        }
        public CoordinateBuilder withLongitude(float longitude) {
            this.longitude = longitude;
            return this;
        }

        public Coordinate build() {
            return  new Coordinate(this);
        }
    }
}