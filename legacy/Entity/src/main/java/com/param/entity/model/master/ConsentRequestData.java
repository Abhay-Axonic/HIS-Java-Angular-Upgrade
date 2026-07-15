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
@Table(name = "m_consent_request_data", schema = "abdm")
@SequenceGenerator(name = "m_consent_request_data_seq", sequenceName = "abdm.m_consent_request_data_seq", allocationSize = 1)

public class ConsentRequestData {
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_consent_request_data_seq")
	
	@Column(name = "consent_request_id")
	private Integer consentRequestId;

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
	
	@Column(name = "consent_artefacts_id")
	private String consentArtefactsId;
	
	@Column(name = "api_response_onfetch_m3")
	private String apiResponseOnfetchtM3;
	
	@Column(name = "hi_types")
	private String hiTypes;
	
	@Column(name = "transaction_id")
	private String transactionId;
	
	@Column(name = "api_response_onrequest_m3")
	private String apiResponseOnRequesttM3;
	
	@Column(name = "api_response_hip")
	private String apiResponsehip;
	
	@Column(name = "private_key")
	private String privateKey;
	
	@Column(name = "organisation_id")
	private Integer organisationId;
	
	@Column(name = "unit_id")
	private Integer unitId;
	
	@Column(name = "patient_name")
	private String patientName;
	
	@Column(name = "consent_response_hitypes")
	private String consentResponseHitypes;
	
	@Column(name = "consent_denied_date")
	private String consentDeniedDate;
	
	@Column(name = "consent_revoked_date")
	private String consentRevokedDate;
	
	@Column(name = "consent_granted_date")
	private String consentGrantedDate;
	
	@Column(name = "consent_from_date")
	private String consentFromDate;
	
	@Column(name = "consent_todate")
	private String consentToDate;
	
	@Column(name = "mobile_number")
	private String mobileNumber;
	
	@Column(name = "consent_request_id_hiu")
	private String consentRequestIdHiu;

	@Column(name = "health_number")
	private String healthNumber;
	
	@Column(name = "patient_full_name")
	private String patientFullName;
	
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


	public Integer getConsentRequestId() {
		return consentRequestId;
	}

	public void setConsentRequestId(Integer consentRequestId) {
		this.consentRequestId = consentRequestId;
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

	public String getConsentArtefactsId() {
		return consentArtefactsId;
	}

	public void setConsentArtefactsId(String consentArtefactsId) {
		this.consentArtefactsId = consentArtefactsId;
	}

	public String getApiResponseOnfetchtM3() {
		return apiResponseOnfetchtM3;
	}

	public void setApiResponseOnfetchtM3(String apiResponseOnfetchtM3) {
		this.apiResponseOnfetchtM3 = apiResponseOnfetchtM3;
	}

	public String getHiTypes() {
		return hiTypes;
	}

	public void setHiTypes(String hiTypes) {
		this.hiTypes = hiTypes;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getApiResponseOnRequesttM3() {
		return apiResponseOnRequesttM3;
	}

	public void setApiResponseOnRequesttM3(String apiResponseOnRequesttM3) {
		this.apiResponseOnRequesttM3 = apiResponseOnRequesttM3;
	}

	public String getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}

	public Integer getOrganisationId() {
		return organisationId;
	}

	public void setOrganisationId(Integer organisationId) {
		this.organisationId = organisationId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getApiResponsehip() {
		return apiResponsehip;
	}

	public void setApiResponsehip(String apiResponsehip) {
		this.apiResponsehip = apiResponsehip;
	}

	public String getConsentResponseHitypes() {
		return consentResponseHitypes;
	}

	public void setConsentResponseHitypes(String consentResponseHitypes) {
		this.consentResponseHitypes = consentResponseHitypes;
	}

	public String getConsentDeniedDate() {
		return consentDeniedDate;
	}

	public void setConsentDeniedDate(String consentDeniedDate) {
		this.consentDeniedDate = consentDeniedDate;
	}

	public String getConsentRevokedDate() {
		return consentRevokedDate;
	}

	public void setConsentRevokedDate(String consentRevokedDate) {
		this.consentRevokedDate = consentRevokedDate;
	}

	public String getConsentGrantedDate() {
		return consentGrantedDate;
	}

	public void setConsentGrantedDate(String consentGrantedDate) {
		this.consentGrantedDate = consentGrantedDate;
	}

	public String getConsentFromDate() {
		return consentFromDate;
	}

	public void setConsentFromDate(String consentFromDate) {
		this.consentFromDate = consentFromDate;
	}

	public String getConsentToDate() {
		return consentToDate;
	}

	public void setConsentToDate(String consentToDate) {
		this.consentToDate = consentToDate;
	}

	public String getHealthNumber() {
		return healthNumber;
	}

	public void setHealthNumber(String healthNumber) {
		this.healthNumber = healthNumber;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getConsentRequestIdHiu() {
		return consentRequestIdHiu;
	}

	public void setConsentRequestIdHiu(String consentRequestIdHiu) {
		this.consentRequestIdHiu = consentRequestIdHiu;
	}

	public String getPatientFullName() {
		return patientFullName;
	}

	public void setPatientFullName(String patientFullName) {
		this.patientFullName = patientFullName;
	}

	
}

