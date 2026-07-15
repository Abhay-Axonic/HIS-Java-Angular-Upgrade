package com.param.adt.master.global.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "t_ldap_configuration", schema = "public")
@SequenceGenerator(name = "t_ldap_configuration_seq", sequenceName = "public.t_ldap_configuration_seq", allocationSize = 1)
public class LdapConfigurationMaster {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_ldap_configuration_seq")
	@Column(name = "ldap_configuration_id")
	private Integer ldapConfigurationId;

	@Column(name = "initial_context_factory")
	private String initialContextFactory;
	
	@Column(name = "security_authentication")
	private String securityAuthentication;
	
	@Column(name = "security_principal")
	private String securityPrincipal;
	
	@Column(name = "security_credentials")
	private String securityCredentials;
	
	@Column(name = "provider_url")
	private String providerUrl;
	
	@Column(name = "unit_id")
	private Integer unitId;

	@Column(name = "organization_id")
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
