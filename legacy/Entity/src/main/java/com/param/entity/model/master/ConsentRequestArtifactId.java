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
@Table(name = "m_consent_request_artifact_id", schema = "abdm")
@SequenceGenerator(name = "m_consent_request_artifact_id_seq", sequenceName = "abdm.m_consent_request_artifact_id_seq", allocationSize = 1)

public class ConsentRequestArtifactId {
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_consent_request_artifact_id_seq")
	
	
	@Column(name = "consent_request_artifact_id")
	private Integer consentRequestArtifactId;

	@Column(name = "consent_request_id")
	private String consentRequestId;

	@Column(name = "consent_id")
	private String consentId;
	

	@Column(name = "consent_artefacts_id")
	private String consentArtefactsId;


	@Column(name = "transaction_id")
	private String transactionId;
	
	@Column(name = "fetch_request_id")
	private String fetchRequestId;
	
	@Column(name = "request_uuid")
	private String requestUuid;

	public Integer getConsentRequestArtifactId() {
		return consentRequestArtifactId;
	}


	public void setConsentRequestArtifactId(Integer consentRequestArtifactId) {
		this.consentRequestArtifactId = consentRequestArtifactId;
	}


	


	public String getConsentRequestId() {
		return consentRequestId;
	}


	public void setConsentRequestId(String consentRequestId) {
		this.consentRequestId = consentRequestId;
	}


	public String getConsentId() {
		return consentId;
	}


	public void setConsentId(String consentId) {
		this.consentId = consentId;
	}


	public String getConsentArtefactsId() {
		return consentArtefactsId;
	}


	public void setConsentArtefactsId(String consentArtefactsId) {
		this.consentArtefactsId = consentArtefactsId;
	}


	public String getTransactionId() {
		return transactionId;
	}


	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}


	public String getFetchRequestId() {
		return fetchRequestId;
	}


	public void setFetchRequestId(String fetchRequestId) {
		this.fetchRequestId = fetchRequestId;
	}


	public String getRequestUuid() {
		return requestUuid;
	}


	public void setRequestUuid(String requestUuid) {
		this.requestUuid = requestUuid;
	}

	
}
