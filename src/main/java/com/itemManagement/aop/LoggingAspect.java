package com.itemManagement.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;



@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Before("execution(* com.itemManagement.service..*(..))")
    public void logBeforeService(JoinPoint joinPoint){
        logger.debug("About to run method : " + joinPoint.getSignature().getName());
        logger.info("About to run method : " + joinPoint.getSignature().getName());
    }



}
