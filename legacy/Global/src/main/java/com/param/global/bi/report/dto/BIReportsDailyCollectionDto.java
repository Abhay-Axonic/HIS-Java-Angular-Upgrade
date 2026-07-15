package com.param.global.bi.report.dto;

public class BIReportsDailyCollectionDto {
	
	private String depositDate;
	private String unitName;
	private Double cash;
	
	private Double online;
	
	private Double cheque;
	
	private Double creditcard;
	
	private Double debitcard;

	private Double mpesa;

	private Double loyaltycard;
	
	private Double eTransfer;


	public String getDepositDate() {
		return depositDate;
	}

	public void setDepositDate(String depositDate) {
		this.depositDate = depositDate;
	}


	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public Double getCash() {
		return cash;
	}

	public void setCash(Double cash) {
		this.cash = cash;
	}

	public Double getOnline() {
		return online;
	}

	public void setOnline(Double online) {
		this.online = online;
	}

	public Double getCheque() {
		return cheque;
	}

	public void setCheque(Double cheque) {
		this.cheque = cheque;
	}

	public Double getCreditcard() {
		return creditcard;
	}

	public void setCreditcard(Double creditcard) {
		this.creditcard = creditcard;
	}

	public Double getDebitcard() {
		return debitcard;
	}

	public void setDebitcard(Double debitcard) {
		this.debitcard = debitcard;
	}

	public Double getMpesa() {
		return mpesa;
	}

	public void setMpesa(Double mpesa) {
		this.mpesa = mpesa;
	}

	public Double getLoyaltycard() {
		return loyaltycard;
	}

	public void setLoyaltycard(Double loyaltycard) {
		this.loyaltycard = loyaltycard;
	}


	public Double geteTransfer() {
		return eTransfer;
	}

	public void seteTransfer(Double eTransfer) {
		this.eTransfer = eTransfer;
	}
	
}
