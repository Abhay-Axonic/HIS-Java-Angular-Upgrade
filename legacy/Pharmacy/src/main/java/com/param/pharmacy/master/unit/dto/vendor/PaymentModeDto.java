package com.param.pharmacy.master.unit.dto.vendor;

public class PaymentModeDto {

	protected Integer paymentModeId;
	
	protected String paymentModeDesc;

	public PaymentModeDto(Integer paymentModeId, String paymentModeDesc) {
		super();
		this.paymentModeId = paymentModeId;
		this.paymentModeDesc = paymentModeDesc;
	}

	public Integer getPaymentModeId() {
		return paymentModeId;
	}

	public void setPaymentModeId(Integer paymentModeId) {
		this.paymentModeId = paymentModeId;
	}

	public String getPaymentModeDesc() {
		return paymentModeDesc;
	}

	public void setPaymentModeDesc(String paymentModeDesc) {
		this.paymentModeDesc = paymentModeDesc;
	}
	
}
