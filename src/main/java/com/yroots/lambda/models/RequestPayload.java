package com.yroots.lambda.models;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class RequestPayload {

	@NotNull
	@NotBlank
	private String serviceName;

	@NotNull
	@NotBlank
	private String category;
	
	private SmsPayload smsPayload;
	private EmailPayload emailPayload;
	public String getServiceName() {
		return serviceName;
	}
	public String getCategory() {
		return category;
	}
	public SmsPayload getSmsPayload() {
		return smsPayload;
	}
	public EmailPayload getEmailPayload() {
		return emailPayload;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public void setSmsPayload(SmsPayload smsPayload) {
		this.smsPayload = smsPayload;
	}
	public void setEmailPayload(EmailPayload emailPayload) {
		this.emailPayload = emailPayload;
	}
	@Override
	public String toString() {
		return "RequestPayload [serviceName=" + serviceName + ", category=" + category + ", smsPayload=" + smsPayload
				+ ", emailPayload=" + emailPayload + "]";
	}
	
	
}
