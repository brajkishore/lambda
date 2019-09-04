package com.yroots.lambda.services;

import com.yroots.lambda.dao.CategoryRepository;
import com.yroots.lambda.dao.EmailAccountRepository;
import com.yroots.lambda.dao.ServiceAccountRepository;
import com.yroots.lambda.dao.SmsAccountRepository;
import com.yroots.lambda.dao.SubscriptionRepository;
import com.yroots.lambda.dao.UserRepository;

import freemarker.template.Configuration;

public class StaticContextProvider {

	private static UserRepository userRepository;
	private static ServiceAccountRepository serviceAccountRepository;
	private static EmailAccountRepository emailAccountRepository;
	private static SmsAccountRepository smsAccountRepository;
	private static SubscriptionRepository subscriptionRepository;
	private static CategoryRepository categoryRepository;
	private static GlobalExecutorService executorService;
	private static Configuration freemarkerConfig;
	public static UserRepository getUserRepository() {
		return userRepository;
	}
	public static ServiceAccountRepository getServiceAccountRepository() {
		return serviceAccountRepository;
	}
	public static EmailAccountRepository getEmailAccountRepository() {
		return emailAccountRepository;
	}
	public static SmsAccountRepository getSmsAccountRepository() {
		return smsAccountRepository;
	}
	public static SubscriptionRepository getSubscriptionRepository() {
		return subscriptionRepository;
	}
	public static CategoryRepository getCategoryRepository() {
		return categoryRepository;
	}
	public static void setUserRepository(UserRepository userRepository) {
		StaticContextProvider.userRepository = userRepository;
	}
	public static void setServiceAccountRepository(ServiceAccountRepository serviceAccountRepository) {
		StaticContextProvider.serviceAccountRepository = serviceAccountRepository;
	}
	public static void setEmailAccountRepository(EmailAccountRepository emailAccountRepository) {
		StaticContextProvider.emailAccountRepository = emailAccountRepository;
	}
	public static void setSmsAccountRepository(SmsAccountRepository smsAccountRepository) {
		StaticContextProvider.smsAccountRepository = smsAccountRepository;
	}
	public static void setSubscriptionRepository(SubscriptionRepository subscriptionRepository) {
		StaticContextProvider.subscriptionRepository = subscriptionRepository;
	}
	public static void setCategoryRepository(CategoryRepository categoryRepository) {
		StaticContextProvider.categoryRepository = categoryRepository;
	}
	public static GlobalExecutorService getExecutorService() {
		return executorService;
	}
	public static void setExecutorService(GlobalExecutorService executorService) {
		StaticContextProvider.executorService = executorService;
	}
	public static Configuration getFreemarkerConfig() {
		return freemarkerConfig;
	}
	public static void setFreemarkerConfig(Configuration freemarkerConfig) {
		StaticContextProvider.freemarkerConfig = freemarkerConfig;
	}
}
