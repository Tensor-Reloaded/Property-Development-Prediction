package aspects.monitors;

import com.example.propertypredictionbackend.dtos.PredictionRequest;
import com.example.propertypredictionbackend.utils.SingletonProvider;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Objects;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

@Aspect
public class SimpleFlowMonitor {

    private ResourceBundle bundle;

    @Pointcut("execution(* com.example.propertypredictionbackend.flows.SimplePredictionFlow.adaptPredictionImage(..))")
    public void adaptImage() {

    }

    @Pointcut("execution(* com.example.propertypredictionbackend.flows.SimplePredictionFlow.getDirectResponseFromModel(..))")
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
        System.out.println("[ServerMonitor] Adapt call finished");
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
        PredictionRequest image = (PredictionRequest) arguments[1];
        if (url == null) {
            System.out.println("[ServerMonitor] Null URL for receiving the model. Updating with " + urlForPrediction());
            arguments[0] = urlForPrediction();
        }
        if (image == null) {
            System.out.println("[ServerMonitor] No image provided for getting the image from the model server!");
            throw new RuntimeException("[ServerMonitor] Null image when calling the image from the server model");
        }
        try {
            SingletonProvider.getImageUtils().convertBase64ImageToBufferedImage(image.getImage());
            return pjp.proceed(arguments);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @AfterReturning(value = "receiveResponse()", returning = "returnValue")
    public void afterReceiveCall(Object returnValue) {
        if (returnValue == null) {
            System.out.println("[ServerMonitor] Error: null response from the method that get the image from the server!");
        } else {
            System.out.println("[ServerMonitor] Received the following response: " + returnValue);
        }
    }

}
