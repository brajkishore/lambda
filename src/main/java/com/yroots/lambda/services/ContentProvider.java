package com.yroots.lambda.services;

import org.springframework.util.StringUtils;

import com.yroots.lambda.configs.Util;
import com.yroots.lambda.models.EmailPayload;
import com.yroots.lambda.models.RequestPayload;
import com.yroots.lambda.models.SmsPayload;

public class ContentProvider {

	private RequestPayload request;

	private ContentProvider(RequestPayload request) {
		this.request = request;
	}

	public static ContentProvider newInstance(RequestPayload request) {
		return new ContentProvider(request);
	}

	public String getSmsFormattedText() {
		String text = "";

		if (request.getSmsPayload() != null) {
			SmsPayload payload = request.getSmsPayload();
			// Check if template url given, yes, download and use that to send text
			if (StringUtils.hasText(payload.getTemplateUrl())) {
				// Currently not supported
			} else if (StringUtils.hasText(payload.getText())) {
				text = payload.getText();
			} else {
				text = request.getServiceName().toLowerCase() + "_" + request.getCategory().toLowerCase() + "_sms.ftl";
			}
			text = Util.getFormattedText(text, Util.getParams(payload.getData()));
		}
		return text;
	}
	
	public String getSubsSmsFormattedText() {
		String text = "";

		if (request.getSmsPayload() != null) {
			SmsPayload payload = request.getSmsPayload();
			// Check if template url given, yes, download and use that to send text
			if (StringUtils.hasText(payload.getTemplateUrl())) {
				// Currently not supported
			} else if (StringUtils.hasText(payload.getText())) {
				text = payload.getText();
			} else {
				text = request.getServiceName().toLowerCase() + "_" + request.getCategory().toLowerCase() + "_sub_sms.ftl";
			}
			text = Util.getFormattedText(text, Util.getParams(payload.getData()));
		}
		return text;			
	}
	
	public String getEmailFormattedText() {
		String text = "";

		if (request.getEmailPayload()!= null) {
			EmailPayload payload = request.getEmailPayload();
			// Check if template url given, yes, download and use that to send text
			if (StringUtils.hasText(payload.getTemplateUrl())) {
				// Currently not supported
			} else if (StringUtils.hasText(payload.getText())) {
				text = payload.getText();
			} else {
				text = request.getServiceName().toLowerCase() + "_" + request.getCategory().toLowerCase() + "_email.ftl";
			}
			text = Util.getFormattedText(text, Util.getParams(payload.getData()));
		}
		return text;
	}

	public String getSubsEmailFormattedText() {
		String text = "";

		if (request.getEmailPayload()!= null) {
			EmailPayload payload = request.getEmailPayload();
			// Check if template url given, yes, download and use that to send text
			if (StringUtils.hasText(payload.getTemplateUrl())) {
				// Currently not supported
			} else if (StringUtils.hasText(payload.getText())) {
				text = payload.getText();
			} else {
				text = request.getServiceName().toLowerCase() + "_" + request.getCategory().toLowerCase() + "_sub_email.ftl";
			}
			text = Util.getFormattedText(text, Util.getParams(payload.getData()));
		}
		return text;
	}
}
