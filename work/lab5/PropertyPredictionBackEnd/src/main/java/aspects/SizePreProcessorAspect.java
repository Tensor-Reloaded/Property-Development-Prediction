package aspects;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class SizePreProcessorAspect {

    @Pointcut("execution(* com.example.propertypredictionbackend.preprocesors.ImageSizePreProcessor.preProcessImage(..))")
    public void resizeImage() {
    }

    @Before("resizeImage()")
    public void messageBefore() {
        System.out.println("Adapt size initiated for execution");
    }

    @After("resizeImage()")
    public void messageAfter() {
        System.out.println("Adapt size finished execution");
    }
}
