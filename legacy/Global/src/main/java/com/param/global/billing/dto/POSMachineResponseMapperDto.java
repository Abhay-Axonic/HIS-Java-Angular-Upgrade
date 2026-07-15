package com.param.global.billing.dto;

public class POSMachineResponseMapperDto {
	private int posMachineResponseId;
	
	private String requestedUniqueId;
	private String deviceId;

	private String requestedJson;
	private String generatedReponse;

	private Integer organizationId;
	
	private Integer unitId;
	private Integer posMachineDeviceId;

	private char status;

	private String createdDate;

	private Integer createdBy;

	private String updatedDate;

	private Integer updatedBy;
	private Integer patientId;
	private String credCardHolderName;
	private String credCardTypeId;
	private String credAmount;
	private String credCardNumber;
	private String credExpiryDate;
	private String transactionNo;
	private boolean isConsumed;
	private Double transactionAmount;
	private String machineConfigDetails;
	private String hmacKey;
	private String nonKey;
	private String partnercode;
	public int getPosMachineResponseId() {
		return posMachineResponseId;
	}

	public void setPosMachineResponseId(int posMachineResponseId) {
		this.posMachineResponseId = posMachineResponseId;
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

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
	}


	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getGeneratedReponse() {
		return generatedReponse;
	}

	public void setGeneratedReponse(String generatedReponse) {
		this.generatedReponse = generatedReponse;
	}

	public String getRequestedUniqueId() {
		return requestedUniqueId;
	}

	public void setRequestedUniqueId(String requestedUniqueId) {
		this.requestedUniqueId = requestedUniqueId;
	}

	public String getRequestedJson() {
		return requestedJson;
	}

	public String getCredCardHolderName() {
		return credCardHolderName;
	}

	public void setCredCardHolderName(String credCardHolderName) {
		this.credCardHolderName = credCardHolderName;
	}

	public String getCredCardTypeId() {
		return credCardTypeId;
	}

	public void setCredCardTypeId(String credCardTypeId) {
		this.credCardTypeId = credCardTypeId;
	}

	public String getCredAmount() {
		return credAmount;
	}

	public void setCredAmount(String credAmount) {
		this.credAmount = credAmount;
	}

	public String getCredCardNumber() {
		return credCardNumber;
	}

	public void setCredCardNumber(String credCardNumber) {
		this.credCardNumber = credCardNumber;
	}
	public void setRequestedJson(String requestedJson) {
		this.requestedJson = requestedJson;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getCredExpiryDate() {
		return credExpiryDate;
	}

	public void setCredExpiryDate(String credExpiryDate) {
		this.credExpiryDate = credExpiryDate;
	}

	public String getTransactionNo() {
		return transactionNo;
	}

	public void setTransactionNo(String transactionNo) {
		this.transactionNo = transactionNo;
	}

	public boolean getIsConsumed() {
		return isConsumed;
	}

	public void setIsConsumed(boolean isConsumed) {
		this.isConsumed = isConsumed;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public Double getTransactionAmount() {
		return transactionAmount;
	}

	public void setTransactionAmount(Double transactionAmount) {
		this.transactionAmount = transactionAmount;
	}

	public Integer getPosMachineDeviceId() {
		return posMachineDeviceId;
	}

	public void setPosMachineDeviceId(Integer posMachineDeviceId) {
		this.posMachineDeviceId = posMachineDeviceId;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getMachineConfigDetails() {
		return machineConfigDetails;
	}

	public void setMachineConfigDetails(String machineConfigDetails) {
		this.machineConfigDetails = machineConfigDetails;
	}

	public String getHmacKey() {
		return hmacKey;
	}

	public void setHmacKey(String hmacKey) {
		this.hmacKey = hmacKey;
	}

	public String getNonKey() {
		return nonKey;
	}

	public void setNonKey(String nonKey) {
		this.nonKey = nonKey;
	}

	public String getPartnercode() {
		return partnercode;
	}

	public void setPartnercode(String partnercode) {
		this.partnercode = partnercode;
	}

	
	
}
