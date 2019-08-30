package com.yroots.lambda.services;

import java.util.concurrent.ExecutorService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service("requestHandler")
public class RequestHandlerImpl implements RequestHandler {

	@Value("${core.processor.max.threads}")
	private int maxThreads;
	
	private ExecutorService executorService;
	
	
	@Override
	public void handle(Object request) {		
	}
}
