package com.tensorreloaded.propertyprediction.model;

import ai.djl.Application;
import ai.djl.inference.Predictor;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.modality.cv.util.BufferedImageUtils;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.training.util.ProgressBar;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Base64;


public class ModelUsage {
    public void predict(String imageB64) {
        try {
            var is = new ProcessBuilder(
                            "python3",
                    "/Users/tudorcraciun/VisualStudioCode/Property-Development-Prediction/work/lab5/PDP_api/src/main/resources/run_model.py"
                    ).start().getInputStream();
            var isr = new InputStreamReader(is);
            var br = new BufferedReader(isr);
            System.out.println(br.readLine());
        } catch (IOException e) {

        }

        try {
            byte[] byteImage = Base64.getDecoder().decode(imageB64);
            var bais = new ByteArrayInputStream(byteImage);
            var img = ImageIO.read(bais);

            var criteria =
                    Criteria.builder()
                            .optApplication(Application.CV.IMAGE_CLASSIFICATION)
                            .setTypes(BufferedImage.class, DetectedObjects.class)
                            .optFilter("backbone", "resnet50")
                            .optProgress(new ProgressBar())
                            .build();

            try (ZooModel<BufferedImage, DetectedObjects> model = ModelZoo.loadModel(criteria)) {
                try (Predictor<BufferedImage, DetectedObjects> predictor = model.newPredictor()) {
                    DetectedObjects detection = predictor.predict(img);
                    System.out.println(detection);
                }
            }
        } catch (Exception e) {
            //
        }
    }
}
