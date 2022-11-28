package aspects;


import com.example.propertypredictionbackend.dtos.PredictionRequest;
import com.example.propertypredictionbackend.exceptions.YearNeedToBePositiveException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;

@Aspect
public class PredictionRequestYearsInFutureAspect {

    @Pointcut("execution(* com.example.propertypredictionbackend.flows.SimplePredictionFlow.sendRequestToModel(..))")
    public void validateYearsInFuture() {
    }

    @Before("validateYearsInFuture()")
    public void validateData(JoinPoint pjp) {
        Object[] args = pjp.getArgs();
        PredictionRequest request = (PredictionRequest) args[1];
        int yearsInFuture = request.getYearsInFuture();
        if (yearsInFuture < 0) {
            System.out.println("The argument needs to be a positive number! Received number:" + yearsInFuture);
        }
    }
}
