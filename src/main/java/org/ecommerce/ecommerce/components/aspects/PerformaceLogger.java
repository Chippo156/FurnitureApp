package org.ecommerce.ecommerce.components.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

@Component
@Aspect
public class PerformaceLogger {

    private final Logger logger = Logger.getLogger(getClass().getName());

    private String getMethodName(JoinPoint joinPoint) {
        return joinPoint.getSignature().getName();
    }
    @Pointcut("within(org.ecommerce.ecommerce.controllers.*)")
    public void controllerMethod() {
    }
    @Before("controllerMethod()")
    public void beforeMethodExecution(JoinPoint joinPoint){

        logger.info("Method started: " + getMethodName(joinPoint));
    }
    @After("controllerMethod()")
    public void afterMethodExecution(JoinPoint joinPoint){
        logger.info("Method finished: " + getMethodName(joinPoint));
    }

    @Around("controllerMethod()")
    public Object measureControllerMethodExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.nanoTime();
        Object result =joinPoint.proceed();
        long end = System.nanoTime();
        String methodName = joinPoint.getSignature().getName();
        logger.info("Execution time of " + methodName + " is " + TimeUnit.NANOSECONDS.toMillis(end - start) + " ms");
        return result;
    }


}
