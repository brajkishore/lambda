package com.yroots.lambda.configs;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoggerConfig {
	private Logger logger=LoggerFactory.getLogger(getClass());
	
	private Logger smsAuditLogger;
	private Logger emailAuditLogger;
	
	@PostConstruct
	public void init() {
		smsAuditLogger=LoggerFactory.getLogger("smsAuditLogger");
		emailAuditLogger=LoggerFactory.getLogger("emailAuditLogger");
		logger.info(getClass().getSimpleName()+ " is initialized");
	}

	public Logger getSmsAuditLogger() {
		return smsAuditLogger;
	}

	public Logger getEmailAuditLogger() {
		return emailAuditLogger;
	}
	
}
