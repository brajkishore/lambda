package com.yroots.lambda.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "subscriptions")
public class Subscription extends AuditInfo {
	private static final long serialVersionUID = -3356325905994547913L;
	
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "id", updatable = false, nullable = false)
	private String id;

	@NotNull
	@Column(columnDefinition="TEXT")
	private String topicName;
	
	@NotNull
	private String user_id;	
	private boolean isEmailSub=false;
	private boolean isSMSSub=false;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTopicName() {
		return topicName;
	}
	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public boolean isEmailSub() {
		return isEmailSub;
	}
	public void setEmailSub(boolean isEmailSub) {
		this.isEmailSub = isEmailSub;
	}
	public boolean isSMSSub() {
		return isSMSSub;
	}
	public void setSMSSub(boolean isSMSSub) {
		this.isSMSSub = isSMSSub;
	}
	
	
}
