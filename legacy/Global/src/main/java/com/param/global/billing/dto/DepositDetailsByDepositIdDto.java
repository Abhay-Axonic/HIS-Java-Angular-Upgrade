package com.param.global.billing.dto;

import java.math.BigDecimal;

public class DepositDetailsByDepositIdDto {
	
	private String paymentMode;
	private String BankName;
	private String chequeNumber;
	private String cardNo;
	private BigDecimal totalAmount;
	
	public String getPaymentMode() {
		return paymentMode;
	}
	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}
	public String getBankName() {
		return BankName;
	}
	public void setBankName(String bankName) {
		BankName = bankName;
	}

	public String getChequeNumber() {
		return chequeNumber;
	}
	public void setChequeNumber(String chequeNumber) {
		this.chequeNumber = chequeNumber;
	}
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public BigDecimal getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}
	
}
