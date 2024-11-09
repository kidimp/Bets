package org.chous.bets.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ControllerLoggingAspect {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Around("execution(* org.chous.bets.controllers.*.*(..))")
    public Object logAroundControllers(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        Object[] methodArgs = joinPoint.getArgs();

        logger.info("Calling method {} with parameters: {}", methodName, methodArgs);

        Object result = joinPoint.proceed();

        logger.info("Method {} executed with result: {}", methodName, result);

        return result;
    }
}
