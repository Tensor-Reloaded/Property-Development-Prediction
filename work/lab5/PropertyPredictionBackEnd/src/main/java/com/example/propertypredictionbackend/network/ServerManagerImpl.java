package com.example.propertypredictionbackend.network;

import com.example.propertypredictionbackend.dtos.http.HttpPredictionResponse;
import com.example.propertypredictionbackend.network.model.Result;
import com.example.propertypredictionbackend.network.model.ResultError;
import com.example.propertypredictionbackend.network.model.ResultSuccess;
import com.example.propertypredictionbackend.utils.ImageUtils;
import com.google.gson.Gson;
import org.glassfish.jersey.client.ClientConfig;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import java.awt.image.BufferedImage;
import java.net.URL;

import static java.lang.System.out;

public class ServerManagerImpl implements ServerManager {

    private static ServerManagerImpl singleton = null;


    Client client = ClientBuilder.newClient(new ClientConfig());

    ServerManagerImpl() {
        //
    }

    public static ServerManagerImpl getInstance() {
        if (singleton == null) {
            return new ServerManagerImpl();
        }
        return singleton;
    }

    @Override
    public <R> Result<String> sendRequest(URL serverUrl, R requestBody) {
        out.println("Sending request body " + requestBody.toString() + " to server " + serverUrl);
        String returnedObj = "";
        try {
            returnedObj = client
                    .target(serverUrl.toURI())
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.entity(requestBody, MediaType.APPLICATION_JSON))
                    .readEntity(String.class);
            // Failure leads into exception catch
            // Checking if the received data from the API is a valid response
            BufferedImage image = new ImageUtils().convertBase64ImageToBufferedImage(
                    new Gson().fromJson(returnedObj, HttpPredictionResponse.class).getImage());
            return new ResultSuccess<>(returnedObj);

        } catch (Throwable t) {
            out.println("Caught error with message " + t.getMessage());
            t.printStackTrace();
            t = new Throwable(t + "\nReturnedObj:\n" + returnedObj);
            return new ResultError<>(t);
        }
    }
}
