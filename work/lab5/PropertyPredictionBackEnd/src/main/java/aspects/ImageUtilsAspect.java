package aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class ImageUtilsAspect {

    @Pointcut("execution(* com.example.propertypredictionbackend.utils.ImageUtils.PrintSimpleMessage(..))")
    public void test() {
    }

    @Before("test()")
    public void testAgain(JoinPoint joinPoint) {
        System.out.println("Before message on the print method of the ImageUtils class.");
    }
}
