package com.example.propertypredictionbackend.requests_tests;

import com.example.propertypredictionbackend.dtos.PredictionRequest;
import com.example.propertypredictionbackend.exceptions.YearNeedToBePositiveException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class PredictionRequestYearsInFutureAspect {

    @Test
    public void testObtainingSingletonInstanceUsingAspects() {
        // Arrange
        ApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");
        var weakReference = new Object() {
            PredictionRequest predictionRequest = (PredictionRequest) context.getBean("predictionRequest");
        };

        // Act

        // Act + Asset

        Assertions.assertThrows(YearNeedToBePositiveException.class, () -> weakReference.predictionRequest = new PredictionRequest.PredictionRequestBuilder()
                .withYearsInFuture(-1)
                .withImage(weakReference.predictionRequest.getImage())
                .withCoordinate(weakReference.predictionRequest.getCoordinates())
                .build());
    }
}
