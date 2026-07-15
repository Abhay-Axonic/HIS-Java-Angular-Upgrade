package com.param.billing.cashDesk.dto;

public class BillingIdDetailsDto {

	private Integer paymentReceiptDetailsId;
	
	private Integer paymentRefundId;
	
	private Integer interimHandoverId;

	
	public Integer getPaymentRefundId() {
		return paymentRefundId;
	}

	public void setPaymentRefundId(Integer paymentRefundId) {
		this.paymentRefundId = paymentRefundId;
	}

	public Integer getInterimHandoverId() {
		return interimHandoverId;
	}

	public void setInterimHandoverId(Integer interimHandoverId) {
		this.interimHandoverId = interimHandoverId;
	}

	public Integer getPaymentReceiptDetailsId() {
		return paymentReceiptDetailsId;
	}

	public void setPaymentReceiptDetailsId(Integer paymentReceiptDetailsId) {
		this.paymentReceiptDetailsId = paymentReceiptDetailsId;
	}

	
}
