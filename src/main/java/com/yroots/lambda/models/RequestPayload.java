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
	private List<String> emails;
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

	public List<String> getEmails() {
		return emails;
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

	public void setEmails(List<String> emails) {
		this.emails = emails;
	}

	public void setFormattedText(String formattedText) {
		this.formattedText = formattedText;
	}

	public void setData(List<KeyValue> data) {
		this.data = data;
	}
}
