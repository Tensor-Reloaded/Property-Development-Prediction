package com.example.propertypredictionbackend;

import com.example.propertypredictionbackend.flows.PredictionFlow;
import com.example.propertypredictionbackend.flows.SimplePredictionFlow;
import com.example.propertypredictionbackend.http_predictions.PredictionRequest;
import com.example.propertypredictionbackend.http_predictions.PredictionResponse;
import com.example.propertypredictionbackend.preprocesors.ImagePreProcessorFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Objects;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.UUID;


@RestController
public class RequestController {

    private final ResourceBundle bundle;
    private final URL imagePredictionModelURL;
    private final ImagePreProcessorFactory factory;
    private final PredictionFlow predictionFlow;

    public RequestController() {
        try (InputStream fileStream = getClass().getClassLoader().getResourceAsStream("static/string_constants.properties")) {
            this.factory = ImagePreProcessorFactory.getInstance();
            this.predictionFlow = new SimplePredictionFlow();
            this.bundle = new PropertyResourceBundle(new InputStreamReader(Objects.requireNonNull(fileStream)));
            this.imagePredictionModelURL = new URL(this.bundle.getString("url.backend.model"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping(value = "/predictDevelopment", consumes = "application/json")
    public PredictionResponse handleImagePredictionRequest(@RequestBody PredictionRequest request) {
        RequestImageGetter adapter = new RequestPredictionProxy(request);

        predictionFlow.adaptPredictionImage(adapter);

        UUID id = predictionFlow.sendRequestToModel(imagePredictionModelURL, request);

        return predictionFlow.getResponseFromModel(imagePredictionModelURL, id);
    }
    //TODO new route for prices --> new flow of work
}
