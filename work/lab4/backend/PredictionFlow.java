import jdk.jshell.spi.ExecutionControl;

import java.net.URL;
import java.util.UUID;

public abstract class PredictionFlow {

    private final ImagePreProcessorFactory imagePreProcessorFactory = ImagePreProcessorFactory.getInstance();

    public PredictionRequest mapToPredictionRequest(String request) {
        // To be implemented
        return null;
    }

    public void adaptPredictionImage(PredictionRequest predictionRequest) {
        // To be implemented
    }

    public UUID sendRequestToModel(URL imagePredictionModelURL, PredictionRequest predictionRequest) {
        // To be implemented
        return null;
    }

    public PredictionResponse getResponseFromModel(URL imagePredictionModelURL, UUID id) {
        // To be implemented
        return null;
    }
}
