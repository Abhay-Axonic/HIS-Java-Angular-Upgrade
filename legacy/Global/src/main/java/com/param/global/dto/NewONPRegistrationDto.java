package com.param.global.dto;

public class NewONPRegistrationDto {
	private String connectionString;
	private String dbconnection;
	private String dbUser;
	private String dbPassword;

	public String getDbconnection() {
		return dbconnection;
	}

	public void setDbconnection(String dbconnection) {
		this.dbconnection = dbconnection;
	}

	public String getDbUser() {
		return dbUser;
	}

	public void setDbUser(String dbUser) {
		this.dbUser = dbUser;
	}

	public String getDbPassword() {
		return dbPassword;
	}

	public void setDbPassword(String dbPassword) {
		this.dbPassword = dbPassword;
	}

	public String getConnectionString() {
		return connectionString;
	}

	public void setConnectionString(String connectionString) {
		this.connectionString = connectionString;
	}
	
	
	
}
