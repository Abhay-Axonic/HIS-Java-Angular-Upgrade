package com.param.global.dto;

import javax.persistence.Column;

public class LdapConfigurationMasterDto {
	private Integer ldapConfigurationId;

	private String initialContextFactory;
	
	private String securityAuthentication;
	
	private String securityPrincipal;
	
	private String securityCredentials;
	
	private String providerUrl;
	
	private Integer unitId;

	private Integer organizationId;

	public Integer getLdapConfigurationId() {
		return ldapConfigurationId;
	}

	public void setLdapConfigurationId(Integer ldapConfigurationId) {
		this.ldapConfigurationId = ldapConfigurationId;
	}

	public String getInitialContextFactory() {
		return initialContextFactory;
	}

	public void setInitialContextFactory(String initialContextFactory) {
		this.initialContextFactory = initialContextFactory;
	}

	public String getSecurityAuthentication() {
		return securityAuthentication;
	}

	public void setSecurityAuthentication(String securityAuthentication) {
		this.securityAuthentication = securityAuthentication;
	}

	public String getSecurityPrincipal() {
		return securityPrincipal;
	}

	public void setSecurityPrincipal(String securityPrincipal) {
		this.securityPrincipal = securityPrincipal;
	}

	public String getSecurityCredentials() {
		return securityCredentials;
	}

	public void setSecurityCredentials(String securityCredentials) {
		this.securityCredentials = securityCredentials;
	}

	public String getProviderUrl() {
		return providerUrl;
	}

	public void setProviderUrl(String providerUrl) {
		this.providerUrl = providerUrl;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}
	
	
}
