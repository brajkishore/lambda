package com.yroots.lambda.models;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class RequestPayload {

	@NotNull
	@NotBlank
	private String serviceName;

	@NotNull
	@NotBlank
	private String category;
	private List<String> contacts;
	private String subject;
	private String emailFromName;
	private List<String> toEmails;
	private List<String> ccEmails;
	private List<String> bccEmails;
	private String formattedText;
	private List<KeyValue> data;

	public String getServiceName() {
		return serviceName;
	}

	public String getCategory() {
		return category;
	}

	public List<String> getContacts() {
		return contacts;
	}

	public String getFormattedText() {
		return formattedText;
	}

	public List<KeyValue> getData() {
		return data;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public void setContacts(List<String> contacts) {
		this.contacts = contacts;
	}

	public void setFormattedText(String formattedText) {
		this.formattedText = formattedText;
	}

	public void setData(List<KeyValue> data) {
		this.data = data;
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

	public void setToEmails(List<String> toEmails) {
		this.toEmails = toEmails;
	}

	public void setCcEmails(List<String> ccEmails) {
		this.ccEmails = ccEmails;
	}

	public void setBccEmails(List<String> bccEmails) {
		this.bccEmails = bccEmails;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getEmailFromName() {
		return emailFromName;
	}

	public void setEmailFromName(String emailFromName) {
		this.emailFromName = emailFromName;
	}
	
	
}
