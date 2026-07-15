package com.param.global.dto;

import java.util.List;

import org.json.simple.JSONObject;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class UserSSORequestDto {
	

	private Integer userId;
	private Integer organizationId;
	private Integer unitId;
	private Long lastLogin;
	private Long expiry;
	private String token;
	private String redirectURL;
	private List<JSONObject> permissions;
	
	
	
	
	
	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Long getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(Long lastLogin) {
		this.lastLogin = lastLogin;
	}

	public Long getExpiry() {
		return expiry;
	}

	public void setExpiry(Long expiry) {
		this.expiry = expiry;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getRedirectURL() {
		return redirectURL;
	}

	public void setRedirectURL(String redirectURL) {
		this.redirectURL = redirectURL;
	}

	public List<JSONObject> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<JSONObject> permissions) {
		this.permissions = permissions;
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

	
	
}
