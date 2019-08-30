package com.yroots.lambda.services;

import java.io.NotActiveException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.util.StringUtils;

import com.yroots.lambda.domain.Category;
import com.yroots.lambda.domain.ServiceAccount;
import com.yroots.lambda.domain.Subscription;
import com.yroots.lambda.models.RequestPayload;

public class CoreProcessor implements Runnable {

	private Logger logger = LoggerFactory.getLogger(getClass());

	private RequestPayload request = null;

	private CoreProcessor(RequestPayload request) {
		this.request = request;
	}

	public static CoreProcessor newInstance(RequestPayload request) {
		return new CoreProcessor(request);
	}

	@Override
	public void run() {
		try {
			if (StringUtils.hasText(request.getServiceName()) && StringUtils.hasText(request.getServiceName())) {
				ServiceAccount serviceAccount = StaticContextProvider.getServiceAccountRepository()
						.findById(request.getServiceName())
						.orElseThrow(()->new ResourceNotFoundException("No service by name  " + request.getServiceName()));
				
				if(!serviceAccount.isActive())
					throw new NotActiveException(serviceAccount.getName()+" is inactive");
				
				Category category = StaticContextProvider.getCategoryRepository()
						.findByName(request.getCategory())
						.orElseThrow(()->new ResourceNotFoundException("No category by name  " + request.getCategory()));

				if(!category.isActive())
					throw new NotActiveException(category.getName()+" is inactive");
								
				String subTopic=serviceAccount.getName()+"#"+category.getName();
				List<Subscription> subscriptions = StaticContextProvider.getSubscriptionRepository()
						.findAllByTopicName(subTopic);

				if(category.isEmailActive()) {
					//prep email and send to email sender
					prepAndSendEmailer(serviceAccount,category,subscriptions);
				}
				if(category.isSMSActive()) {
					//prep sms and send to sms sender
					prepAndSendSMSSener(serviceAccount,category,subscriptions);
				}
			}
		} catch (Exception e) {
			logger.error("Error "+ e.getMessage());
		}
	}

	private void prepAndSendSMSSener(ServiceAccount serviceAccount, Category category,
			List<Subscription> subscriptions) {
		// TODO Auto-generated method stub
		
	}

	private void prepAndSendEmailer(ServiceAccount serviceAccount, Category category,
			List<Subscription> subscriptions) {
		// TODO Auto-generated method stub
		
	}
}
