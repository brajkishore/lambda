package com.yroots.lambda.services;

import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import com.yroots.lambda.domain.SMSAccount;

import freemarker.core.ParseException;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;

public class SmsProcessor implements Runnable {
	private Logger logger = LoggerFactory.getLogger(getClass());

	private Map<String, Object> params;
	private WorkStatusListener listener;
	private String message;
	private SMSAccount account;
	private String contacts;

	private SmsProcessor(SMSAccount account, String contacts, Map<String, Object> params, String message,
			WorkStatusListener listener) {
		this.account = account;
		this.contacts = contacts;
		this.params = params;
		this.listener = listener;
		this.message = message;
	}

	public static SmsProcessor newInstance(SMSAccount account, String contacts, Map<String, Object> params,
			String message, WorkStatusListener listener) {
		return new SmsProcessor(account, contacts, params, message, listener);
	}

	@Override
	public void run() {
		if (StringUtils.hasText(message)) {
			String msg = "";
			if (message.endsWith(".ftl")) {
				Template t;
				try {
					t = StaticContextProvider.getFreemarkerConfig().getTemplate(message);
					msg = FreeMarkerTemplateUtils.processTemplateIntoString(t, params);
				} catch (TemplateNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (MalformedTemplateNameException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (TemplateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else
				msg = message;
			logger.debug("Contacts:"+contacts+":text:"+msg);
			if (StringUtils.hasText(msg) && StringUtils.hasText(contacts)) {
				RestTemplate restTemplate = new RestTemplate();
				String baseUrl = account.getUrl();
				String url = baseUrl.replaceAll("<MOBILES>", contacts);
				url = url.replaceAll("<MESSAGE>", msg);
				logger.debug("SMS URL:" + url);
				ResponseEntity<String> response = restTemplate.getForEntity(url.toString(), String.class);
				if (listener != null && response.getStatusCodeValue() == 200) {
					listener.success("Success");
				} else if (listener != null) {
					listener.success("failed with " + response.getStatusCodeValue());
				}
			}
		}
	}
}
