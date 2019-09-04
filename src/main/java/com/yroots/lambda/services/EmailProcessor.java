package com.yroots.lambda.services;

import java.io.IOException;
import java.util.HashMap;
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
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.util.StringUtils;

import com.yroots.lambda.domain.EmailAccount;
import com.yroots.lambda.models.RequestPayload;

import freemarker.core.ParseException;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;

public class EmailProcessor implements Runnable {

	private Logger logger = LoggerFactory.getLogger(getClass());

	private WorkStatusListener listener;
	private String message;
	private EmailAccount account;
	private RequestPayload request;

	private EmailProcessor(EmailAccount account, RequestPayload request, String message, WorkStatusListener listener) {
		this.account = account;
		this.request = request;
		this.listener = listener;
		this.message = message;
	}

	public static EmailProcessor newInstance(EmailAccount account, RequestPayload request, String message,
			WorkStatusListener listener) {
		return new EmailProcessor(account, request, message, listener);
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
		if (StringUtils.hasText(request.getSubject()) && StringUtils.hasText(message) && request.getToEmails() != null
				&& request.getToEmails().size() > 0) {
			String msg = "";
			if (message.endsWith(".ftl")) {
				Template t;
				try {
					Map<String, Object> params = new HashMap<>();
					if (request.getData() != null)
						request.getData().forEach(d -> {
							params.put(d.getKey(), d.getValue());
						});

					t = StaticContextProvider.getFreemarkerConfig().getTemplate(message);
					msg = FreeMarkerTemplateUtils.processTemplateIntoString(t, params);
				} catch (TemplateNotFoundException e) {
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

			logger.debug("Emails:text:" + msg);
			if (StringUtils.hasText(msg)) {
				JavaMailSender sender = getJavaMailSender(account);

				MimeMessage mimeMessage = sender.createMimeMessage();
				boolean success = false;
				try {
					MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
					mimeMessageHelper.setSubject(request.getSubject());
					if (StringUtils.hasText(request.getEmailFromName()))
						mimeMessageHelper.setFrom(new InternetAddress(account.getUsername(), request.getEmailFromName()));
					else
						mimeMessageHelper.setFrom(new InternetAddress(account.getUsername(), account.getFormatedName()));

					if (request.getToEmails() != null) {
						for(String e:request.getToEmails())
							mimeMessageHelper.addTo(e);
					}					
					if (request.getCcEmails() != null) {
						for(String e:request.getCcEmails())
							mimeMessageHelper.addCc(e);
					}
					
					if (request.getBccEmails() != null) {
						for(String e:request.getBccEmails())
							mimeMessageHelper.addBcc(e);
					}
					
					mimeMessageHelper.setText(msg, true);
					sender.send(mimeMessageHelper.getMimeMessage());
					success = true;
				} catch (MessagingException e) {
					e.printStackTrace();
					success = false;
				} catch (Exception e) {
					e.printStackTrace();
					success = false;
				}

				if (listener != null && success) {
					listener.success("Success");
				} else if (listener != null) {
					listener.success("Email failed");
				}
			}
		}

	}
}
