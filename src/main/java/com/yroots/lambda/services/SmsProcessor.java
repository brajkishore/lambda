package com.yroots.lambda.services;

import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import com.yroots.lambda.domain.SMSAccount;
import com.yroots.lambda.models.SmsPayload;

public class SmsProcessor implements Runnable {
	private Logger logger = LoggerFactory.getLogger(getClass());

	private String topic;
	private SMSAccount account;
	private SmsPayload payload;
	private WorkStatusListener listener;

	private SmsProcessor(String topic, SMSAccount account, SmsPayload payload, WorkStatusListener listener) {
		this.topic = topic;
		this.account = account;
		this.payload = payload;
		this.listener = listener;
	}

	public static SmsProcessor newInstance(String topic, SMSAccount account, SmsPayload payload,
			WorkStatusListener listener) {
		return new SmsProcessor(topic, account, payload, listener);
	}

	@Override
	public void run() {
		if (payload == null) {
			logger.info("No Email payload");
			return;
		}
		if (StringUtils.hasText(payload.getText()) && payload.getContacts() != null
				&& payload.getContacts().size() > 0) {
			RestTemplate restTemplate = new RestTemplate();
			String baseUrl = account.getUrl();
			String url = baseUrl.replaceAll("<MOBILES>", String.join(",", payload.getContacts()));
			url = url.replaceAll("<MESSAGE>", payload.getText());
			logger.debug("SMS URL:" + url);
			ResponseEntity<String> response = restTemplate.getForEntity(url.toString(), String.class);
			Map<String, Object> st = new LinkedHashMap<>();
			if (listener != null && response.getStatusCodeValue() == 200) {
				st.put("status", "success");
				st.put("msg", response.getBody());
				st.put("topic", topic);
				st.put("from", account.getName());
				st.put("to", payload.getContacts());
				st.put("text", payload.getText());
				listener.success(st);
			} else if (listener != null) {
				st.put("status", "failed");
				st.put("msg", response.getBody());
				st.put("topic", topic);
				st.put("from", account.getName());
				st.put("to", payload.getContacts());
				st.put("text", payload.getText());
				listener.fail(st);
			}
		}
	}
}
