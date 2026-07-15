package com.param.entity.model.master;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


@Entity
@Table(name = "m_call_back_response", schema = "abdm")
@SequenceGenerator(name = "m_call_back_response_m_call_back_response_id_seq", sequenceName = "abdm.m_call_back_response_m_call_back_response_id_seq", allocationSize = 1)
public class AbdmCallBackResponse {
	


	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_call_back_response_m_call_back_response_id_seq")
	
	@Column(name = "m_call_back_response_id")
	private Integer mCallBackResponseId;

	@Column(name = "hip_id")
	private String hipId;

	@Column(name = "call_back_json_on_fetch_mode")
	private String callBackJsonoNFetchMode;
	
	@Column(name = "call_back_json_on_init")
	private String callBackJsonoNOnInit;
	
	@Column(name = "call_back_json_on_confirm")
	private String callBackJsonoNOnConfirm;
	
	@Column(name = "request_id")
	private String requestId;
	
	@Column(name = "request_id_init")
	private String requestIdInit;
	
	@Column(name = "request_id_confirm")
	private String requestIdConfirm;
	
	@Column(name = "aadhar_number")
	private String aadharNumber;
	
	@Column(name = "organization_id")
	private Integer organizationId;

	@Column(name = "unit_id")
	private Integer unitId;
	
	@Column(name = "sbx_id")
	private String sbxId;
	
	@Column(name = "request_id_add_contexts")
	private String requestIdAddContexts;
	
	@Column(name = "call_back_json_on_add_contexts")
	private String callBackJsonOnAddContexts;
	

	public Integer getmCallBackResponseId() {
		return mCallBackResponseId;
	}

	public void setmCallBackResponseId(Integer mCallBackResponseId) {
		this.mCallBackResponseId = mCallBackResponseId;
	}

	public String getHipId() {
		return hipId;
	}

	public void setHipId(String hipId) {
		this.hipId = hipId;
	}

	public String getCallBackJsonoNFetchMode() {
		return callBackJsonoNFetchMode;
	}

	public void setCallBackJsonoNFetchMode(String callBackJsonoNFetchMode) {
		this.callBackJsonoNFetchMode = callBackJsonoNFetchMode;
	}

	public String getCallBackJsonoNOnInit() {
		return callBackJsonoNOnInit;
	}

	public void setCallBackJsonoNOnInit(String callBackJsonoNOnInit) {
		this.callBackJsonoNOnInit = callBackJsonoNOnInit;
	}

	public String getCallBackJsonoNOnConfirm() {
		return callBackJsonoNOnConfirm;
	}

	public void setCallBackJsonoNOnConfirm(String callBackJsonoNOnConfirm) {
		this.callBackJsonoNOnConfirm = callBackJsonoNOnConfirm;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getAadharNumber() {
		return aadharNumber;
	}

	public void setAadharNumber(String aadharNumber) {
		this.aadharNumber = aadharNumber;
	}

	public String getRequestIdInit() {
		return requestIdInit;
	}

	public void setRequestIdInit(String requestIdInit) {
		this.requestIdInit = requestIdInit;
	}

	public String getRequestIdConfirm() {
		return requestIdConfirm;
	}

	public void setRequestIdConfirm(String requestIdConfirm) {
		this.requestIdConfirm = requestIdConfirm;
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

	public String getSbxId() {
		return sbxId;
	}

	public void setSbxId(String sbxId) {
		this.sbxId = sbxId;
	}

	public String getRequestIdAddContexts() {
		return requestIdAddContexts;
	}

	public void setRequestIdAddContexts(String requestIdAddContexts) {
		this.requestIdAddContexts = requestIdAddContexts;
	}

	public String getCallBackJsonOnAddContexts() {
		return callBackJsonOnAddContexts;
	}

	public void setCallBackJsonOnAddContexts(String callBackJsonOnAddContexts) {
		this.callBackJsonOnAddContexts = callBackJsonOnAddContexts;
	}
	
	

}
