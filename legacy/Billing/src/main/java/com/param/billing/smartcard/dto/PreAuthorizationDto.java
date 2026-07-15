package com.param.billing.smartcard.dto;

public class PreAuthorizationDto {

	private String code;
	private String amount;
	private String authorized_by;
	private String message;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getAuthorized_by() {
		return authorized_by;
	}
	public void setAuthorized_by(String authorized_by) {
		this.authorized_by = authorized_by;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
