package com.example.propertypredictionbackend.network;

import com.example.propertypredictionbackend.network.model.Result;
import com.example.propertypredictionbackend.network.model.ResultError;
import com.example.propertypredictionbackend.network.model.ResultSuccess;
import org.glassfish.jersey.client.ClientConfig;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
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
    public <T, R> Result<T> sendRequest(URL serverUrl, R requestBody) {
        out.println("Sending request body " + requestBody.toString() + " to server " + serverUrl);
        try {
            Object returnedObj = client
                    .target(serverUrl.toURI())
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.entity(requestBody, MediaType.APPLICATION_JSON))
                    .getEntity();

            // Failure leads into exception catch
            return new ResultSuccess<T>((T) returnedObj);

        } catch (Throwable t) {
            out.println("Caught error");
            t.printStackTrace();
            return new ResultError<>(t);
        }
    }
}
