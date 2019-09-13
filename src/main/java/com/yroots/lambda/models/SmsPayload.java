package com.yroots.lambda.models;

import java.util.List;

public class SmsPayload{

	private List<String> contacts;
	private String templateUrl;	
	private String text;	
	private List<KeyValue> data;
	public List<String> getContacts() {
		return contacts;
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
	public void setContacts(List<String> contacts) {
		this.contacts = contacts;
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
}
