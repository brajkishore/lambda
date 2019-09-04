package com.yroots.lambda.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "email_accounts",uniqueConstraints = { @UniqueConstraint(columnNames = { "name" }) })
public class EmailAccount extends AuditInfo {
	private static final long serialVersionUID = 6387971256894327138L;

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "id", updatable = false, nullable = false)
	private String id;
		
	@NotNull
	private String name;

	@NotNull
	private String formatedName;

	private String encoding="UTF-8";
	
	@NotNull
	private String host;
	
	@NotNull	
	private Integer port;
	
	@NotNull
	private String username;
		
	@NotNull
	private String password;
	
	@NotNull
	private String protocol;
		
	private boolean testConn=false;
	
	private boolean smtpAuth=false;
	private boolean isTlsEnabled=false;
	private boolean isDebugEnabled = false;
	private boolean isActive = false;

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

	public String getId() {
		return id;
	}

	public String getEncoding() {
		return encoding;
	}

	public String getHost() {
		return host;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getProtocol() {
		return protocol;
	}

	public boolean isTestConn() {
		return testConn;
	}

	public boolean isSmtpAuth() {
		return smtpAuth;
	}

	public boolean isTlsEnabled() {
		return isTlsEnabled;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public void setTestConn(boolean testConn) {
		this.testConn = testConn;
	}

	public void setSmtpAuth(boolean smtpAuth) {
		this.smtpAuth = smtpAuth;
	}

	public void setTlsEnabled(boolean isTlsEnabled) {
		this.isTlsEnabled = isTlsEnabled;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public boolean isDebugEnabled() {
		return isDebugEnabled;
	}

	public void setDebugEnabled(boolean isDebugEnabled) {
		this.isDebugEnabled = isDebugEnabled;
	}

	public String getFormatedName() {
		return formatedName;
	}

	public void setFormatedName(String formatedName) {
		this.formatedName = formatedName;
	}
	
	
}
