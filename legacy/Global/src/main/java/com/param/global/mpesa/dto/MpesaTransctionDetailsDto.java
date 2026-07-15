package com.param.global.mpesa.dto;

public class MpesaTransctionDetailsDto {

	private Integer mpesaTransactionId;
	
	private Double  amount ;
	
	private String phoneNumber;
	
	private String checkoutRequestId;
	
	private String merchantRequestId;
	
	private String partyA;
	
	private String responseCode;
	
	private String customerMessage;
	
	private String responseDescription;
	
	private String resultCode;
	
	private String resultDesc;
	
	private String uhidNumber;
	
	private boolean  isUsed;
	
	private Integer organizationId;
	
	private Integer unitId;
	
	private String createdDate;
	
	private String updatedDate;
	
	private Integer createdBy;
	
	private Integer updatedBy;
	
	private char status;

	public Integer getMpesaTransactionId() {
		return mpesaTransactionId;
	}

	public void setMpesaTransactionId(Integer mpesaTransactionId) {
		this.mpesaTransactionId = mpesaTransactionId;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getCheckoutRequestId() {
		return checkoutRequestId;
	}

	public void setCheckoutRequestId(String checkoutRequestId) {
		this.checkoutRequestId = checkoutRequestId;
	}

	public String getMerchantRequestId() {
		return merchantRequestId;
	}

	public void setMerchantRequestId(String merchantRequestId) {
		this.merchantRequestId = merchantRequestId;
	}

	public String getPartyA() {
		return partyA;
	}

	public void setPartyA(String partyA) {
		this.partyA = partyA;
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getCustomerMessage() {
		return customerMessage;
	}

	public void setCustomerMessage(String customerMessage) {
		this.customerMessage = customerMessage;
	}

	public String getResponseDescription() {
		return responseDescription;
	}

	public void setResponseDescription(String responseDescription) {
		this.responseDescription = responseDescription;
	}

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getResultDesc() {
		return resultDesc;
	}

	public void setResultDesc(String resultDesc) {
		this.resultDesc = resultDesc;
	}

	public String getUhidNumber() {
		return uhidNumber;
	}

	public void setUhidNumber(String uhidNumber) {
		this.uhidNumber = uhidNumber;
	}

	public boolean getUsed() {
		return isUsed;
	}

	public void setUsed(boolean isUsed) {
		this.isUsed = isUsed;
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

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
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
	
}
