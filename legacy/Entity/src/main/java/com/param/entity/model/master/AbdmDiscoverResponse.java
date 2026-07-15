package com.param.entity.model.master;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


@Entity
@Table(name = "m_abdm_discover", schema = "abdm")
@SequenceGenerator(name = "m_abdm_discover_seq", sequenceName = "abdm.m_abdm_discover_seq", allocationSize = 1)
public class AbdmDiscoverResponse {


	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_abdm_discover_seq")
	
	@Column(name = "abdm_discover_id")
	private Integer abdmDiscoverId;

	@Column(name = "hip_id")
	private String hipId;

	@Column(name = "sbx_id")
	private String sbxId;
	
	@Column(name = "discover_json")
	private String discoverJson;
	
	@Column(name = "confirm_json")
	private String confirmJson;
	
	@Column(name = "init_json")
	private String initJson;
	
	@Column(name = "transaction_id")
	private String transactionId ;
	
	@Column(name = "request_id")
	private String requestId ;
	
	@Column(name = "init_request_id")
	private String initRequestId ;
	
	@Column(name = "confirm_request_id")
	private String confirmRequestId ;
	
	@Column(name = "uhid_id")
	private String uhidId ;
	
	@Column(name = "encounter_number")
	private String encounterNumber ;

	
	@Column(name = "otp")
	private String otp ;
	
	@Column(name = "api_response_event_30")
	private String apiResponseEvent30;
	
	@Column(name = "on_init_response_json")
	private String onInitResponseJson;
	
	public Integer getAbdmDiscoverId() {
		return abdmDiscoverId;
	}

	public void setAbdmDiscoverId(Integer abdmDiscoverId) {
		this.abdmDiscoverId = abdmDiscoverId;
	}

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

	public String getDiscoverJson() {
		return discoverJson;
	}

	public void setDiscoverJson(String discoverJson) {
		this.discoverJson = discoverJson;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getConfirmJson() {
		return confirmJson;
	}

	public void setConfirmJson(String confirmJson) {
		this.confirmJson = confirmJson;
	}

	public String getInitJson() {
		return initJson;
	}

	public void setInitJson(String initJson) {
		this.initJson = initJson;
	}

	public String getInitRequestId() {
		return initRequestId;
	}

	public void setInitRequestId(String initRequestId) {
		this.initRequestId = initRequestId;
	}

	public String getConfirmRequestId() {
		return confirmRequestId;
	}

	public void setConfirmRequestId(String confirmRequestId) {
		this.confirmRequestId = confirmRequestId;
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public String getUhidId() {
		return uhidId;
	}

	public void setUhidId(String uhidId) {
		this.uhidId = uhidId;
	}

	public String getEncounterNumber() {
		return encounterNumber;
	}

	public void setEncounterNumber(String encounterNumber) {
		this.encounterNumber = encounterNumber;
	}

	public String getApiResponseEvent30() {
		return apiResponseEvent30;
	}

	public void setApiResponseEvent30(String apiResponseEvent30) {
		this.apiResponseEvent30 = apiResponseEvent30;
	}

	public String getOnInitResponseJson() {
		return onInitResponseJson;
	}

	public void setOnInitResponseJson(String onInitResponseJson) {
		this.onInitResponseJson = onInitResponseJson;
	}
	

}
