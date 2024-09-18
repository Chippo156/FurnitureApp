package org.ecommerce.ecommerce.components.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.logging.Logger;

@Component
@Aspect
public class UserActivityLogger {
    private final Logger logger = Logger.getLogger(getClass().getName());


    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void controllerMethod() {
    }

    @Around("controllerMethod() && execution(* org.ecommerce.ecommerce.controllers.UserController*.*(..))")
    public Object loggerUserActivity(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        String remoteAddress = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest().getRemoteUser();
        logger.info("User Activity started: " + methodName + ", IP address: " + remoteAddress);
        Object result = joinPoint.proceed();
        logger.info("User Activity finished: " + methodName + ", IP address: " + remoteAddress);
        return result;}

}
