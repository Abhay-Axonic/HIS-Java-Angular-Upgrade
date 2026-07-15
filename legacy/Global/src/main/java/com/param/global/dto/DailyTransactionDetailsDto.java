package com.param.global.dto;

import java.util.Date;

public class DailyTransactionDetailsDto {
	
	private Integer id;
	
	private Date date;
	
	private Boolean isCash; 
	
	private Boolean isCard; 
	
	private Boolean isCheque; 
	
	private Integer organizationId;
	
	private Integer unitId;
	
	private Integer cashTransactionDetailsId;
	
	private Integer chequeTransactionDetailsId;
	
	private Integer cardTransactionDetailsId;
	
	private Integer cashRefundId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Boolean getIsCash() {
		return isCash;
	}

	public void setIsCash(Boolean isCash) {
		this.isCash = isCash;
	}

	public Boolean getIsCard() {
		return isCard;
	}

	public void setIsCard(Boolean isCard) {
		this.isCard = isCard;
	}

	public Boolean getIsCheque() {
		return isCheque;
	}

	public void setIsCheque(Boolean isCheque) {
		this.isCheque = isCheque;
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

	public Integer getCashTransactionDetailsId() {
		return cashTransactionDetailsId;
	}

	public void setCashTransactionDetailsId(Integer cashTransactionDetailsId) {
		this.cashTransactionDetailsId = cashTransactionDetailsId;
	}

	public Integer getChequeTransactionDetailsId() {
		return chequeTransactionDetailsId;
	}

	public void setChequeTransactionDetailsId(Integer chequeTransactionDetailsId) {
		this.chequeTransactionDetailsId = chequeTransactionDetailsId;
	}

	public Integer getCardTransactionDetailsId() {
		return cardTransactionDetailsId;
	}

	public void setCardTransactionDetailsId(Integer cardTransactionDetailsId) {
		this.cardTransactionDetailsId = cardTransactionDetailsId;
	}

	public Integer getCashRefundId() {
		return cashRefundId;
	}

	public void setCashRefundId(Integer cashRefundId) {
		this.cashRefundId = cashRefundId;
	}
	
	
}
