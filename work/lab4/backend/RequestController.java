import java.net.URL;
import java.util.UUID;

public class RequestController {

    private final URL imagePredictionModelURL;
    private final ImagePreProcessorFactory factory;
    private final PredictionFlow predictionFlow;

    public RequestController(URL imagePredictionModelURL, ImagePreProcessorFactory factory, PredictionFlow predictionFlow) {

        this.imagePredictionModelURL = imagePredictionModelURL;
        this.factory = factory;
        this.predictionFlow = predictionFlow;
    }

    public PredictionResponse handleImagePredictionRequest(String request) {

        PredictionRequest predictionRequest = predictionFlow.mapToPredictionRequest(request);

        predictionFlow.adaptPredictionImage(predictionRequest);

        UUID id = predictionFlow.sendRequestToModel(imagePredictionModelURL, predictionRequest);

        return predictionFlow.getResponseFromModel(imagePredictionModelURL, id);
    }
}
