package com.yroots.lambda.services;

import java.io.NotActiveException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.util.StringUtils;

import com.yroots.lambda.domain.Category;
import com.yroots.lambda.domain.ServiceAccount;
import com.yroots.lambda.domain.Subscription;
import com.yroots.lambda.domain.User;
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
						.findByName(request.getServiceName())
						.orElseThrow(()->new ResourceNotFoundException("No service by name  " + request.getServiceName()));
				
				logger.debug("serviceAccount:"+serviceAccount.getName()+":"+serviceAccount.isActive());
				if(!serviceAccount.isActive())
					throw new NotActiveException(serviceAccount.getName()+" is inactive");
				
				Category category = StaticContextProvider.getCategoryRepository()
						.findByName(request.getCategory())
						.orElseThrow(()->new ResourceNotFoundException("No category by name  " + request.getCategory()));
				
				logger.debug("category:"+category.getName()+":"+category.isActive());
				
				if(!category.isActive())
					throw new NotActiveException(category.getName()+" is inactive");
								
				String subTopic=serviceAccount.getName()+"#"+category.getName();
				List<Subscription> subscriptions = StaticContextProvider.getSubscriptionRepository()
						.findAllByTopicName(subTopic);

				logger.debug("category:"+category.getName()+":isEmailActive:"+category.isEmailActive());
				
				if(category.isEmailActive()) {
					//prep email and send to email sender
					prepAndSendEmailer(serviceAccount,category,subscriptions,request);
				}			
				logger.debug("category:"+category.getName()+":isSMSActive:"+category.isSmsActive());
				if(category.isSmsActive()) {
					//prep sms and send to sms sender
					prepAndSendSMSSener(serviceAccount,category,subscriptions,request);
				}
			}
		} catch (Exception e) {
			logger.error("Error "+ e.getMessage());
			e.printStackTrace();
		}
	}

	private void prepAndSendSMSSener(ServiceAccount serviceAccount, Category category,
			List<Subscription> subscriptions, RequestPayload request) {
				
		String msg="";
		if(!StringUtils.hasText(request.getFormattedText()))
			msg=serviceAccount.getName().toLowerCase()+"_"+category.getName().toLowerCase()+"_sms.ftl";			
		String conts="";
		if(request.getContacts()!=null)
			conts=String.join(",",request.getContacts());
		
		Map<String,Object> params=new HashMap<>();
		if(request.getData()!=null) {
			request.getData().forEach(d->{
				params.put(d.getKey(),d.getValue());
			});
		}
		SmsProcessor task=SmsProcessor.newInstance(category.getSmsAccount(), conts, params, msg, null);
		StaticContextProvider.getExecutorService().submit(task);
		
		if(subscriptions!=null) {
			conts=null;
			List<String> s=subscriptions.stream().map(v->{
				try {
					Optional<User> u=StaticContextProvider.getUserRepository().findById(v.getId());
					if(u.isPresent() && u.get().getContacts()!=null)
						return String.join(",", u.get().getContacts());
				} catch (Exception e) {					
				}		
				return null;
			}).filter(o->o!=null).collect(Collectors.toList());
			
			if(s!=null)
				conts=String.join(",",s);
			
			if(StringUtils.hasText(conts)) {
				msg=serviceAccount.getName().toLowerCase()+"_"+category.getName().toLowerCase()+"_sub_sms.ftl";			
				task=SmsProcessor.newInstance(category.getSmsAccount(), conts, params, msg, null);
				StaticContextProvider.getExecutorService().submit(task);			
			}
		}
	}

	private void prepAndSendEmailer(ServiceAccount serviceAccount, Category category,
			List<Subscription> subscriptions, RequestPayload request2) {
		
		String msg="";
		if(!StringUtils.hasText(request.getFormattedText()))
			msg=serviceAccount.getName().toLowerCase()+"_"+category.getName().toLowerCase()+"_email.ftl";
		
		EmailProcessor task=EmailProcessor.newInstance(category.getEmailAccount(), request, msg, null);
		StaticContextProvider.getExecutorService().submit(task);
		
		if(subscriptions!=null) {			
			List<List<String>> allEmails=subscriptions.stream().map(v->{
				try {
					Optional<User> u=StaticContextProvider.getUserRepository().findById(v.getId());
					if(u.isPresent() && u.get().getEmails()!=null)
						return u.get().getEmails();
				} catch (Exception e) {					
				}		
				return null;
			}).filter(o->o!=null).collect(Collectors.toList());
			
			List<String> emails=new ArrayList<>();
			if(allEmails!=null)
				allEmails.forEach(l->{
					emails.addAll(l);
				});
			
			if(emails.size()>0) {
				request.setToEmails(emails);
				request.setCcEmails(null);
				request.setBccEmails(null);
				msg=serviceAccount.getName().toLowerCase()+"_"+category.getName().toLowerCase()+"_sub_email.ftl";			
				task=EmailProcessor.newInstance(category.getEmailAccount(),request, msg, null);
				StaticContextProvider.getExecutorService().submit(task);			
			}
		}
	}
}
