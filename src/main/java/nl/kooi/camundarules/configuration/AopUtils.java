package nl.kooi.camundarules.configuration;

import org.aspectj.lang.JoinPoint;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AopUtils {

    public <T> Optional<T> getParamByType(JoinPoint joinPoint, Class<T> paramClass) {
        for (Object parameter : joinPoint.getArgs()) {
            if (paramClass.isInstance(parameter)) {
                return Optional.of((T) parameter);
            }
        }
        return Optional.empty();
    }
}