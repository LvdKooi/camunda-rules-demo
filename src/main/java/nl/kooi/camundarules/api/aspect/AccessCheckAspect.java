package nl.kooi.camundarules.api.aspect;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.kooi.camundarules.configuration.RequestUtils;
import nl.kooi.camundarules.exception.UnauthorizedException;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class AccessCheckAspect {

    private final RequestUtils requestUtils;

    @Before("methodAnnotatedWithTraceAccessDetails()")
    public void checkAndLogAuthorization() {
        var user = requestUtils.getContextInformation().orElseThrow(UnauthorizedException::new);
        log.info("Request for user {}", user.getUserName());
    }

    @Pointcut("execution(@nl.kooi.camundarules.configuration.UserIsAuthorized * *(..))")
    public void methodAnnotatedWithTraceAccessDetails() {
    }

}