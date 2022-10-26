import java.awt.image.BufferedImage;

public class RequestPredictionAdapter implements RequestImageGetter {

    private PredictionRequest predictionRequest;

    public RequestPredictionAdapter(PredictionRequest predictionRequest) {
        this.predictionRequest = predictionRequest;
    }

    @Override
    public BufferedImage getImage() {
        return predictionRequest.getImage();
    }

    @Override
    public void setImage(BufferedImage image) {
        /// to be implemented using java reflexion
    }
}
