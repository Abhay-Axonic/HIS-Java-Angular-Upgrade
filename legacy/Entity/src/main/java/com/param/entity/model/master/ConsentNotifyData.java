package com.param.entity.model.master;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "m_consent_notify_data", schema = "abdm")
@SequenceGenerator(name = "m_consent_notify_data_seq", sequenceName = "abdm.m_consent_notify_data_seq", allocationSize = 1)

public class ConsentNotifyData {
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_consent_notify_data_seq")
	
	@Column(name = "consent_notify_id")
	private Integer consentNotifyId;

	@Column(name = "consent_id")
	private String consentId;
	
	@Column(name = "hip_id")
	private String hipId;

	@Column(name = "sbx_id")
	private String sbxId;
	
	@Column(name = "consent_notify_json")
	private String consentNotifyJson;
	
	@Column(name = "abha_id")
	private String abhaId;
	
	@Column(name = "request_id")
	private String requestId;

	@Column(name = "status")
	private Character status;
	
	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "api_response_oninit_request")
	private String apiResponseOninitRequest;
	
	@Column(name = "api_response_notify_request_m3")
	private String apiResponseNotifyRequestM3;
	
	@Column(name = "consent_status")
	private String consentStatus;
	
	@Column(name = "consent_expiry_date")
	private String consentExpiryDate;

	public String getHipId() {
		return hipId;
	}

	public void setHipId(String hipId) {
		this.hipId = hipId;
	}

	public String getSbxId() {
		return sbxId;
	}

	public void setSbxId(String sbxId) {
		this.sbxId = sbxId;
	}

	public String getAbhaId() {
		return abhaId;
	}

	public void setAbhaId(String abhaId) {
		this.abhaId = abhaId;
	}

	public Integer getConsentNotifyId() {
		return consentNotifyId;
	}

	public void setConsentNotifyId(Integer consentNotifyId) {
		this.consentNotifyId = consentNotifyId;
	}

	public String getConsentNotifyJson() {
		return consentNotifyJson;
	}

	public void setConsentNotifyJson(String consentNotifyJson) {
		this.consentNotifyJson = consentNotifyJson;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getConsentId() {
		return consentId;
	}

	public void setConsentId(String consentId) {
		this.consentId = consentId;
	}

	public String getApiResponseOninitRequest() {
		return apiResponseOninitRequest;
	}

	public void setApiResponseOninitRequest(String apiResponseOninitRequest) {
		this.apiResponseOninitRequest = apiResponseOninitRequest;
	}

	public String getApiResponseNotifyRequestM3() {
		return apiResponseNotifyRequestM3;
	}

	public void setApiResponseNotifyRequestM3(String apiResponseNotifyRequestM3) {
		this.apiResponseNotifyRequestM3 = apiResponseNotifyRequestM3;
	}

	public String getConsentStatus() {
		return consentStatus;
	}

	public void setConsentStatus(String consentStatus) {
		this.consentStatus = consentStatus;
	}

	public String getConsentExpiryDate() {
		return consentExpiryDate;
	}

	public void setConsentExpiryDate(String consentExpiryDate) {
		this.consentExpiryDate = consentExpiryDate;
	}

	
}

