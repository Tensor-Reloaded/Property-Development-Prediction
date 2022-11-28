package com.tensorreloaded.propertyprediction.rest.controllers;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

import com.tensorreloaded.propertyprediction.rest.model.PredictionRequest;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class PredictionRequestController {

    // REST implements by default the stateless design pattern
    @RequestMapping(value = "/predictProperty", method = RequestMethod.GET, consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> predictProperty(
            @RequestParam(value = "requestB64") String requestB64
    ) {
        try {
            var result = (PredictionRequest) new JSONParser(requestB64).parse();
            //ImagePredictor(result.image).predictInFuture(result.yearsInFuture);
            return new ResponseEntity<>("Good", HttpStatus.OK);
        } catch (Exception e) {
            //
        }
        return new ResponseEntity<>("Bad json", HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/predictPrice", method = RequestMethod.GET, consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> predictPrice(
            @RequestParam(value = "requestB64") String requestB64
    ) {
        try {
            var result = (PredictionRequest) new JSONParser(requestB64).parse();
            //PricePredictor(result.image).predictInFuture(result.yearsInFuture);
            return new ResponseEntity<>("Good", HttpStatus.OK);
        } catch (Exception e) {
            //
        }
        return new ResponseEntity<>("Bad json", HttpStatus.BAD_REQUEST);
    }
}