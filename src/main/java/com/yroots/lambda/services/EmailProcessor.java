package com.yroots.lambda.services;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.util.StringUtils;

import com.yroots.lambda.domain.EmailAccount;
import com.yroots.lambda.models.EmailPayload;

public class EmailProcessor implements Runnable {

	private Logger logger = LoggerFactory.getLogger(getClass());
	private String topic;
	private EmailAccount account;
	private EmailPayload payload;
	private WorkStatusListener listener;

	private EmailProcessor(String topic, EmailAccount account, EmailPayload payload, WorkStatusListener listener) {
		this.topic = topic;
		this.account = account;
		this.payload = payload;
		this.listener = listener;
	}

	public static EmailProcessor newInstance(String topic, EmailAccount account, EmailPayload payload,
			WorkStatusListener listener) {
		return new EmailProcessor(topic, account, payload, listener);
	}

	private JavaMailSender getJavaMailSender(EmailAccount account) {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost(account.getHost());
		mailSender.setPort(account.getPort());

		mailSender.setUsername(account.getUsername());
		mailSender.setPassword(account.getPassword());

		Properties props = mailSender.getJavaMailProperties();
		props.put("mail.transport.protocol", account.getProtocol());
		props.put("mail.smtp.auth", account.isSmtpAuth());
		props.put("mail.smtp.starttls.enable", account.isTlsEnabled());
		props.put("mail.debug", account.isDebugEnabled());

		return mailSender;
	}

	@Override
	public void run() {
		if (payload == null) {
			logger.info("No Email payload");
			return;
		}

		if (StringUtils.hasText(payload.getSubject()) && payload.getToEmails() != null
				&& payload.getToEmails().size() > 0 && StringUtils.hasText(payload.getText())) {
			JavaMailSender sender = getJavaMailSender(account);

			MimeMessage mimeMessage = sender.createMimeMessage();
			boolean success = false;
			Throwable throwable = null;
			try {
				MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
				mimeMessageHelper.setSubject(payload.getSubject());
				if (StringUtils.hasText(payload.getEmailFromName()))
					mimeMessageHelper.setFrom(new InternetAddress(account.getUsername(), payload.getEmailFromName()));
				else
					mimeMessageHelper.setFrom(new InternetAddress(account.getUsername(), account.getFormatedName()));

				if (payload.getToEmails() != null) {
					for (String e : payload.getToEmails())
						mimeMessageHelper.addTo(e);
				}
				if (payload.getCcEmails() != null) {
					for (String e : payload.getCcEmails())
						mimeMessageHelper.addCc(e);
				}

				if (payload.getBccEmails() != null) {
					for (String e : payload.getBccEmails())
						mimeMessageHelper.addBcc(e);
				}

				mimeMessageHelper.setText(payload.getText(), true);
				sender.send(mimeMessageHelper.getMimeMessage());
				success = true;
			} catch (MessagingException e) {
				e.printStackTrace();
				throwable = e;
				success = false;
			} catch (Exception e) {
				e.printStackTrace();
				throwable = e;
				success = false;
			}

			Map<String, Object> st = new LinkedHashMap<>();
			if (listener != null && success) {
				st.put("status", "success");
				st.put("msg", "");
				st.put("topic", topic);
				st.put("from", account.getName());
				st.put("sub", payload.getSubject());
				st.put("to", payload.getToEmails());
				st.put("cc", payload.getCcEmails());
				st.put("bcc", payload.getBccEmails());
				st.put("text", payload.getText());
				listener.success(st);
			} else if (listener != null) {
				st.put("status", "failed");
				st.put("msg", throwable.getMessage());
				st.put("topic", topic);
				st.put("from", account.getName());
				st.put("sub", payload.getSubject());
				st.put("to", payload.getToEmails());
				st.put("cc", payload.getCcEmails());
				st.put("bcc", payload.getBccEmails());
				st.put("text", payload.getText());
				listener.fail(st);
			}
		}
	}
}
