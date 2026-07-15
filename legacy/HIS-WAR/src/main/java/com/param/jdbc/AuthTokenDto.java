package com.param.jdbc;

public class AuthTokenDto {

	public String userName;
	public String password;
	public String secretKey;
	public String secretPassword;
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getSecretKey() {
		return secretKey;
	}
	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}
	public String getSecretPassword() {
		return secretPassword;
	}
	public void setSecretPassword(String secretPassword) {
		this.secretPassword = secretPassword;
	}
	
	
	
}
