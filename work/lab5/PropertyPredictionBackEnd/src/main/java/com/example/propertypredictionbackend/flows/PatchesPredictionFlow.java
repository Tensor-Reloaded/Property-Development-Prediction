package com.example.propertypredictionbackend.flows;

import com.example.propertypredictionbackend.RequestImageGetter;
import com.example.propertypredictionbackend.dtos.PredictionRequest;
import com.example.propertypredictionbackend.dtos.PredictionResponse;
import com.example.propertypredictionbackend.dtos.http.HttpPredictionResponse;
import com.example.propertypredictionbackend.network.ServerManagerImpl;
import com.example.propertypredictionbackend.network.model.Result;
import com.example.propertypredictionbackend.network.model.ResultError;
import com.example.propertypredictionbackend.network.model.ResultSuccess;
import com.example.propertypredictionbackend.preprocesors.ImagePreProcessor;
import com.example.propertypredictionbackend.preprocesors.ImagePreProcessorFactory;
import com.example.propertypredictionbackend.preprocesors.ImagePreProcessorType;
import com.example.propertypredictionbackend.utils.CommunicationUtils;
import com.example.propertypredictionbackend.utils.ImageUtils;
import com.google.gson.Gson;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.out;

public class PatchesPredictionFlow extends PredictionFlow {

    private ImageUtils imageUtils = new ImageUtils();
    private final int cropSize = 224;
    private final int countCrops = 4;
    private final String FORMAT = "png";
    private final ServerManagerImpl serverManagerInstance = ServerManagerImpl.getInstance();

    @Override
    public void adaptPredictionImage(RequestImageGetter predictionRequest) {

        ImagePreProcessor imageCropPreProcessor = ImagePreProcessorFactory.getInstance()
                .getImagePreProcessor(ImagePreProcessorType.CROP_PREPROCESSOR);

        imageCropPreProcessor.preProcessImage(predictionRequest);
    }

    @Override
    public PredictionResponse getDirectResponseFromModel(URL imagePredictionModelURL, PredictionRequest predictionRequest) {

        BufferedImage imageSource = imageUtils.convertBase64ImageToBufferedImage(predictionRequest.getImage());
        List<BufferedImage> patches = new ArrayList<>();
        out.println("Image size: width:" + imageSource.getWidth() + " heigth: " + imageSource.getHeight());
        int heightTotal = 448;
        int widthTotal = 448;
        for (int indexRow = 0; indexRow < countCrops / 2; indexRow++) {
            for (int indexColumn = 0; indexColumn < countCrops / 2; indexColumn++) {
                PredictionRequest cropRequest = new PredictionRequest.PredictionRequestBuilder()
                        .withImage(imageUtils.convertBufferedImageToBase64Image(
                                imageUtils.cropImage(imageSource, cropSize * indexColumn, cropSize * indexRow, cropSize, cropSize), FORMAT))
                        .withCoordinate(predictionRequest.getCoordinates())
                        .withYearsInFuture(predictionRequest.getYearsInFuture())
                        .build();
                patches.add(getCropPredicted(imagePredictionModelURL, cropRequest));
            }
        }

        BufferedImage finalPredictionImage = new BufferedImage(widthTotal, heightTotal, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = finalPredictionImage.createGraphics();

        for (int i = 0; i < countCrops / 2; i++) {
            for (int j = 0; j < countCrops / 2; j++) {
                g2d.drawImage(patches.get(i * countCrops / 2 + j), cropSize * j, cropSize * i, null);
            }
        }
        g2d.dispose();

        return new PredictionResponse.PredictionResponseBuilder()
                .withImage(imageUtils.convertBufferedImageToBase64Image(finalPredictionImage, FORMAT))
                .withPredictedPrice(-1)
                .build();
    }

    private BufferedImage getCropPredicted(URL imagePredictionURL, PredictionRequest predictionRequest) {
        Result<String> receivedResult = serverManagerInstance.sendRequest(
                imagePredictionURL,
                predictionRequest
        );

        out.println("Received answer from server: " + receivedResult.toString());

        if (receivedResult instanceof ResultError<String>) {
            out.println("Failed request");
            return null;
        } else if (receivedResult instanceof ResultSuccess<String>) {
            out.println("Succeeded request");
            return
                    imageUtils.convertBase64ImageToBufferedImage(
                            new Gson()
                                    .fromJson(((ResultSuccess<String>) receivedResult)
                                            .getResultData(), HttpPredictionResponse.class).getImage());
        }

        out.println("Unknown status");
        //should be thrown an exception
        return null;
    }
}
