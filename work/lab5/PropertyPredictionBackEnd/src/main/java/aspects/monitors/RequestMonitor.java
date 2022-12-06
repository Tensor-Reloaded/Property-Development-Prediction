package aspects.monitors;

import com.example.propertypredictionbackend.network.model.Result;
import com.example.propertypredictionbackend.utils.SingletonProvider;
import com.example.propertypredictionbackend.dtos.PredictionRequest;
import com.example.propertypredictionbackend.dtos.http.HttpPredictionRequest;
import com.example.propertypredictionbackend.dtos.http.HttpPredictionResponse;
import com.example.propertypredictionbackend.exceptions.ConvertBase64ImageToBufferedImageException;
import com.example.propertypredictionbackend.utils.CommunicationUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class RequestMonitor {


    @Pointcut("execution(* com.example.propertypredictionbackend.RequestController.handleImagePredictionRequest(..))")
    public void executePredictionForClient() {

    }

    @Around("executePredictionForClient())")
    public Object checkFlow(ProceedingJoinPoint pjp) {
        System.out.println("[RequestMonitor] Simple flow called");
        Object[] argumentsForFlow = pjp.getArgs();
        HttpPredictionRequest request = (HttpPredictionRequest) argumentsForFlow[0];
        PredictionRequest requestPrediction = CommunicationUtils.mapHttpPredictionRequestToPredictionRequest(request);
        try {
            SingletonProvider.getImageUtils().convertBase64ImageToBufferedImage(requestPrediction.getImage());
            if (request.getYearsInFuture() <= 0) {
                System.out.println("[RequestMonitor] Invalid years. Setting years to value 1");
                request.setYearsInFuture(1);
            }
            return pjp.proceed(argumentsForFlow);
        } catch (ConvertBase64ImageToBufferedImageException exception) {
            System.out.println("[RequestMonitor] Exception on received image: invalid base64 representation");
            throw exception;
        } catch (Throwable e) {
            System.out.println("[RequestMonitor] Unhandled exception occurred. " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @AfterReturning(value = "executePredictionForClient()", returning = "httpResponse")
    public void analyzeReturnImage(Object httpResponse) {
        System.out.println("[RequestMonitor] Returning to the client the response from the API.");
        if (((HttpPredictionResponse) httpResponse).getPredictedPrice() < 0) {
            System.out.println("[RequestMonitor] Sending error to the client");
        }
        System.out.println("[RequestMonitor]" + (HttpPredictionResponse) httpResponse);
    }

}
