package aspects;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class ImagePreProcessorFactoryAspect {

    @Pointcut("execution(* com.example.propertypredictionbackend.preprocesors.ImagePreProcessorFactory.getInstance())")
    public void obtainSingletonInstance() {
    }

    @Before("obtainSingletonInstance()")
    public void messageBefore() {
        System.out.println("Obtaining singleton instance..");
    }

    @After("obtainSingletonInstance()")
    public void messageAfter() {
        System.out.println("Singleton instance obtained.");
    }
}
