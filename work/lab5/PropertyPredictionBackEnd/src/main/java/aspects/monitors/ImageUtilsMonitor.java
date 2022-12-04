package aspects.monitors;

import com.example.propertypredictionbackend.exceptions.NotBase64ImageException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Aspect
public class ImageUtilsMonitor {

    public static final String BASE64_REGEX = "data:image/(jpeg|png|bmp);base64,[A-Za-z0-9+/]*";

    @Around("execution(* com.example.propertypredictionbackend.utils.ImageUtils.convertBase64ImageToBufferedImage(..))")
    public Object checkExecution(ProceedingJoinPoint pjp) {
        Object[] arguments = pjp.getArgs();
        String base64Image = (String) arguments[0];

        Pattern pattern = Pattern.compile(BASE64_REGEX);
        Matcher matcher = pattern.matcher(base64Image);

        if(!matcher.matches()) {
            throw new NotBase64ImageException(base64Image);
        }

        try {
            return pjp.proceed(arguments);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return this;
    }
}
