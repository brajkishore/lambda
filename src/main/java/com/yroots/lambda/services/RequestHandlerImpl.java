package com.yroots.lambda.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yroots.lambda.models.RequestPayload;

@Service("requestHandler")
public class RequestHandlerImpl implements RequestHandler {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private GlobalExecutorService executorService;
	
	@Override
	public void handle(RequestPayload request) {
		executorService.submit(CoreProcessor.newInstance(request));
	}
}
