package com.param.global.dto;

import java.util.Date;

public class LoyaltyCardLogDto {

	private int loyaltyCardLogId;
	
	private String loyaltyCardNo;
	
	private Integer patientId;
	
	private String otp;
	
	private Integer otpCount;
	
	private Double amount;
	
	private String authKey;
	
	private char isVerified;
	
	private String otpExpiredTime;
	
	private Date otpExpiredTimeInDate;
	
	private String cardBlockDate;
	
	private Integer organizationId;
	
	private Integer unitId;
	
	private char status;

	private String createdDate;

	private Integer createdBy;

	private Integer updatedBy;

	private String updatedDate;
	
	private Integer loyaltyCardStatus;

	public int getLoyaltyCardLogId() {
		return loyaltyCardLogId;
	}

	public void setLoyaltyCardLogId(int loyaltyCardLogId) {
		this.loyaltyCardLogId = loyaltyCardLogId;
	}

	public String getLoyaltyCardNo() {
		return loyaltyCardNo;
	}

	public void setLoyaltyCardNo(String loyaltyCardNo) {
		this.loyaltyCardNo = loyaltyCardNo;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public Integer getOtpCount() {
		return otpCount;
	}

	public void setOtpCount(Integer otpCount) {
		this.otpCount = otpCount;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}


	public char getIsVerified() {
		return isVerified;
	}

	public void setIsVerified(char isVerified) {
		this.isVerified = isVerified;
	}

	public String getOtpExpiredTime() {
		return otpExpiredTime;
	}

	public void setOtpExpiredTime(String otpExpiredTime) {
		this.otpExpiredTime = otpExpiredTime;
	}

	public String getCardBlockDate() {
		return cardBlockDate;
	}

	public void setCardBlockDate(String cardBlockDate) {
		this.cardBlockDate = cardBlockDate;
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

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}


	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public String getAuthKey() {
		return authKey;
	}

	public void setAuthKey(String authKey) {
		this.authKey = authKey;
	}

	public Integer getLoyaltyCardStatus() {
		return loyaltyCardStatus;
	}

	public void setLoyaltyCardStatus(Integer loyaltyCardStatus) {
		this.loyaltyCardStatus = loyaltyCardStatus;
	}

	public Date getOtpExpiredTimeInDate() {
		return otpExpiredTimeInDate;
	}

	public void setOtpExpiredTimeInDate(Date otpExpiredTimeInDate) {
		this.otpExpiredTimeInDate = otpExpiredTimeInDate;
	}
}
