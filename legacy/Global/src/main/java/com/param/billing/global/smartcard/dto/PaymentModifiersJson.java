package com.param.billing.global.smartcard.dto;

public class PaymentModifiersJson {

	private String type;
	private Double amount;
	private String ref_number;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getRef_number() {
		return ref_number;
	}

	public void setRef_number(String ref_number) {
		this.ref_number = ref_number;
	}

}
