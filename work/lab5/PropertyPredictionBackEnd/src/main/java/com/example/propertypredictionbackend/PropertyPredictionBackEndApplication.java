package com.example.propertypredictionbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class PropertyPredictionBackEndApplication {

    public static void main(String[] args) {
        SpringApplication.run(PropertyPredictionBackEndApplication.class, args);
    }

}
