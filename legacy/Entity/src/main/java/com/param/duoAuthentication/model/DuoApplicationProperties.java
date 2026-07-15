package com.param.duoAuthentication.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "m_duo_application_properties" , schema = "public")
@SequenceGenerator(name = "m_duo_application_properties_seq" , sequenceName = "m_duo_application_properties_seq" , allocationSize = 1)
public class DuoApplicationProperties {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "m_duo_application_properties_seq")
	@Column(name = "duo_application_properties_id")
	private Integer duo_application_properties_id;
	
	@Column(name = "client_id")
	private String clientId;
	
	@Column(name = "client_secret")
	private String clientSecret;
	
	@Column(name = "api_host")
	private String apiHost;
	
	@Column(name = "redirect_uri")
	private String redirectUri;
	
	@Column(name = "failmode")
	private String failmode;
	
	@Column(name = "organization_id")
	private Integer organizationId;
	
	@Column(name = "unit_id")
	private Integer unitId;

	public Integer getDuo_application_properties_id() {
		return duo_application_properties_id;
	}

	public void setDuo_application_properties_id(Integer duo_application_properties_id) {
		this.duo_application_properties_id = duo_application_properties_id;
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
	
	
}
