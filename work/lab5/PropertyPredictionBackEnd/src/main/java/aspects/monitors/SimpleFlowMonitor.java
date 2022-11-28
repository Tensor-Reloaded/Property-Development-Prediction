package aspects.monitors;

import com.example.propertypredictionbackend.dtos.PredictionRequest;
import com.example.propertypredictionbackend.dtos.PredictionResponse;
import com.example.propertypredictionbackend.exceptions.ConvertBase64ImageToBufferedImageException;
import com.example.propertypredictionbackend.flows.SimplePredictionFlow;
import com.example.propertypredictionbackend.preprocesors.ImagePreProcessorFactory;
import com.example.propertypredictionbackend.utils.ImageUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.rmi.server.UID;
import java.util.Objects;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

@Aspect
public class SimpleFlowMonitor {

    private ResourceBundle bundle;

    @Pointcut("execution(* com.example.propertypredictionbackend.flows.SimplePredictionFlow.adaptPredictionImage(..))")
    public void adaptImage() {

    }

    @Pointcut("execution(* com.example.propertypredictionbackend.flows.SimplePredictionFlow.sendRequestToModel(..))")
    public void sendRequest() {

    }

    @Pointcut("execution(* com.example.propertypredictionbackend.flows.SimplePredictionFlow.getResponseFromModel(..))")
    public void receiveResponse() {

    }


    @Around("adaptImage()")
    public Object monitorAdaptionProcedure(ProceedingJoinPoint pjp) {
        Object[] arguments = pjp.getArgs();
        try {
            return pjp.proceed(arguments);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @After("adaptImage()")
    public void afterAdaptImageCall() {
        System.out.println("Adapt call finished");
    }


    @Around("sendRequest()")
    public Object monitorSendRequest(ProceedingJoinPoint pjp) {
        Object[] arguments = pjp.getArgs();
        try {
            ImageUtils.convertBase64ImageToBufferedImage(((PredictionRequest) arguments[0]).getImage());
            return pjp.proceed(arguments);
        } catch (ConvertBase64ImageToBufferedImageException exception) {
            throw new RuntimeException("Invalid string base64 image!" + ((PredictionRequest) arguments[0]).getImage());
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @AfterReturning(value = "sendRequest()", returning = "returnValue")
    public void afterSendCall(Object returnValue) {
        if (returnValue == null) {
            System.out.println("Error: null value for ID received from the model server!");
        } else {
            System.out.println("ID for receiving the image: " + returnValue);
        }
    }

    private URL urlForPrediction() {
        try (InputStream fileStream = getClass().getClassLoader().getResourceAsStream("static/string_constants.properties")) {
            this.bundle = new PropertyResourceBundle(new InputStreamReader(Objects.requireNonNull(fileStream)));
            return new URL(this.bundle.getString("url.backend.model"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Around("receiveResponse()")
    public Object monitorReceiveResponse(ProceedingJoinPoint pjp) {
        Object[] arguments = pjp.getArgs();
        URL url = (URL) arguments[0];
        UID uid = (UID) arguments[1];
        if (url == null) {
            System.out.println("Null URL for receiving the model. Updating with " + urlForPrediction());
            arguments[0] = urlForPrediction();
        }
        if (uid == null) {
            System.out.println("No UID provided for getting the image from the model server!");
            throw new RuntimeException("Null UID when calling the image from the server model");
        }
        try {
            return pjp.proceed(arguments);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @AfterReturning(value = "receiveResponse()", returning = "returnValue")
    public void afterReceiveCall(Object returnValue) {
        if (returnValue == null) {
            System.out.println("Error: null response from the method that get the image from the server!");
        } else {
            System.out.println("Received the following response: " + returnValue);
        }
    }

}
