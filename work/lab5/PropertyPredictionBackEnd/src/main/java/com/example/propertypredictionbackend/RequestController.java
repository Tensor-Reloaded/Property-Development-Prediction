package com.example.propertypredictionbackend;

import com.example.propertypredictionbackend.flows.PredictionFlow;
import com.example.propertypredictionbackend.flows.SimplePredictionFlow;
import com.example.propertypredictionbackend.dtos.Coordinate;
import com.example.propertypredictionbackend.dtos.PredictionRequest;
import com.example.propertypredictionbackend.dtos.PredictionResponse;
import com.example.propertypredictionbackend.preprocesors.ImagePreProcessor;
import com.example.propertypredictionbackend.preprocesors.ImagePreProcessorFactory;
import io.swagger.annotations.Api;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.net.URL;
import java.util.Objects;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.UUID;

@RestController
@RequestMapping("/api")
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

    @RequestMapping(value = "/predictDevelopment", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public PredictionResponse handleImagePredictionRequest(@RequestBody PredictionRequest request) {
        RequestImageGetter adapter = new RequestPredictionProxy(request);

        predictionFlow.adaptPredictionImage(adapter);

        UUID id = predictionFlow.sendRequestToModel(imagePredictionModelURL, request);

        return predictionFlow.getResponseFromModel(imagePredictionModelURL, id);
    }
    //TODO new route for prices --> new flow of work

    @GetMapping(value = "/testAspect")
    public void demonstrationOfTheAspect() {
        /*
        This method is here only for understanding the way the aspects works.
        Should be deleted after.
         */
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("src/test/java/com/example/propertypredictionbackend/preprocessors_tests/test_images/textBase.txt")))) {
            ApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");
            ImagePreProcessor preProcessorLocal = (ImagePreProcessor) context.getBean("sizePreProcessor");
            String image = reader.readLine();
            Coordinate coordinate = new Coordinate.CoordinateBuilder()
                    .withLatitude(43.464868f)
                    .withLongitude(54.3161f)
                    .build();
            PredictionRequest request = new PredictionRequest.PredictionRequestBuilder()
                    .withImage(image)
                    .withYearsInFuture(5)
                    .withCoordinate(coordinate).build();
            RequestImageGetter adapter = new RequestPredictionProxy(request);

            // Act
            preProcessorLocal.preProcessImage(adapter);

            // Assert
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
