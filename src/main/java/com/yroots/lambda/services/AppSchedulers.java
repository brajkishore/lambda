package com.yroots.lambda.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class AppSchedulers {
	private Logger logger=LoggerFactory.getLogger(getClass());
	
	@EventListener(ApplicationReadyEvent.class)
	public void onApplicationInit() {
		
	}
	
}
