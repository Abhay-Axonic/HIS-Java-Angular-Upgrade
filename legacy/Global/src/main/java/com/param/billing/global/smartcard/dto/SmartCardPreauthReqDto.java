package com.param.billing.global.smartcard.dto;

public class SmartCardPreauthReqDto {
	
	private String code;
	private Double amount;
	private String message;
	private String authorizedBy;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getAuthorizedBy() {
		return authorizedBy;
	}
	public void setAuthorizedBy(String authorizedBy) {
		this.authorizedBy = authorizedBy;
	}
	
	
	
}
