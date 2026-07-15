package com.param.duoAuthentication.dto;

public class DuoApplicationPropertiesDto {
	private int duoApplicationPropertiesId;
	
	private String clientId;
	
	private String clientSecret;
	
	private String apiHost;
	
	private String redirectUri;
	
	private String username;
	private String password;
	private String failmode;
	private String userName;
	private String macAddress;

	private Integer organizationId;
	
	private Integer unitId;

	public int getDuoApplicationPropertiesId() {
		return duoApplicationPropertiesId;
	}

	public void setDuoApplicationPropertiesId(int duoApplicationPropertiesId) {
		this.duoApplicationPropertiesId = duoApplicationPropertiesId;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

	public String getApiHost() {
		return apiHost;
	}

	public void setApiHost(String apiHost) {
		this.apiHost = apiHost;
	}

	public String getRedirectUri() {
		return redirectUri;
	}

	public void setRedirectUri(String redirectUri) {
		this.redirectUri = redirectUri;
	}

	public String getFailmode() {
		return failmode;
	}

	public void setFailmode(String failmode) {
		this.failmode = failmode;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getMacAddress() {
		return macAddress;
	}

	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}

	
	
}
