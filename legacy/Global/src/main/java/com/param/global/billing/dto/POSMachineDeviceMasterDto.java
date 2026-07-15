package com.param.global.billing.dto;

public class POSMachineDeviceMasterDto {
private Integer posMachineDeviceId;
	
	private String deviceName;

	private Integer organizationId;
	
	private Integer unitId;

	private Character status;

	private String createdDate;

	private Integer createdBy;

	private String updatedDate;
	private String invoice;

	private Integer updatedBy;
	private String selectedPaymentModeJson;
	private String requestedJson;
	private String requestedUniqueId;
	private boolean isConsumed;
	private Integer patientId;
	private String machineConfigDetails;
	private String uploadLink;
	private String mobileNumber;
	private Double transactionAmount;
	private String requestHmac;
	private String response;
	private String requestedJsonString;
	private String partnercode;
	private String responseResult;
	private String transactionDetails;

	
	public Integer getPosMachineDeviceId() {
		return posMachineDeviceId;
	}

	public void setPosMachineDeviceId(Integer posMachineDeviceId) {
		this.posMachineDeviceId = posMachineDeviceId;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
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

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
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

	public String getSelectedPaymentModeJson() {
		return selectedPaymentModeJson;
	}

	public void setSelectedPaymentModeJson(String selectedPaymentModeJson) {
		this.selectedPaymentModeJson = selectedPaymentModeJson;
	}

	public String getRequestedJson() {
		return requestedJson;
	}

	public void setRequestedJson(String requestedJson) {
		this.requestedJson = requestedJson;
	}

	public String getInvoice() {
		return invoice;
	}

	public void setInvoice(String invoice) {
		this.invoice = invoice;
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

	public String getRequestedUniqueId() {
		return requestedUniqueId;
	}

	public void setRequestedUniqueId(String requestedUniqueId) {
		this.requestedUniqueId = requestedUniqueId;
	}

	public String getMachineConfigDetails() {
		return machineConfigDetails;
	}

	public void setMachineConfigDetails(String machineConfigDetails) {
		this.machineConfigDetails = machineConfigDetails;
	}

	public String getUploadLink() {
		return uploadLink;
	}

	public void setUploadLink(String uploadLink) {
		this.uploadLink = uploadLink;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public Double getTransactionAmount() {
		return transactionAmount;
	}

	public void setTransactionAmount(Double transactionAmount) {
		this.transactionAmount = transactionAmount;
	}

	public String getRequestHmac() {
		return requestHmac;
	}

	public void setRequestHmac(String requestHmac) {
		this.requestHmac = requestHmac;
	}

	public String getRequestedJsonString() {
		return requestedJsonString;
	}

	public void setRequestedJsonString(String requestedJsonString) {
		this.requestedJsonString = requestedJsonString;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public String getPartnercode() {
		return partnercode;
	}

	public void setPartnercode(String partnercode) {
		this.partnercode = partnercode;
	}

	public String getResponseResult() {
		return responseResult;
	}

	public void setResponseResult(String responseResult) {
		this.responseResult = responseResult;
	}

	public String getTransactionDetails() {
		return transactionDetails;
	}

	public void setTransactionDetails(String transactionDetails) {
		this.transactionDetails = transactionDetails;
	}

	
}
