package com.example.propertypredictionbackend.network;

import com.example.propertypredictionbackend.network.model.Result;
import io.swagger.models.HttpMethod;

import java.net.URL;

/**
 * Generic interface to send requests to a provided server
 *
 */
public interface ServerManager {

    /**
     * @param serverUrl The server to which the request is sent
     * @param requestBody The request body
     * @param <T> The datatype of the received information
     * @param <R> The datatype of the sent information
     * @return The result of
     */
    <T, R> Result<T> sendRequest(
            URL serverUrl,
            R requestBody
    );

}
