package com.param.billing.smartcard.dto;

public class PaymentModifiersDto {
	private String type; 
	private String amount;
	private String ref_number;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getRef_number() {
		return ref_number;
	}
	public void setRef_number(String ref_number) {
		this.ref_number = ref_number;
	}
	
	
}
