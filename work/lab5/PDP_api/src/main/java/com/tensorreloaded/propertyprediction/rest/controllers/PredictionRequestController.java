package com.tensorreloaded.propertyprediction.rest.controllers;

import com.tensorreloaded.propertyprediction.rest.model.PredictionRequest;
import com.tensorreloaded.propertyprediction.rest.model.PredictionResponse;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.tensorreloaded.propertyprediction.rest.Constants.DEFAULT_FLOAT_VALUE;
import static java.lang.System.out;

@RestController
@RequestMapping("/api")
public class PredictionRequestController {

    // REST implements by default the stateless design pattern
    @RequestMapping(value = "/predictProperty", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public PredictionResponse predictProperty(@RequestBody PredictionRequest predictionRequest) {
        out.println("Received predictionRequest: " + predictionRequest);
        return new PredictionResponse("test", DEFAULT_FLOAT_VALUE);
    }
}