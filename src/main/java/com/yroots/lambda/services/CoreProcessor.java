package com.yroots.lambda.services;

import java.io.NotActiveException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.util.StringUtils;

import com.yroots.lambda.domain.Category;
import com.yroots.lambda.domain.ServiceAccount;
import com.yroots.lambda.domain.Subscription;
import com.yroots.lambda.domain.User;
import com.yroots.lambda.models.EmailPayload;
import com.yroots.lambda.models.RequestPayload;
import com.yroots.lambda.models.SmsPayload;

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
						.findByName(request.getServiceName()).orElseThrow(
								() -> new ResourceNotFoundException("No service by name  " + request.getServiceName()));

				logger.debug("serviceAccount:" + serviceAccount.getName() + ":" + serviceAccount.isActive());
				if (!serviceAccount.isActive())
					throw new NotActiveException(serviceAccount.getName() + " is inactive");

				Category category = StaticContextProvider.getCategoryRepository().findByName(request.getCategory())
						.orElseThrow(
								() -> new ResourceNotFoundException("No category by name  " + request.getCategory()));

				logger.debug("category:" + category.getName() + ":" + category.isActive());

				if (!category.isActive())
					throw new NotActiveException(category.getName() + " is inactive");

				String subTopic = serviceAccount.getName() + "#" + category.getName();
				List<Subscription> subscriptions = StaticContextProvider.getSubscriptionRepository()
						.findAllByTopicName(subTopic);

				logger.debug("category:" + category.getName() + ":isEmailActive:" + category.isEmailActive());

				if (category.isEmailActive()) {
					// prep email and send to email sender
					prepAndSendEmailer(serviceAccount, category, subscriptions, request);
				}
				logger.debug("category:" + category.getName() + ":isSMSActive:" + category.isSmsActive());
				if (category.isSmsActive()) {
					// prep sms and send to sms sender
					prepAndSendSMSSender(serviceAccount, category, subscriptions, request);
				}
			}
		} catch (Exception e) {
			logger.error("Error " + e.getMessage());
			e.printStackTrace();
		}
	}

	private void prepAndSendSMSSender(ServiceAccount serviceAccount, Category category,
			List<Subscription> subscriptions, RequestPayload request) {

		logger.debug("Preparing to send  sms");
		ContentProvider contentProvider = ContentProvider.newInstance(request);

		if (request.getSmsPayload() != null) {
			String msg = contentProvider.getSmsFormattedText();
			SmsPayload payload = new SmsPayload();
			BeanUtils.copyProperties(request.getSmsPayload(), payload);
			payload.setText(msg);
			String topic = serviceAccount.getName() + "#" + category.getName();
			SmsProcessor task = SmsProcessor.newInstance(topic, category.getSmsAccount(), payload,
					new SmsStatusListener());
			StaticContextProvider.getExecutorService().submit(task);
		}
		// Process for subscribers
		if (subscriptions != null) {
			List<List<String>> allTos = subscriptions.stream().filter(e -> e.isSMSSub()).map(v -> {
				try {
					Optional<User> u = StaticContextProvider.getUserRepository().findById(v.getId());
					if (u.isPresent() && u.get().getContacts() != null)
						return u.get().getContacts();
				} catch (Exception e) {
				}
				return null;
			}).filter(o -> o != null).collect(Collectors.toList());

			List<String> tos = new ArrayList<>();
			if (allTos != null)
				allTos.forEach(l -> {
					tos.addAll(l);
				});

			if (tos.size() > 0) {
				SmsPayload payload = new SmsPayload();
				String topic = serviceAccount.getName() + "#" + category.getName();
				payload.setContacts(tos);
				payload.setText(contentProvider.getSubsSmsFormattedText());
				SmsProcessor task = SmsProcessor.newInstance(topic, category.getSmsAccount(), payload,
						new SmsStatusListener());
				StaticContextProvider.getExecutorService().submit(task);
			}
		}
	}

	private void prepAndSendEmailer(ServiceAccount serviceAccount, Category category, List<Subscription> subscriptions,
			RequestPayload request) {

		logger.debug("Preparing to send  emails");
		ContentProvider contentProvider = ContentProvider.newInstance(request);
		if (request.getEmailPayload() != null) {
			String msg = contentProvider.getEmailFormattedText();
			EmailPayload payload = new EmailPayload();
			BeanUtils.copyProperties(request.getEmailPayload(), payload);
			payload.setText(msg);
			String topic = serviceAccount.getName() + "#" + category.getName();
			EmailProcessor task = EmailProcessor.newInstance(topic, category.getEmailAccount(), payload,
					new EmailStatusListener());
			StaticContextProvider.getExecutorService().submit(task);
		}

		// Process for subscribers
		if (subscriptions != null) {
			List<List<String>> allTos = subscriptions.stream().filter(e -> e.isEmailSub()).map(v -> {
				try {
					Optional<User> u = StaticContextProvider.getUserRepository().findById(v.getId());
					if (u.isPresent() && u.get().getEmails() != null)
						return u.get().getEmails();
				} catch (Exception e) {
				}
				return null;
			}).filter(o -> o != null).collect(Collectors.toList());

			List<String> tos = new ArrayList<>();
			if (allTos != null)
				allTos.forEach(l -> {
					tos.addAll(l);
				});

			if (tos.size() > 0) {
				EmailPayload payload = new EmailPayload();
				String topic = serviceAccount.getName() + "#" + category.getName();
				payload.setToEmails(tos);
				payload.setText(contentProvider.getSubsEmailFormattedText());
				EmailProcessor task = EmailProcessor.newInstance(topic, category.getEmailAccount(), payload,
						new EmailStatusListener());
				StaticContextProvider.getExecutorService().submit(task);
			}
		}
	}
}
