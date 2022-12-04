package aspects.monitors;

import com.example.propertypredictionbackend.utils.SingletonProvider;
import com.example.propertypredictionbackend.dtos.PredictionRequest;
import com.example.propertypredictionbackend.dtos.http.HttpPredictionRequest;
import com.example.propertypredictionbackend.exceptions.ConvertBase64ImageToBufferedImageException;
import com.example.propertypredictionbackend.utils.CommunicationUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class RequestMonitor {

    @Around("execution(* com.example.propertypredictionbackend.RequestController.handleImagePredictionRequest(..))")
    public Object checkFlow(ProceedingJoinPoint pjp) {
        System.out.println("Simple flow called");
        Object[] argumentsForFlow = pjp.getArgs();
        HttpPredictionRequest request = (HttpPredictionRequest) argumentsForFlow[0];
        PredictionRequest requestPrediction = CommunicationUtils.mapHttpPredictionRequestToPredictionRequest(request);
        try {
            SingletonProvider.getImageUtils().convertBase64ImageToBufferedImage(requestPrediction.getImage());
            if (request.getYearsInFuture() <= 0) {
                System.out.println("Invalid years. Setting years to value 1");
                request.setYearsInFuture(1);
            }
            return pjp.proceed(argumentsForFlow);
        } catch (ConvertBase64ImageToBufferedImageException exception) {
            System.out.println("Exception on received image: invalid base64 representation");
            throw exception;
        } catch (Throwable e) {
            System.out.println("Unhandled exception occurred. " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

}
