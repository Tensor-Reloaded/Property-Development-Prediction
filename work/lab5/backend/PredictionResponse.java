import java.awt.image.BufferedImage;

public class PredictionResponse {

    private final BufferedImage image;

    private final float predictedPrice;

    private PredictionResponse(PredictionResponseBuilder builder) {
        this.image = builder.image;
        this.predictedPrice = builder.predictedPrice;
    }

    public BufferedImage getImage() {
        return image;
    }

    public float getPredictedPrice() {
        return predictedPrice;
    }


    public static class PredictionResponseBuilder
    {
        private BufferedImage image;
        private float predictedPrice;

        public PredictionResponseBuilder withImage(BufferedImage image) {
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
}