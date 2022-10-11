package nl.kooi.camundarules.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.kooi.camundarules.api.dto.UserDto;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class AccessCheckAspect {

    private final AopUtils aopUtils;
    private final RequestUtils requestUtils;


    @Before("methodAnnotatedWithTraceAccessDetails()")
    public void logAccessDetails(JoinPoint joinPoint) {
        var user = requestUtils.getContextInformation().orElseThrow();
        System.out.println(user);
    }

    @Pointcut("execution(@nl.kooi.camundarules.configuration.UserIsAuthorized * *(..))")
    public void methodAnnotatedWithTraceAccessDetails() {
    }

}