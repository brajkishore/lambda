package com.yroots.lambda.models;

import java.util.List;

public class EmailPayload{
	private String subject;
	private String emailFromName;
	private List<String> toEmails;
	private List<String> ccEmails;
	private List<String> bccEmails;
	private String templateUrl;	
	private String text;	
	private List<KeyValue> data;
	private List<String> attachments;
	public String getSubject() {
		return subject;
	}
	public String getEmailFromName() {
		return emailFromName;
	}
	public List<String> getToEmails() {
		return toEmails;
	}
	public List<String> getCcEmails() {
		return ccEmails;
	}
	public List<String> getBccEmails() {
		return bccEmails;
	}
	public String getTemplateUrl() {
		return templateUrl;
	}
	public String getText() {
		return text;
	}
	public List<KeyValue> getData() {
		return data;
	}
	public List<String> getAttachments() {
		return attachments;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public void setEmailFromName(String emailFromName) {
		this.emailFromName = emailFromName;
	}
	public void setToEmails(List<String> toEmails) {
		this.toEmails = toEmails;
	}
	public void setCcEmails(List<String> ccEmails) {
		this.ccEmails = ccEmails;
	}
	public void setBccEmails(List<String> bccEmails) {
		this.bccEmails = bccEmails;
	}
	public void setTemplateUrl(String templateUrl) {
		this.templateUrl = templateUrl;
	}
	public void setText(String text) {
		this.text = text;
	}
	public void setData(List<KeyValue> data) {
		this.data = data;
	}
	public void setAttachments(List<String> attachments) {
		this.attachments = attachments;
	}	
}
