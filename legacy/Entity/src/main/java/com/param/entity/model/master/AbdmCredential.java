package com.param.entity.model.master;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "m_abdm_credential", schema = "abdm")
@SequenceGenerator(name = "m_abdm_credential_seq", sequenceName = "abdm.m_abdm_credential_seq", allocationSize = 1)
public class AbdmCredential {

@Id
@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_abdm_credential_seq")

@Column(name = "abdm_credential_id")
private Integer abdmCredentialId;

@Column(name = "clientId")
private String clientId;

@Column(name = "client_secret")
private String clientSecret;

@Column(name = "faciality_json")
private String facialityJson;

@Column(name = "status")
private Character status;

@Column(name = "organization_id")
private Integer organizationId;

@Column(name = "unit_id")
private Integer unitId;

@Column(name = "dev_url")
private String devUrl;

@Column(name = "healthidsbx_url")
private String healthidsbxUrl;

@Column(name = "phrsbx_url")
private String phrsbxUrl;


@Column(name = "gateway_url")
private String gatewayUrl;

@Column(name = "id")
private String id;

@Column(name = "alise_Name")
private String aliseName;

@Column(name = "dev_abdm_url")
private String devAbdmUrl;

@Column(name = "xcm_id")
private String xcmId;

@Column(name = "datapush_url")
private String datapushUrl;

public Integer getAbdmCredentialId() {
	return abdmCredentialId;
}

public void setAbdmCredentialId(Integer abdmCredentialId) {
	this.abdmCredentialId = abdmCredentialId;
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

public Character getStatus() {
	return status;
}

public void setStatus(Character status) {
	this.status = status;
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

public String getFacialityJson() {
	return facialityJson;
}

public void setFacialityJson(String facialityJson) {
	this.facialityJson = facialityJson;
}

public String getDevUrl() {
	return devUrl;
}

public void setDevUrl(String devUrl) {
	this.devUrl = devUrl;
}

public String getHealthidsbxUrl() {
	return healthidsbxUrl;
}

public void setHealthidsbxUrl(String healthidsbxUrl) {
	this.healthidsbxUrl = healthidsbxUrl;
}

public String getPhrsbxUrl() {
	return phrsbxUrl;
}

public void setPhrsbxUrl(String phrsbxUrl) {
	this.phrsbxUrl = phrsbxUrl;
}

public String getGatewayUrl() {
	return gatewayUrl;
}

public void setGatewayUrl(String gatewayUrl) {
	this.gatewayUrl = gatewayUrl;
}

public String getId() {
	return id;
}

public void setId(String id) {
	this.id = id;
}

public String getAliseName() {
	return aliseName;
}

public void setAliseName(String aliseName) {
	this.aliseName = aliseName;
}

public String getDevAbdmUrl() {
	return devAbdmUrl;
}

public void setDevAbdmUrl(String devAbdmUrl) {
	this.devAbdmUrl = devAbdmUrl;
}

public String getXcmId() {
	return xcmId;
}

public void setXcmId(String xcmId) {
	this.xcmId = xcmId;
}

public String getDatapushUrl() {
	return datapushUrl;
}

public void setDatapushUrl(String datapushUrl) {
	this.datapushUrl = datapushUrl;
}



}

