package com.yroots.lambda.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "categories", uniqueConstraints = { @UniqueConstraint(columnNames = { "name" }) })
public class Category extends AuditInfo {
	private static final long serialVersionUID = -325597237847722852L;

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "id", updatable = false, nullable = false)
	private String id;

	@Column(length = 200)
	@NotNull
	private String name;
	
	private boolean isActive=false;
	
	
	private boolean isEmailActive=false;
		
	private boolean isSmsActive=false;
		
	@ManyToOne(fetch=FetchType.EAGER)
	private EmailAccount emailAccount;
	
	@ManyToOne(fetch=FetchType.EAGER)
	private SMSAccount smsAccount;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public EmailAccount getEmailAccount() {
		return emailAccount;
	}

	public void setEmailAccount(EmailAccount emailAccount) {
		this.emailAccount = emailAccount;
	}

	public SMSAccount getSmsAccount() {
		return smsAccount;
	}

	public void setSmsAccount(SMSAccount smsAccount) {
		this.smsAccount = smsAccount;
	}

	public boolean isEmailActive() {
		return isEmailActive;
	}

	public boolean isSmsActive() {
		return isSmsActive;
	}

	public void setEmailActive(boolean isEmailActive) {
		this.isEmailActive = isEmailActive;
	}

	public void setSmsActive(boolean isSmsActive) {
		this.isSmsActive = isSmsActive;
	}
}
