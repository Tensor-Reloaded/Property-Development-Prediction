package aspects;


import com.example.propertypredictionbackend.exceptions.YearNeedToBePositiveException;
import org.aspectj.lang.JoinPoint;

public class PredictionRequestYearsInFutureAspect {

    public void validateYearsInFuture(JoinPoint point) {
        Object[] args = point.getArgs();
        int yearsInFuture = (Integer) args[0];
        if(yearsInFuture < 0) {
            System.out.println("The argument needs to be a positive number");
            throw new YearNeedToBePositiveException(yearsInFuture);
        }
    }

}
