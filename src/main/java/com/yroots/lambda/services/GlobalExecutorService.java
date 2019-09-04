package com.yroots.lambda.services;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GlobalExecutorService {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Value("${core.processor.max.threads}")
	private int maxThreads;

	private ExecutorService executorService;
	
	@PostConstruct
	public void init() {
		logger.info("Initializing " + getClass().getSimpleName());
		executorService = Executors.newFixedThreadPool(maxThreads > 0 ? maxThreads : 2);
		logger.info("Initialized " + getClass().getSimpleName());
	}

	public void submit(Runnable task) {
		executorService.submit(task);
	}
}
