package com.javaportal.utility;

import org.apache.logging.log4j.LogManager;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.apache.logging.log4j.Logger;

import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
	
	private static final Logger LOGGER = LogManager.getLogger(LoggingAspect.class);
	
	@AfterThrowing(pointcut="execution(* com.javaportal.service.*Impl.*(..))", throwing="exception")
	public void logAllServiceException(Exception e) throws Exception{
		LOGGER.info(e.getMessage(), e);
	}
}
