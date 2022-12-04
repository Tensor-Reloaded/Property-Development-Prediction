package aspects.monitors;

import com.example.propertypredictionbackend.dtos.PredictionRequest;
import com.example.propertypredictionbackend.network.model.ResultError;
import com.example.propertypredictionbackend.network.model.ResultSuccess;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class ServerMonitor {

    private void analyzePayload(Object payload) throws Exception {
        if (payload == null) {
            throw new Exception("[ServerMonitor] Null payload given!");
        }
        //here analysis the case for predictionrequest
        if (payload instanceof PredictionRequest) {
            if (((PredictionRequest) payload).getImage() == null) {
                throw new Exception("[ServerMonitor] Invalid image: null!");
            }
            if (((PredictionRequest) payload).getImage().isEmpty()) {
                throw new Exception("[ServerMonitor] Invalid image: empty!");
            }
            if (((PredictionRequest) payload).getYearsInFuture() <= 0) {
                throw new Exception("[ServerMonitor] Invalid years! Found " + ((PredictionRequest) payload).getYearsInFuture());
            }
        }
    }

    @Pointcut("execution(* com.example.propertypredictionbackend.network.ServerManagerImpl.sendRequest(..))")
    public void executeSentGetImagePredictionWithServer() {

    }

    @Around("executeSentGetImagePredictionWithServer()")
    public Object monitorSentGetRequestForImage(ProceedingJoinPoint pjp) {
        try {
            Object[] args = pjp.getArgs();

            System.out.println("[ServerMonitor] Sending a request to the resource at address " + args[0].toString()
                    + " with data " + args[1].toString());
            if (args[0] == null) {
                System.out.println("[ServerMonitor] Null URL specified for the server!");
                throw new Exception("[ServerMonitor] Invalid URL: found null");
            }
            analyzePayload(args[1]);

            return pjp.proceed(args);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @AfterReturning(value = "executeSentGetImagePredictionWithServer()", returning = "result")
    public void analyzeReturn(Object result) {
        if (result instanceof ResultSuccess<?>) {
            System.out.println("[ServerMonitor] Success on prediction. Result: " + ((ResultSuccess<?>) result).getResultData());
        } else if (result instanceof ResultError<?>) {
            System.out.println("[ServerMonitor] Error returned from the API. Error:" + result);
        } else {
            System.out.println("[ServerMonitor] Unknown result type! Result: " + result.toString());
        }
    }
}
