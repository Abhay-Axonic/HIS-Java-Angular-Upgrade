package com.param.global.org.common.dto;

import java.math.BigDecimal;
import java.math.BigInteger;

import javax.persistence.Column;

public class LoyaltyCardResDto {

	private Integer cardConfigurationId;
    private BigDecimal minStartAmount;
	private BigDecimal thresholdAmount;
	private BigDecimal minRechargeAmount;
	private Double availableDeposit;
	private Integer loyaltyCardId;
	private String cardNo;
	private char isActive;
	
	public Integer getCardConfigurationId() {
		return cardConfigurationId;
	}
	public void setCardConfigurationId(Integer cardConfigurationId) {
		this.cardConfigurationId = cardConfigurationId;
	}
	public BigDecimal getMinStartAmount() {
		return minStartAmount;
	}
	public void setMinStartAmount(BigDecimal minStartAmount) {
		this.minStartAmount = minStartAmount;
	}
	public BigDecimal getThresholdAmount() {
		return thresholdAmount;
	}
	public void setThresholdAmount(BigDecimal thresholdAmount) {
		this.thresholdAmount = thresholdAmount;
	}
	public BigDecimal getMinRechargeAmount() {
		return minRechargeAmount;
	}
	public void setMinRechargeAmount(BigDecimal minRechargeAmount) {
		this.minRechargeAmount = minRechargeAmount;
	}
	public Double getAvailableDeposit() {
		return availableDeposit;
	}
	public void setAvailableDeposit(Double availableDeposit) {
		this.availableDeposit = availableDeposit;
	}
	public Integer getLoyaltyCardId() {
		return loyaltyCardId;
	}
	public void setLoyaltyCardId(Integer loyaltyCardId) {
		this.loyaltyCardId = loyaltyCardId;
	}
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public char getIsActive() {
		return isActive;
	}
	public void setIsActive(char isActive) {
		this.isActive = isActive;
	}
	
}
