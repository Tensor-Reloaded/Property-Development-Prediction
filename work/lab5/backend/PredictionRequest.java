import java.awt.image.BufferedImage;

public class PredictionRequest {

    private BufferedImage image;

    private final Coordinate coordinates;

    private final int yearsInFuture;

    private PredictionRequest(PredictionRequestBuilder builder) {
        this.image = builder.image;
        this.coordinates = builder.coordinates;
        this.yearsInFuture = builder.yearsInFuture;
    }

    public BufferedImage getImage() {
        return image;
    }

    public Coordinate getCoordinates() {
        return coordinates;
    }

    public int getYearsInFuture() {
        return yearsInFuture;
    }

    public static class PredictionRequestBuilder
    {

        private BufferedImage image;

        private Coordinate coordinates;

        private int yearsInFuture;

        public PredictionRequestBuilder withImage(BufferedImage image) {
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

