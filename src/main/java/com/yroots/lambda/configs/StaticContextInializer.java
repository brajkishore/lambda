package com.yroots.lambda.configs;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yroots.lambda.dao.CategoryRepository;
import com.yroots.lambda.dao.EmailAccountRepository;
import com.yroots.lambda.dao.ServiceAccountRepository;
import com.yroots.lambda.dao.SmsAccountRepository;
import com.yroots.lambda.dao.SubscriptionRepository;
import com.yroots.lambda.dao.UserRepository;
import com.yroots.lambda.services.GlobalExecutorService;
import com.yroots.lambda.services.StaticContextProvider;

import freemarker.template.Configuration;

@Component
public class StaticContextInializer {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ServiceAccountRepository serviceAccountRepository;
	
	@Autowired
	private EmailAccountRepository emailAccountRepository;
	
	@Autowired
	private SmsAccountRepository smsAccountRepository;
	
	@Autowired
	private SubscriptionRepository subscriptionRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private GlobalExecutorService executorService;
	
	@Autowired
    private Configuration freemarkerConfig;
	
	@PostConstruct
	public void init () {
		logger.info("Initializing " + getClass().getSimpleName());
		StaticContextProvider.setCategoryRepository(categoryRepository);
		StaticContextProvider.setSubscriptionRepository(subscriptionRepository);
		StaticContextProvider.setSmsAccountRepository(smsAccountRepository);
		StaticContextProvider.setEmailAccountRepository(emailAccountRepository);
		StaticContextProvider.setServiceAccountRepository(serviceAccountRepository);
		StaticContextProvider.setUserRepository(userRepository);
		StaticContextProvider.setExecutorService(executorService);
		StaticContextProvider.setFreemarkerConfig(freemarkerConfig);
		logger.info("Initialized " + getClass().getSimpleName());
	}	
}
