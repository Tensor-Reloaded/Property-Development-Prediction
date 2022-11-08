package aspects;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class ContrastPreProcessorAspect {

    @Pointcut("execution(* com.example.propertypredictionbackend.preprocesors.ImageContrastPreProcessor.*(..))")
    public void adaptImage() {
    }

    @Before("adaptImage()")
    public void messageBefore() {
        System.out.println("Adapt contrast method initiated for execution");
    }

    @After("adaptImage()")
    public void messageAfter() {
        System.out.println("Adapt contrast method finished execution");
    }
}
