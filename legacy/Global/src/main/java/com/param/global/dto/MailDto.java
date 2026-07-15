package com.param.global.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Properties;

public class MailDto {

	 @JsonIgnore
	 private String host;
	 @JsonIgnore
	 private String port;
	 @JsonIgnore
	 private String id;
	 @JsonIgnore
	 private String password;
	 
	 private Properties properties;
	 
	public Properties getProperties() {
		return properties;
	}
	public void setProperties(Properties properties) {
		this.properties = properties;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	 
}
