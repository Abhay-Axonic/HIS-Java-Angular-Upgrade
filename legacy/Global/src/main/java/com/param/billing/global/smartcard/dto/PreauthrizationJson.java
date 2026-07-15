package com.param.billing.global.smartcard.dto;

public class PreauthrizationJson {
	private String code;     
	private Double amount;   
	private String authorized_by;   
	private String message;
	
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
