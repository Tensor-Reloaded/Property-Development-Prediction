package com.example.propertypredictionbackend.dtos;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PredictionResponseConfig {

    @Bean
    public PredictionRequest getPredictionRequest() {
        return new PredictionRequest();
    }
}
